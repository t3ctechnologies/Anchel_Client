package com.t3c.anchel.client.model.dashboard;

import java.util.Date;

import com.t3c.anchel.client.model.IModel;

public class SharedDetailsDto implements IModel {

	private static final long serialVersionUID = 1L;

	private String uuid;
	private String name;
	private Date creationDate;
	private Date modificationDate;
	private Date expirationDate;
	private int downloaded;
	private FileDetailsDTO document;
	private RecipientsDto recipient;
	private String description;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}

	public FileDetailsDTO getDocument() {
		return document;
	}

	public void setDocument(FileDetailsDTO document) {
		this.document = document;
	}

	public RecipientsDto getRecipient() {
		return recipient;
	}

	public void setRecipient(RecipientsDto recipient) {
		this.recipient = recipient;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "SharedDetailsDto [uuid=" + uuid + ", name=" + name + ", creationDate=" + creationDate
				+ ", modificationDate=" + modificationDate + ", expirationDate=" + expirationDate + ", downloaded="
				+ downloaded + ", document=" + document + ", recipient=" + recipient + ", description=" + description
				+ "]";
	}
}
