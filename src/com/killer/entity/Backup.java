package com.killer.entity;

import java.io.Serializable;

public class Backup implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6030459228380007361L;
	private int contactId;
	private String contactName;
	private String contactSize;
	private String contactTime;
	public Backup() {
		super();
		// TODO 自动生成的构造函数存根
	}
	



	public Backup(int contactId, String contactName, String contactSize,
			String contactTime) {
		super();
		this.contactId = contactId;
		this.contactName = contactName;
		this.contactSize = contactSize;
		this.contactTime = contactTime;
	}




	public int getContactId() {
		return contactId;
	}




	public void setContactId(int contactId) {
		this.contactId = contactId;
	}




	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactSize() {
		return contactSize;
	}
	public void setContactSize(String contactSize) {
		this.contactSize = contactSize;
	}
	public String getContactTime() {
		return contactTime;
	}
	public void setContactTime(String contactTime) {
		this.contactTime = contactTime;
	}
	

}
