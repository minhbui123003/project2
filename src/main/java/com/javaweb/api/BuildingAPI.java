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
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.service.BuildingService;

import customexception.FieldRequiredException;
@Transactional
@RestController
@PropertySource("classpath:application.properties")
public class BuildingAPI {
	
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
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
	

	
	@PersistenceContext
	private EntityManager entityManager;

	
	@GetMapping(value="/api/building/{managerName}/{name}")
	public List<BuildingDTO>  getBuildingByName(@PathVariable String managerName,
												@PathVariable String name	)
	{
		BuildingDTO kq  = new BuildingDTO();
		
		List<BuildingEntity> buildingEntities = buildingRepository.findByManagerNameContainingAndNameContaining(managerName, name);
		
		return null;

	}
	
	
//	sửa thành thêm có sử dụng save của data-jpa
	@PostMapping(value="/api/building/")
	public void createBuilding(@RequestBody BuildingResquestDTO buildingResquestDTO)
	{
		BuildingEntity buildingEntity = new BuildingEntity();
		buildingEntity.setName(buildingResquestDTO.getName());
		buildingEntity.setStreet(buildingResquestDTO.getStreet());
		buildingEntity.setWard(buildingResquestDTO.getWard());
		
		DistrictEntity districtEntity = new DistrictEntity();
		districtEntity.setId(buildingResquestDTO.getDistrictId());
		
		buildingEntity.setDistrict(districtEntity);
//		entityManager.persist(buildingEntity);
		buildingRepository.save(buildingEntity);
		System.out.println("thêm xong");
	}
	

//	sửa thành cập nhật sử dụng datta -jpa
	@PutMapping(value="/api/building/")
	public void updateBuilding(@RequestBody BuildingResquestDTO buildingResquestDTO)
	{
		BuildingEntity buildingEntity = buildingRepository.findById(buildingResquestDTO.getId()).get();
//		buildingEntity.setId(1L);
		buildingEntity.setName(buildingResquestDTO.getName());
		buildingEntity.setStreet(buildingResquestDTO.getStreet());
		buildingEntity.setWard(buildingResquestDTO.getWard());
		
		DistrictEntity districtEntity = new DistrictEntity();
		districtEntity.setId(buildingResquestDTO.getDistrictId());
		
		buildingEntity.setDistrict(districtEntity);
//		entityManager.merge(buildingEntity);
		buildingRepository.save(buildingEntity);
		System.out.println("sửa  xong");
	}
	

	
//	xóa 1 list
	@DeleteMapping(value="/api/building/{ids}")
	public void deleteList(@PathVariable Long[] ids)
	{
		buildingRepository.deleteByIdIn(ids);
		System.out.println("xóa xong");
	}
}
