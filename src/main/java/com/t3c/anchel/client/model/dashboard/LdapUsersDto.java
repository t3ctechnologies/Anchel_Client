package com.t3c.anchel.client.model.dashboard;

import com.t3c.anchel.client.model.IModel;

public class LdapUsersDto implements IModel {

	private static final long serialVersionUID = 1L;

	private String type;
	private String identifier;
	private String display;
	protected String domain;
	private String firstName;
	private String lastName;
	private String mail;

	@Override
	public String toString() {
		return "LdapUsersDto [type=" + type + ", identifier=" + identifier + ", display=" + display + ", domain="
				+ domain + ", firstName=" + firstName + ", lastName=" + lastName + ", mail=" + mail + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
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
}
