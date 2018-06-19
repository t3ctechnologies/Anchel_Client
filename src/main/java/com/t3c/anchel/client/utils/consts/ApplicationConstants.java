package com.t3c.anchel.client.utils.consts;

public class ApplicationConstants
{
	private static final String	SUCCESS	= "SUCCESS";
	private static final String	FAILURE	= "FAILURE";

	// API's
	public static final String	LOGIN	= "/linshare/webservice/rest/user/v2/authentication/authorized";
	public static final String	MY_FILE	= "/linshare/webservice/rest/user/v2/documents";
	public static final String RECEIVED_FILE	= "/linshare/webservice/rest/user/v2/received_shares";
	public static final String UPLOAD_FILE	= "/linshare/webservice/rest/user/v2/flow.json";
	public static final String WORK_GROUP	= "/linshare/webservice/rest/user/v2/work_groups";
	
	public static String getSuccess()
	{
		return SUCCESS;
	}

	public static String getFailure()
	{
		return FAILURE;
	}
}
