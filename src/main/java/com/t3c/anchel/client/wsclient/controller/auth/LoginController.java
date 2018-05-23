package com.t3c.anchel.client.wsclient.controller.auth;

import org.apache.log4j.Logger;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.services.security.LoginService;

public class LoginController
{
	final static Logger OUT = Logger.getLogger(LoginController.class);

	public ResponseObject isAuthorised(String username, String password, String url)
	{
		OUT.debug("Authentication started with username :" + username + " URL :" + url);
		ResponseObject response = null;
		try
		{
			response = new ResponseObject();
			response.setResponseObject(new LoginService().authenticate(username, password, url));
			response.setStatus(ApplicationConstants.getSuccess());
		}
		catch (Exception e)
		{
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		return response;
	}
}
