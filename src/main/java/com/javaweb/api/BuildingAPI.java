package com.javaweb.api;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.Model.BuildingDTO;
import com.javaweb.service.BuildingService;

import customexception.FieldRequiredException;

@RestController
public class BuildingAPI {
	
	@Autowired
	private BuildingService buildingService;
	
	@GetMapping(value="/api/building")
	public List<BuildingDTO>  getBuilding(@RequestParam Map<String,Object> params,
										  @RequestParam(name = "typeCode", required = false) List<String> typeCode
			)
	{
		List<BuildingDTO> kq = buildingService.findAll(params,typeCode); 
		return kq;

	}
	

	public void valiDate(BuildingDTO buildingDTO) 
	{
		if(buildingDTO.getName()==null || buildingDTO.getName().equals("") || buildingDTO.getNumberOfbasement()==null )
		{
			throw new FieldRequiredException("name or numberOfBasement is null");
		}
	}
	
	
	@DeleteMapping(value="/api/building/{id}/{name}")
	public void delete(@PathVariable Integer id, 
						@PathVariable String name,
						@RequestParam (value = "ward", required = false) String ward)
	{
		System.out.println("đã xóa tòa "+ name + " nhà có : "+id+" xong ở : "+ward);
	}
}
