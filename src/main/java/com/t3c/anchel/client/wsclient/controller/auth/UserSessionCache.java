package com.t3c.anchel.client.wsclient.controller.auth;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class UserSessionCache
{
	final static Logger					OUT			= Logger.getLogger(UserSessionCache.class);
	private static final UserSessionCache INSTANCE = new UserSessionCache();
	private static Map<String, String>	userSession	= new HashMap<String, String>();

	public void doInsert(String key, String value)
	{
		OUT.debug("Inserting key :" + key);
		userSession.put(key, value);
	}

	public void doUpdate(String key, String value)
	{
		OUT.debug("Updating key :" + key);
		userSession.put(key, value);
	}

	public String doGet(String key)
	{
		OUT.debug("Getting key :" + key);
		return userSession.get(key);
	}

	public void doDelete(String key)
	{
		OUT.debug("Deleting key :" + key);
		userSession.remove(key);
		OUT.debug("User :" + key + " logged out successfully");
	}

	public void doDeleteAll()
	{
		OUT.debug("Removing all session");
	}

	public static UserSessionCache getInstance()
	{
		return INSTANCE;
	}

}
