package com.javaweb.repository.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;


@Repository
public class BuildingRepositoryImpl implements BuildingRepository{
	static final String BD_URL = "jdbc:mysql://localhost:3306/estatebasic";
	static final String USER = "root";
	static final String PASS = "";
	
//	hàm xu ly join các bảng
	public static void joinTable(Map<String,Object> params,List<String> typeCode, StringBuilder sql)
	{
		String staffId= (String)params.get("staffId");
		if(StringUtil.checkString(staffId))
		{
			sql.append("inner join assignmentbuilding on b.id = assignmentbuilding.buildingid  ");
		}
		if(typeCode!=null && typeCode.size()!=0)
		{
			sql.append("inner join buildingrenttype on b.id = buildingrenttype.buildingid  ");
			sql.append("inner join renttype on renttype.id = buildingrenttype.renttypeid  ");
		}
		String rentAreaTo = (String)params.get("areaTo");
		String rentAreaFrom = (String)params.get("areaFrom");
		if(   StringUtil.checkString(rentAreaFrom )  || StringUtil.checkString(rentAreaTo)  )
		{
			sql.append("inner join rentarea on rentarea.buildingid =b.id");
		}
		
	}
	
//	xử lý câu lệnh query nomal ( không phức tạp)
	public static void queryNomal(Map<String,Object> params, StringBuilder where)
	{
		for(Map.Entry<String,Object >it:params.entrySet())
		{
			if(!it.getKey().equals("staffId")&&!it.getKey().equals("typeCode") && !it.getKey().startsWith("area")
					&& !it.getKey().startsWith("rentPrice")  )
			{
				String value = it.getValue().toString();
				if(StringUtil.checkString(value))
				{
					if(NumberUtil.isNumber(value))
					{
						where.append(" AND b."+it.getKey()+ " = "+ value );
					}
					else
					{
						where.append(" AND b."+it.getKey()+ " like '%"  +value+  "%'   " );
					}
				}
				
			}
		}
	}
//	xử lý câu lệnh query special ( không phức tạp)
	public static void querySpecial(Map<String,Object> params,List<String> typeCode,StringBuilder where)
	{
		String staffId= (String)params.get("staffId");
		if(StringUtil.checkString(staffId))
		{
			where.append(" and assignmentbuilding.staffid =  "+staffId + " ");
		}
		
//		lấy dữ liệu  area  từ area
		String rentAreaTo = (String)params.get("areaTo");
		String rentAreaFrom = (String)params.get("areaFrom");
		if(   StringUtil.checkString(rentAreaFrom )  || StringUtil.checkString(rentAreaTo)  )
		{
			if(StringUtil.checkString(rentAreaFrom))
			{
				where.append(" and b.rentprice >= "+rentAreaFrom + " ");
			}
			if(StringUtil.checkString(rentAreaTo))
			{
				where.append(" and b.rentprice <= "+rentAreaTo + " ");
			}
		}
		
//		lấy dữ liệu  rentPrice từ building
		String rentPriceTo = (String)params.get("rentPriceTo");
		String rentPriceFrom = (String)params.get("rentPriceFrom");
		if(   StringUtil.checkString(rentPriceFrom )  || StringUtil.checkString(rentPriceTo)  )
		{
			if(StringUtil.checkString(rentPriceFrom))
			{
				where.append(" and rentarea.value >= "+rentPriceFrom + " ");
			}
			if(StringUtil.checkString(rentPriceTo))
			{
				where.append(" and rentarea.value <= "+rentPriceTo + " ");
			}
		}
		
//		xử lý dữ liệu khi lấy type Code
		if(typeCode!=null && typeCode.size()!=0)
		{
			
//			java 7
			
			List<String> code = new ArrayList<String>();
			for(String item :typeCode)
			{
				code.add(" '"+item + "'  ");
			}
			
			
			where.append("and renttype.code in("+String.join(",",code) +")   ");
		}
		
	}
	
	@Override
	public List<BuildingEntity> findAll(Map<String,Object> params,List<String> typeCode) {
		System.out.println("connecting with database");
		
		StringBuilder sql = new StringBuilder("SELECT b.id,b.name,b.districtid, b.street, b.ward, b.numberofbasement,b.floorarea,b.rentprice,b.managername,"
				+ "b.managerphonenumber,b.servicefee,b.brokeragefee "
				+ " FROM building b  ");
		
		joinTable(params, typeCode, sql);
		
		StringBuilder where = new StringBuilder(" Where 1=1   ");
		queryNomal(params, where);
		querySpecial(params, typeCode, where);
		where.append(" group by b.id");
		sql.append(where);
		
		System.out.println(sql);
		List<BuildingEntity> kq = new ArrayList<BuildingEntity>();
		
		try(Connection conn = DriverManager.getConnection(BD_URL, USER, PASS); 
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
		    )
		{
			while(rs.next())
			{
				BuildingEntity building = new BuildingEntity();
				
				building.setId(rs.getLong("b.id"));
				building.setName(rs.getString("b.name"));
				building.setWard(rs.getString("b.ward"));
				building.setNumberOfbasement(rs.getInt("b.numberOfBasement"));
				building.setDistrictid(rs.getLong("b.districtid"));
				building.setStreet(rs.getString("b.street"));
				building.setFloorArea(rs.getLong("b.floorarea"));
				building.setRentPrice(rs.getLong("b.rentprice"));
				building.setServiceFee(rs.getString("b.servicefee"));
				building.setBrokerageFee(rs.getLong("b.brokeragefee"));
				building.setManagerName(rs.getString("b.managername"));
				building.setManagerPhoneNumber("b.managerphonenumber");				
				
				kq.add(building);
				
			}
			
			System.out.println("Connected is successfully");
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("The connect is faild");
		}
		return kq;
	}

}
