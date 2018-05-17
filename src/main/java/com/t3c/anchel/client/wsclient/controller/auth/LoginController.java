package com.t3c.anchel.client.wsclient.controller.auth;

import org.apache.log4j.Logger;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.wsclient.services.security.LoginService;

public class LoginController
{
	final static Logger log = Logger.getLogger(LoginController.class);

	public ResponseObject isAuthorised(String username, String password, String url)
	{
		log.debug("Authentication started with username :" + username + " URL :" + url);
		ResponseObject response = new ResponseObject();
		response.setResponseObject(new LoginService().authenticate(username,password,url));
		return response;
	}
}
