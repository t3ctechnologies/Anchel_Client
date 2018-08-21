package com.t3c.anchel.client.model.dashboard;

import com.t3c.anchel.client.model.IModel;

public class RecipientsDto implements IModel {

	private static final long serialVersionUID = 1L;

	protected String domain;
	private String firstName;
	private String lastName;
	private String mail;
	private String accountType;
	protected String uuid;
	private Boolean external;
	@Override
	public String toString() {
		return "RecipientsDto [domain=" + domain + ", firstName=" + firstName + ", lastName=" + lastName + ", mail="
				+ mail + ", accountType=" + accountType + ", uuid=" + uuid + ", external=" + external + "]";
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
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Boolean getExternal() {
		return external;
	}
	public void setExternal(Boolean external) {
		this.external = external;
	}
}
