package com.t3c.anchel.client.model.dashboard;

import java.util.Date;

import com.t3c.anchel.client.model.IModel;

public class JTableDto implements IModel{
	private static final long serialVersionUID = 1L;

	private String uuid;
	private String name;
	private String modificationDate;
	private String type;
	private String size;
	private String workgroupId;
	@Override
	public String toString() {
		return "JTableDto [uuid=" + uuid + ", name=" + name + ", modificationDate=" + modificationDate + ", type="
				+ type + ", size=" + size + ", workgroupId=" + workgroupId + "]";
	}
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
	public String getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getWorkgroupId() {
		return workgroupId;
	}
	public void setWorkgroupId(String workgroupId) {
		this.workgroupId = workgroupId;
	}
}
