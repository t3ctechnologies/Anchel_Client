package com.t3c.anchel.client.model.dashboard;

import com.t3c.anchel.client.model.IModel;

public class AuthorDetailsDTO implements IModel{

	private static final long serialVersionUID = 1L;
	
	protected String firstName;
	protected String lastName;
	protected String name;
	protected String mail;
	protected String uuid;
	protected String accountType;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	@Override
	public String toString() {
		return "AuthorDetailsDTO [firstName=" + firstName + ", lastName=" + lastName + ", name=" + name + ", mail="
				+ mail + ", uuid=" + uuid + ", accountType=" + accountType + "]";
	}
}
