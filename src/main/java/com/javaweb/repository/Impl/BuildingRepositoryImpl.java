package com.javaweb.repository.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
	public static void joinTable(Map<String,Object> params,List<String> typeCode, StringBuilder join)
	{
		String staffId = (String)params.get("staffId");
		if( StringUtil.checkString(staffId) )
		{
			join.append(" inner join assignmentbuilding on assignmentbuilding.buildingid = b.id   ");
		}
		
//		join bảng renttype
		if(typeCode!=null && typeCode.size()!=0)
		{
			join.append(" inner join buildingrenttype on buildingrenttype.buildingid = b.id   ");
			join.append(" inner join renttype  on renttype.id = buildingrenttype.renttypeid  ");
		}
//		join bảng rentarea
//		String rentAreaFrom = (String)params.get("areaFrom");
//		String rentAreaTo = (String)params.get("areaTo");
//		if( StringUtil.checkString(rentAreaTo) || StringUtil.checkString(rentAreaFrom))
//		{
//			join.append(" inner join rentarea on rentarea.buildingid = b.id ");
//		}
		
	}
	
//	xử lý câu lệnh query nomal ( không phức tạp)
	public static void queryNomal(Map<String,Object> params, StringBuilder where)
	{
		for (Map.Entry<String, Object> item : params.entrySet()) 
		{
			if( !item.getKey().equals("staffId") && !item.getKey().equals("typeCode") 
					&& !item.getKey().startsWith("area") && !item.getKey().startsWith("rentPrice") )
			{
				String value = item.getValue().toString();
				
				if( StringUtil.checkString(value))
				{
					if(NumberUtil.isNumber(value) )
					{
						where.append(" and  b."+item.getKey()+" = "+ item.getValue() +"  ");
					}
					else
					{
						where.append(" and  b."+item.getKey()+" like '%"+ item.getValue() +"%'  ");

					}
				}
				
			}
		}
	}
//	xử lý câu lệnh query special ( không phức tạp)
	public static void querySpecial(Map<String,Object> params,List<String> typeCode,StringBuilder where)
	{

//		xử lý câu lệnh lấy dữ liệu từ 
		
		String staffId = (String)params.get("staffId");
		if(StringUtil.checkString(staffId))
		{
			where.append(" and assignmentbuilding.staffid =  "+staffId +" ");
		}
		
		
//		xử lý dữ liệu   area  từ rentarea
		String rentAreaTo = (String)params.get("areaTo");
		String rentAreaFrom = (String)params.get("areaFrom");
		if(   StringUtil.checkString(rentAreaFrom )  || StringUtil.checkString(rentAreaTo)  )
		{
			where.append(" and exists (select * from rentarea r where b.id = r.buildingid    ");
			if(StringUtil.checkString(rentAreaFrom))
			{
				where.append(" and r.value >= "+rentAreaFrom + " ");
			}
			if(StringUtil.checkString(rentAreaTo))
			{
				where.append(" and r.value <= "+rentAreaTo + " ");
			}
			
			where.append(" ) ");
		}
		
//		xử lý dữ liệu   từ rentprice
		String rentPriceFrom = (String)params.get("rentPriceFrom");
		String rentPriceTo = (String)params.get("rentPriceTo");
		if(   StringUtil.checkString(rentPriceTo )  ||StringUtil.checkString(rentPriceFrom)  )
		{
			if(StringUtil.checkString(rentAreaFrom))
			{
				where.append(" and b.rentprice >= "+rentPriceFrom + " ");
			}
			if(StringUtil.checkString(rentPriceTo))
			{
				where.append(" and b.rentprice <= "+rentPriceTo + " ");
			}
		}
		
//		xử lý dữ liệu từ bảng rentType java7
		
//		if(typeCode!=null && typeCode.size()!=0 )
//		{
//			
//			List<String> listTypeCode = new ArrayList<String>();
//			
//			for(String item : typeCode)
//			{
//				listTypeCode.add(" '"+item+"' ");
//			}
//			
//			where.append(" and renttype.code in("+String.join(", ",listTypeCode)+") ");
//			
//		}
//		
		
//		java8
		if(typeCode!=null && typeCode.size()!=0 ) {
			where.append(" and  (");
			String sql1 =  typeCode.stream().map(it-> "renttype.code like '%"+it+"%' ").collect(Collectors.joining(" or "));
			where.append(sql1);
			where.append(" ) ");
		}
		
	}
	
	@Override
	public List<BuildingEntity> findAll(Map<String,Object> params,List<String> typeCode) {
		System.out.println("connecting with database");
		
		StringBuilder sql = new StringBuilder("select b.id , b.name , b.ward, b.street,b.numberofbasement,"
				+ " b.districtid, b.managername , b.managerphonenumber,b.rentprice , b.servicefee,"
				+ " b.brokeragefee, b.floorarea "
				+ " from building b ");
		
		joinTable(params, typeCode, sql);
		
		StringBuilder where = new StringBuilder(" Where 1=1   ");
		queryNomal(params, where);
		querySpecial(params, typeCode, where);
		where.append(" group by b.id ");
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
				

				building.setName(rs.getString("b.name"));
				building.setNumberOfbasement(rs.getInt("b.numberofbasement"));
				building.setStreet(rs.getString("b.street"));
				building.setWard(rs.getString("b.ward"));
				building.setId(rs.getLong("b.id"));
				building.setDistrictid(rs.getLong("b.districtid"));
				building.setManagerName(rs.getString("b.managername"));
				building.setManagerPhoneNumber( rs.getString("b.managerphonenumber"));
				building.setFloorArea(rs.getLong("b.floorarea") );
				building.setRentPrice(rs.getLong("b.rentprice"));
				building.setServiceFee(rs.getString("b.servicefee"));
				building.setBrokerageFee(rs.getLong("b.brokeragefee"));			
				
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
