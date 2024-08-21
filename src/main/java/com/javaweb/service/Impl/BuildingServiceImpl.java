package com.javaweb.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.Model.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl  implements BuildingService{
	@Autowired

	private BuildingRepository buildingRepository;
	@Autowired
	private DistrictRepository districtRepository;
	
	@Override
	public List<BuildingDTO> findAll(Map<String,Object> params,List<String> typeCode) {
		
		
		List<BuildingEntity> buildingEntities = buildingRepository.findAll( params,typeCode) ;
		
		List<BuildingDTO> result = new ArrayList<BuildingDTO>();
		for(BuildingEntity item:buildingEntities)
		{
			BuildingDTO building = new BuildingDTO();
			
			building.setName(item.getName());
			DistrictEntity districtEntity = districtRepository.findNameById(item.getDistrictid());
			building.setNumberOfbasement(item.getNumberOfbasement());
			building.setAddress(item.getStreet() +" ,"+ item.getWard()+districtEntity.getName());
			building.setManagerName(item.getManagerName());
			building.setManagerPhoneNumber(item.getManagerPhoneNumber());
			building.setFloorArea(item.getFloorArea());
			building.setEmptyArea(item.getEmptyArea());
			building.setServiceFee(item.getServiceFee());
			building.setBrokerageFee(item.getBrokerageFee());
			
			result.add(building);
		}

		
		
		return result;
	}

}
