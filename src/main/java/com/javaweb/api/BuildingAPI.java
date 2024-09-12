package com.javaweb.api;



import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.Model.BuildingDTO;
import com.javaweb.Model.BuildingResquestDTO;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
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
	@PersistenceContext
	private EntityManager entityManager;
	@Transactional
	
	@PostMapping(value="/api/building")
	public void createBuilding(@RequestBody BuildingResquestDTO buildingResquestDTO)
	{
		BuildingEntity buildingEntity = new BuildingEntity();
		buildingEntity.setName(buildingResquestDTO.getName());
		buildingEntity.setStreet(buildingResquestDTO.getStreet());
		buildingEntity.setWard(buildingResquestDTO.getWard());
		
		DistrictEntity districtEntity = new DistrictEntity();
		districtEntity.setId(buildingResquestDTO.getDistrictId());
		
		buildingEntity.setDistrict(districtEntity);
		entityManager.persist(buildingEntity);
		System.out.println("thêm xong");
	}
	
	@Transactional
	@PutMapping(value="/api/building")
	public void updateBuilding(@RequestBody BuildingResquestDTO buildingResquestDTO)
	{
		BuildingEntity buildingEntity = new BuildingEntity();
		buildingEntity.setId(1L);
		buildingEntity.setName(buildingResquestDTO.getName());
		buildingEntity.setStreet(buildingResquestDTO.getStreet());
		buildingEntity.setWard(buildingResquestDTO.getWard());
		
		DistrictEntity districtEntity = new DistrictEntity();
		districtEntity.setId(buildingResquestDTO.getDistrictId());
		
		buildingEntity.setDistrict(districtEntity);
		entityManager.merge(buildingEntity);
		System.out.println("sửa  xong");
	}
	
	@DeleteMapping(value="/api/building/{id}")
	public void delete(@PathVariable Long id)
	{
		BuildingEntity buildingEntity = entityManager.find(BuildingEntity.class, id);
		entityManager.remove(buildingEntity);
		System.out.println("xóa xong");
	}
}
