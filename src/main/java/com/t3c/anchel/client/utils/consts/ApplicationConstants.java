package com.t3c.anchel.client.utils.consts;

public class ApplicationConstants
{
	private static final String	SUCCESS	= "SUCCESS";
	private static final String	FAILURE	= "FAILURE";

	// API's
	public static final String	LOGIN	= "/linshare/webservice/rest/user/v2/authentication/authorized";
	public static final String	MY_FILE	= "/linshare/webservice/rest/user/v2/documents";

	public static String getSuccess()
	{
		return SUCCESS;
	}

	public static String getFailure()
	{
		return FAILURE;
	}
}
