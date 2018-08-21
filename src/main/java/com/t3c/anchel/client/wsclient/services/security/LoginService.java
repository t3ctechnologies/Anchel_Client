package com.t3c.anchel.client.wsclient.services.security;

import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;

public class LoginService
{
	final static Logger OUT = Logger.getLogger(LoginService.class);

	public Object authenticate(String username, String password, String url) throws IOException
	{
		OUT.debug("Authencation using username :" + username + " URL :" + url);
		StringBuilder urlBuffer = new StringBuilder();
		urlBuffer.append(url);
		urlBuffer.append(ApplicationConstants.LOGIN);
		String authString = username + ":" + password;
		String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
		OUT.debug("Base64 encoded auth string: " + authStringEnc);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + authStringEnc).get(ClientResponse.class);
		if (resp.getStatus() != 200)
		{
			OUT.error("Unable to connect to the server");
			throw new IOException();
		}
		String sessionDetails = getSessionDetails(resp.getHeaders());
		if (sessionDetails == null)
		{
			OUT.error("Unable to authenticate!");
			return new String("Unable to authenticate!");
		}
		UserSessionCache.getInstance().doInsert(username + "_base", authStringEnc);
		UserSessionCache.getInstance().doInsert(username + "_url", url);

		String output = resp.getEntity(String.class);
		OUT.debug("response: " + output);
		if (restClient != null)
		{
			restClient.destroy();
		}
		return output;
	}

	private String getSessionDetails(MultivaluedMap<String, String> headder)
	{
		Iterator<String> it = headder.keySet().iterator();
		while (it.hasNext())
		{
			String key = (String) it.next();
			if (key.equalsIgnoreCase("Set-Cookie"))
			{
				return headder.getFirst(key);
			}
		}
		return null;

	}

}
