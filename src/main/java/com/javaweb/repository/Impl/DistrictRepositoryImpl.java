package com.javaweb.repository.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.DistrictEntity;
@Repository
public class DistrictRepositoryImpl implements DistrictRepository{

	static final String BD_URL = "jdbc:mysql://localhost:3306/estatebasic";
	static final String USER = "root";
	static final String PASS = "";
	
	@Override
	public DistrictEntity findNameById(Long id) {
		DistrictEntity districtEntities = new DistrictEntity();
		StringBuilder sql = new StringBuilder( "select d.name from district d where d.id =  "+id+" ");
		try(Connection conn = DriverManager.getConnection(BD_URL, USER, PASS); 
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());
			    )
		{
			while(rs.next())
			{
				
				districtEntities.setName(rs.getString("name"));
				
			}
		}
		catch (SQLException e) {
			System.out.println("lỗi ko kết mối đc vs bảng district");
		}
		return districtEntities;
	}

}
