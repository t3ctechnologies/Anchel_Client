package com.t3c.anchel.client.utils.enums;

public enum ACStatusEnum
{
	SUCCESS("SUCCESS"), FAILURE("FAILURE");

	private String status;

	private ACStatusEnum(String status)
	{
		this.status = status;
	}

	public String getStatusValue()
	{
		return status;
	}

}
