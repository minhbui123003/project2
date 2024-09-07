package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.javaweb.Model.BuildingDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;

@Component
public class BuildingDTOConverter {
//	@Autowired
//	private DistrictRepository districtRepository;
//	
//	@Autowired
//	private RentAreaRepository rentAreaRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public BuildingDTO toBuildingDTO(BuildingEntity item)
	{
		BuildingDTO building = modelMapper.map(item, BuildingDTO.class) ;
		
//		DistrictEntity district = item.getDistrict();
		building.setAddress(item.getStreet()+ " , "+ item.getWard() +" " +item.getDistrict().getName());

		
		List<RentAreaEntity> rentAreas = item.getRentAreas();
		String listRentArea = rentAreas.stream().map(value -> value.getValue().toString() ).collect(Collectors.joining(","));
		building.setRentArea(listRentArea);
		
		
//		building.setName(item.getName());
//		building.setNumberOfbasement(item.getNumberOfbasement());
//		building.setManagerName(item.getManagerName());
//		building.setManagerPhoneNumber(item.getManagerPhoneNumber());
//		building.setRentArea(listRentArea);
//		building.setFloorArea(item.getFloorArea());
//		building.setEmptyArea(item.getEmptyArea());
//		building.setServiceFee(item.getServiceFee());
//		building.setBrokerageFee(item.getBrokerageFee());
		
		
		
		return building;
	}
	
}
