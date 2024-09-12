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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username",nullable = false,unique = true)
	private String userName;
	
	@Column(name = "password",nullable = false)
	private String passWord;
	
	@Column(name = "fullname",nullable = false)
	private String fullName;
	
	@Column(name = "status",nullable = false)
	private Integer status; 
	
	@Column(name = "email",nullable = false)
	private String email;

//	thủ công
//	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//	private List<UserRoleEntity> userRolerEntities = new ArrayList<UserRoleEntity>();

//	công nghệ
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_role",
				joinColumns = @JoinColumn(name ="userid" ,nullable = false),
				inverseJoinColumns = @JoinColumn(name = "roleid",nullable = false))
	private List<RoleEntity> listRoleEntities = new ArrayList<RoleEntity>();
	
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

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<RoleEntity> getListRoleEntities() {
		return listRoleEntities;
	}

	public void setListRoleEntities(List<RoleEntity> listRoleEntities) {
		this.listRoleEntities = listRoleEntities;
	}

	
}
