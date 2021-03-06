package com.t3c.anchel.client.utils.consts;

public class ApplicationConstants {
	// Production or Development
	// Production - ""
	// Development - "/com/t3c/anchel/client/utils/images"
	private static final String DEV_PROD = "";
	// private static final String DEV_PROD ="/com/t3c/anchel/client/utils/images";

	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";

	// API's
	public static final String LOGIN = "/linshare/webservice/rest/user/v2/authentication/authorized";
	public static final String MY_FILE = "/linshare/webservice/rest/user/v2/documents";
	public static final String RECEIVED_FILE = "/linshare/webservice/rest/user/v2/received_shares";
	public static final String UPLOAD_FILE = "/linshare/webservice/rest/user/v2/flow.json";
	public static final String WORK_GROUP = "/linshare/webservice/rest/user/v2/work_groups";
	public static final String FUNCTIONALITIES = "/linshare/webservice/rest/user/v2/functionalities";
	public static final String LIST_EMAIL_IDS = "/linshare/webservice/rest/user/v2/autocomplete/*.*?type=SHARING";
	public static final String SHARE_FILE = "/linshare/webservice/rest/user/v2/shares";
	
	// Images
	public static final String ICON_IMG = DEV_PROD + "/icon/favicon.png";
	public static final String LOGIN_IMAGE1 = DEV_PROD + "/login/logo.png";
	public static final String UPLOAD_IMG = DEV_PROD + "/dashboard/uploads.png";
	public static final String DOWNLOAD_IMG = DEV_PROD + "/dashboard/download.png";
	public static final String DELETE_IMG = DEV_PROD + "/dashboard/delete.png";
	public static final String RENAME_IMG = DEV_PROD + "/dashboard/rename.png";
	public static final String LOGOUT_IMG = DEV_PROD + "/dashboard/logot.jpg"; 
	public static final String REFRESH_IMG = DEV_PROD + "/dashboard/refresh.png";
	public static final String ADD_IMG = DEV_PROD + "/dashboard/add.png";
	public static final String LOGO1_IMG = DEV_PROD + "/about/icon-01.png";
	public static final String INFO_IMG = DEV_PROD + "/dashboard/info.png";
	public static final String SHARE_IMG = DEV_PROD + "/dashboard/share.png";

	public static String getSuccess() {
		return SUCCESS;
	}

	public static String getFailure() {
		return FAILURE;
	}
}
