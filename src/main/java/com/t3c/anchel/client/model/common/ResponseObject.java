package com.t3c.anchel.client.model.common;

import com.t3c.anchel.client.model.IModel;

public class ResponseObject implements IModel
{
	private static final long	serialVersionUID	= 1L;

	String						status;
	String						internalMsg;
	String						errorCode;
	Object						responseObject;

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getInternalMsg()
	{
		return internalMsg;
	}

	public void setInternalMsg(String internalMsg)
	{
		this.internalMsg = internalMsg;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public Object getResponseObject()
	{
		return responseObject;
	}

	public void setResponseObject(Object responseObject)
	{
		this.responseObject = responseObject;
	}

	@Override
	public String toString()
	{
		return "ResponseObject [status=" + status + ", internalMsg=" + internalMsg + ", errorCode=" + errorCode + ", responseObject=" + responseObject + "]";
	}

}
