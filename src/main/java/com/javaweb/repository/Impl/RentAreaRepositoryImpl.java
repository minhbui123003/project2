package com.javaweb.repository.Impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.utils.ConnectionUtil;

//@Repository
public class RentAreaRepositoryImpl {//implements RentAreaRepository {

//	@Override
//	public List<RentAreaEntity> getvalueByBuildingId(Long id) {
//		
//		String sql = " select * from rentarea where rentarea.buildingid= "+id+"  ";
//		
//		List<RentAreaEntity> rentAreas = new ArrayList<RentAreaEntity>() ;
//		try(	Connection con = ConnectionUtil.getConnection();
//				Statement stmt = con.createStatement();
//				ResultSet rs = stmt.executeQuery(sql);
//			)
//		{
//			while(rs.next())
//			{
//				RentAreaEntity area =  new RentAreaEntity();
//				area.setValue(rs.getString("value"));
//				
//				rentAreas.add(area);
//			}
//		}
//		catch (SQLException e) 
//		{
//			
//		}
//		
//		
//		return rentAreas;
//	}

}
