package com.javaweb.api;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.Model.BuildingDTO;
import com.javaweb.service.BuildingService;

import customexception.FieldRequiredException;

@RestController
@PropertySource("classpath:application.properties")
public class BuildingAPI {
	
	@Autowired
	private BuildingService buildingService;
	
	@Value("${minh.bui}")
	private String data;
	
	
	@GetMapping(value="/api/building")
	public List<BuildingDTO>  getBuilding(@RequestParam Map<String,Object> params,
										  @RequestParam(name = "typeCode", required = false) List<String> typeCode
			)
	{
		List<BuildingDTO> kq = buildingService.findAll(params,typeCode); 
		return kq;

	}
	

//	public void valiDate(BuildingDTO buildingDTO) 
//	{
//		if(buildingDTO.getName()==null || buildingDTO.getName().equals("") || buildingDTO.getNumberOfbasement()==null )
//		{
//			throw new FieldRequiredException("name or numberOfBasement is null");
//		}
//	}
//	
	
	@DeleteMapping(value="/api/building/{id}")
	public void delete(@PathVariable Integer id)
	{
		System.out.println(data);
	}
}
