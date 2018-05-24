package com.t3c.anchel.client.model.dashboard;

import java.util.Date;

import com.t3c.anchel.client.model.IModel;

public class FileDetailsDTO implements IModel {
	private static final long serialVersionUID = 1L;

	private String uuid;
	private String name;
	private String description;
	private Date creationDate;
	private Date modificationDate;
	private Date expirationDate;
	private Boolean ciphered;
	private String type;
	private Long size;
	private String metaData;
	private String sha256sum;
	private boolean hasThumbnail;
	private Long shared;

	// Received files field
	private Long downloaded;
	private SenderDTO sender;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Boolean getCiphered() {
		return ciphered;
	}

	public void setCiphered(Boolean ciphered) {
		this.ciphered = ciphered;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	public String getSha256sum() {
		return sha256sum;
	}

	public void setSha256sum(String sha256sum) {
		this.sha256sum = sha256sum;
	}

	public boolean isHasThumbnail() {
		return hasThumbnail;
	}

	public void setHasThumbnail(boolean hasThumbnail) {
		this.hasThumbnail = hasThumbnail;
	}

	public Long getShared() {
		return shared;
	}

	public void setShared(Long shared) {
		this.shared = shared;
	}

	public Long getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(Long downloaded) {
		this.downloaded = downloaded;
	}

	public SenderDTO getSender() {
		return sender;
	}

	public void setSender(SenderDTO sender) {
		this.sender = sender;
	}

	@Override
	public String toString() {
		return "FileDetailsDTO [uuid=" + uuid + ", name=" + name + ", description=" + description + ", creationDate="
				+ creationDate + ", modificationDate=" + modificationDate + ", expirationDate=" + expirationDate
				+ ", ciphered=" + ciphered + ", type=" + type + ", size=" + size + ", metaData=" + metaData
				+ ", sha256sum=" + sha256sum + ", hasThumbnail=" + hasThumbnail + ", shared=" + shared + ", downloaded="
				+ downloaded + ", sender=" + sender + "]";
	}

}
