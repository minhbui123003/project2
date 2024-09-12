package com.javaweb.repository.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name",nullable = false)
	private String userName;
	
	@Column(name = "code",nullable = false, unique = true)
	private String code;
	

//	thủ công
//	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
//	private List<UserRoleEntity> userRoleEntities = new ArrayList<UserRoleEntity>();
//	công nghệ
	
	@ManyToMany(mappedBy = "listRoleEntities",fetch = FetchType.LAZY)
	private List<UserEntity> listUserEntities = new ArrayList<UserEntity>();
	
	

	public List<UserEntity> getListUserEntities() {
		return listUserEntities;
	}

	public void setListUserEntities(List<UserEntity> listUserEntities) {
		this.listUserEntities = listUserEntities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
