package com.javaweb.repository.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "building")

public class BuildingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@Column(name="districtid")
//	private Long districtId;
	
	@Column(name="name")
	private String name ;
	
	@Column(name="managername")
	private String managerName ;
	
	@Column(name="managerphonenumber")
	private String managerPhoneNumber ;
	
	@Column(name="floorarea")
	private Long floorArea;
	


	@Column(name="rentprice")
	private Long rentPrice;
	
	@Column(name="servicefee")
	private String serviceFee;
	
	@Column(name="brokeragefee")
	private Long brokerageFee;
	
	@Column(name="numberofbasement")
	private Integer  numberOfbasement;
	
	@Column(name="ward")
	private String ward ;
	
	@Column(name="street")
	private String street ;
	
	@ManyToOne
	@JoinColumn(name = "districtid")
	private DistrictEntity district;
	
//	kết nối 1-n vs bảng rentarea
	@OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
	private List<RentAreaEntity> rentAreas = new ArrayList<RentAreaEntity>();
	
	
	
	
	

	
	public List<RentAreaEntity> getRentAreas() {
		return rentAreas;
	}
	public void setRentAreas(List<RentAreaEntity> rentAreas) {
		this.rentAreas = rentAreas;
	}
	public DistrictEntity getDistrict() {
		return district;
	}
	public void setDistrict(DistrictEntity district) {
		this.district = district;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


//	public Long getDistrictId() {
//		return districtId;
//	}
//	public void setDistrictId(Long districtId) {
//		this.districtId = districtId;
//	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getManagerPhoneNumber() {
		return managerPhoneNumber;
	}
	public void setManagerPhoneNumber(String managerPhoneNumber) {
		this.managerPhoneNumber = managerPhoneNumber;
	}
	public Long getFloorArea() {
		return floorArea;
	}
	public void setFloorArea(Long floorArea) {
		this.floorArea = floorArea;
	}

	public Long getRentPrice() {
		return rentPrice;
	}
	public void setRentPrice(Long rentPrice) {
		this.rentPrice = rentPrice;
	}
	public String getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}
	public Long getBrokerageFee() {
		return brokerageFee;
	}
	public void setBrokerageFee(Long brokeraveFee) {
		this.brokerageFee = brokeraveFee;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNumberOfbasement() {
		return numberOfbasement;
	}
	public void setNumberOfbasement(Integer numberOfbasement) {
		this.numberOfbasement = numberOfbasement;
	}
	public String getWard() {
		return ward;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	
}
