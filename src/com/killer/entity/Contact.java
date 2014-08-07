package com.killer.entity;

import java.io.Serializable;

public class Contact implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String img;
	private String name;
	private String mobile;
	private String home;
	private String office;
	private String email;
	private String addr;
	



	public Contact(Integer id, String img, String name, String mobile,
			String home, String office, String email, String addr) {
		super();
		this.id = id;
		this.img = img;
		this.name = name;
		this.mobile = mobile;
		this.home = home;
		this.office = office;
		this.email = email;
		this.addr = addr;
	}
	public Contact() {
		super();
		// TODO 自动生成的构造函数存根
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
