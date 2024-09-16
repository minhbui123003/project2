package com.javaweb.repository.Impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.NumberDifferent0;


@Repository
//@PropertySource("classpath:application.properties")
@Primary
public class JDBCBuildingRepositoryImpl implements BuildingRepository{

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${spring.datasource.url}")
	private  String BD_URL;
	@Value("${spring.datasource.username}")
	private  String USER;
	@Value("${spring.datasource.password}")
	private String  PASS ;
	
	
//	hàm xu ly join các bảng
	public static void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder join)
	{
		Long staffId = buildingSearchBuilder.getStaffId();
		if( NumberDifferent0.checkNumberKhac0(staffId) )
		{
			join.append(" inner join assignmentbuilding on assignmentbuilding.buildingid = b.id   ");
		}
		
//		join bảng renttype
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
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
	public static void queryNomal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where)
	{
//		for (Map.Entry<String, Object> item : params.entrySet()) 
//		{
//			if( !item.getKey().equals("staffId") && !item.getKey().equals("typeCode") 
//					&& !item.getKey().startsWith("area") && !item.getKey().startsWith("rentPrice") )
//			{
//				String value = item.getValue().toString();
//				
//				if( StringUtil.checkString(value))
//				{
//					if(NumberUtil.isNumber(value) )
//					{
//						where.append(" and  b."+item.getKey()+" = "+ item.getValue() +"  ");
//					}
//					else
//					{
//						where.append(" and  b."+item.getKey()+" like '%"+ item.getValue() +"%'  ");
//
//					}
//				}
//				
//			}
//		}
		
		try {
			Field[] fields= BuildingSearchBuilder.class.getDeclaredFields();
			for(Field item : fields) {
				item.setAccessible(true);
				String fieldName = item.getName();
				if(!fieldName.equals("staffId") && !fieldName.equals("typeCode") 
					&& !fieldName.startsWith("area") &&!fieldName.startsWith("rentPrice"))
				{
					Object value = item.get(buildingSearchBuilder);
					if( value!=null)
						{
							if(item.getType().getName().equals("java.lang.Long")||item.getType().getName().equals("java.lang.Integer")
									||item.getType().getName().equals("java.lang.Float") )
							{
								
								where.append(" and  b."+fieldName+" = "+ value +"  ");
							}
							else if(item.getType().getName().equals("java.lang.String"))
							{
								where.append(" and  b."+fieldName+" like '%"+ value +"%'  ");
		
							}
						}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
//	xử lý câu lệnh query special ( không phức tạp)
	public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder,StringBuilder where)
	{

//		xử lý câu lệnh lấy dữ liệu từ 
		
		Long staffId = buildingSearchBuilder.getStaffId();
		if( NumberDifferent0.checkNumberKhac0(staffId) )
		{
			where.append(" and assignmentbuilding.staffid =  "+staffId +" ");
		}
		
		
//		xử lý dữ liệu   area  từ rentarea
		Long rentAreaTo = buildingSearchBuilder.getAreaTo();
		Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();
		if(   NumberDifferent0.checkNumberKhac0(rentAreaFrom)  || NumberDifferent0.checkNumberKhac0(rentAreaTo)   )
		{
			where.append(" and exists (select * from rentarea r where b.id = r.buildingid    ");
			if(NumberDifferent0.checkNumberKhac0(rentAreaFrom))
			{
				where.append(" and r.value >= "+rentAreaFrom + " ");
			}
			if( NumberDifferent0.checkNumberKhac0(rentAreaTo) )
			{
				where.append(" and r.value <= "+rentAreaTo + " ");
			}
			
			where.append(" ) ");
		}
		
//		xử lý dữ liệu   từ rentprice
		Long rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
		Long rentPriceTo = buildingSearchBuilder.getRentPriceTo();
		if(   NumberDifferent0.checkNumberKhac0(rentPriceTo )  ||NumberDifferent0.checkNumberKhac0(rentPriceFrom)  )
		{
			if(NumberDifferent0.checkNumberKhac0(rentPriceFrom))
			{
				where.append(" and b.rentprice >= "+rentPriceFrom + " ");
			}
			if(NumberDifferent0.checkNumberKhac0(rentPriceTo))
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
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		if(typeCode!=null && typeCode.size()!=0 ) {
			where.append(" and  (");
			String sql1 =  typeCode.stream().map(it-> "renttype.code like '%"+it+"%' ").collect(Collectors.joining(" or "));
			where.append(sql1);
			where.append(" ) ");
		}
		
	}
	
	@Override
	public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder) {
		System.out.println("connecting with database");
		
		StringBuilder sql = new StringBuilder("select b.id , b.name , b.ward, b.street,b.numberofbasement,"
				+ " b.districtid, b.managername , b.managerphonenumber,b.rentprice , b.servicefee,"
				+ " b.brokeragefee, b.floorarea "
				+ " from building b ");
		
		joinTable(buildingSearchBuilder, sql);
		
		StringBuilder where = new StringBuilder(" Where 1=1   ");
		queryNomal(buildingSearchBuilder, where);
		querySpecial(buildingSearchBuilder, where);
		where.append(" group by b.id ");
		sql.append(where);
		
		System.out.println(sql);
//		List<BuildingEntity> kq = new ArrayList<BuildingEntity>();
//		
//		try(Connection conn = DriverManager.getConnection(BD_URL, USER, PASS); 
//			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery(sql.toString());
//		    )
//		{
//			while(rs.next())
//			{
//				BuildingEntity building = new BuildingEntity();
//				
//
//				building.setName(rs.getString("b.name"));
//				building.setNumberOfbasement(rs.getInt("b.numberofbasement"));
//				building.setStreet(rs.getString("b.street"));
//				building.setWard(rs.getString("b.ward"));
//				building.setId(rs.getLong("b.id"));
////				building.setDistrictId(rs.getLong("b.districtid"));
//				building.setManagerName(rs.getString("b.managername"));
//				building.setManagerPhoneNumber( rs.getString("b.managerphonenumber"));
//				building.setFloorArea(rs.getLong("b.floorarea") );
//				building.setRentPrice(rs.getLong("b.rentprice"));
//				building.setServiceFee(rs.getString("b.servicefee"));
//				building.setBrokerageFee(rs.getLong("b.brokeragefee"));			
//				
//				kq.add(building);
//				
//			}
//			
//			System.out.println("Connected is successfully");
//		}
//		catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println("The connect is faild");
//		}
//		return kq;
		Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
		return query.getResultList();
	}

}
