package com.t3c.anchel.client.wsclient.controller.auth;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.t3c.anchel.client.utils.consts.ApplicationConstants;

public class UserSessionCache
{
	final static Logger					OUT			= Logger.getLogger(UserSessionCache.class);

	private static Map<String, String>	userSession	= new HashMap<String, String>();

	public void doInsert(String SessionId)
	{
		OUT.debug("Inserting user session ID :" + SessionId);
		userSession.put(ApplicationConstants.USER_SESSION_ID, SessionId);
	}

	public void doUpdate(String SessionId)
	{
		OUT.debug("Updating user session ID :" + SessionId);
		userSession.put(ApplicationConstants.USER_SESSION_ID, SessionId);
	}

	public String doGet()
	{
		OUT.debug("Getting user session ID");
		return userSession.get(ApplicationConstants.USER_SESSION_ID);
	}

	public void doDelete(String SessionId)
	{
		OUT.debug("Deleting user session ID :" + SessionId);
		userSession.remove(SessionId);
	}
	
	public void doDeleteAll()
	{
		OUT.debug("Removing all session");
	}

}
