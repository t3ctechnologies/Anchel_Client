
package com.t3c.anchel.client.model.dashboard;

import java.util.Date;

import com.t3c.anchel.client.model.IModel;

public class SenderDTO implements IModel{
	private static final long serialVersionUID = 1L;
	
	private String				uuid;
	private Date				creationDate;
	private Date				modificationDate;
	private String				domain;
	private String				firstName;
	private String				lastName;
	private String				mail;
	private String				role;
	private String				accountType;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
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
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	@Override
	public String toString() {
		return "SenderDTO [uuid=" + uuid + ", creationDate=" + creationDate + ", modificationDate=" + modificationDate
				+ ", domain=" + domain + ", firstName=" + firstName + ", lastName=" + lastName + ", mail=" + mail
				+ ", role=" + role + ", accountType=" + accountType + "]";
	}
}
