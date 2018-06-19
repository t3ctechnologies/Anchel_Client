package com.t3c.anchel.client.model.dashboard;

import java.util.Date;

import com.t3c.anchel.client.model.IModel;

public class WorkGroupDTO implements IModel {
	private static final long serialVersionUID = 1L;

	protected String name;
	protected String uuid;
	protected Date creationDate;
	protected Date modificationDate;
	protected String locale;
	protected String externalMailLocale;
	protected String domain;

	// WorkgroupFolders fields

	protected String type;
	protected String parent;
	protected String workGroup;
	protected String description;
	protected String metaData;

	protected AuthorDetailsDTO lastAuthor;

	// FileDetails Fields

	private Long size;
	protected String mimeType;
	protected String sha256sum;
	private boolean hasThumbnail;
	private Date uploadDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getExternalMailLocale() {
		return externalMailLocale;
	}
	public void setExternalMailLocale(String externalMailLocale) {
		this.externalMailLocale = externalMailLocale;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getWorkGroup() {
		return workGroup;
	}
	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMetaData() {
		return metaData;
	}
	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}
	public AuthorDetailsDTO getLastAuthor() {
		return lastAuthor;
	}
	public void setLastAuthor(AuthorDetailsDTO lastAuthor) {
		this.lastAuthor = lastAuthor;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
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
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	@Override
	public String toString() {
		return "WorkGroupDTO [name=" + name + ", uuid=" + uuid + ", creationDate=" + creationDate
				+ ", modificationDate=" + modificationDate + ", locale=" + locale + ", externalMailLocale="
				+ externalMailLocale + ", domain=" + domain + ", type=" + type + ", parent=" + parent + ", workGroup="
				+ workGroup + ", description=" + description + ", metaData=" + metaData + ", lastAuthor=" + lastAuthor
				+ ", size=" + size + ", mimeType=" + mimeType + ", sha256sum=" + sha256sum + ", hasThumbnail="
				+ hasThumbnail + ", uploadDate=" + uploadDate + "]";
	}
}
