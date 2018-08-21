package com.t3c.anchel.client.model.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.t3c.anchel.client.model.IModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FunctionalityDto implements IModel {

	private static final long serialVersionUID = 1L;

	protected String type;
	protected String identifier;
	protected Boolean enable;
	protected Boolean canOverride;
	protected String value;
	protected String unit;
	protected List<String> units = new ArrayList<String>();
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
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	public Boolean getCanOverride() {
		return canOverride;
	}
	public void setCanOverride(Boolean canOverride) {
		this.canOverride = canOverride;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public List<String> getUnits() {
		return units;
	}
	public void setUnits(List<String> units) {
		this.units = units;
	}
	@Override
	public String toString() {
		return "FunctionalityDto [type=" + type + ", identifier=" + identifier + ", enable=" + enable + ", canOverride="
				+ canOverride + ", value=" + value + ", unit=" + unit + ", units=" + units + "]";
	}
}
