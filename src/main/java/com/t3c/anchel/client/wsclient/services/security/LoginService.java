package com.t3c.anchel.client.wsclient.services.security;

import java.util.Base64;
import java.util.Iterator;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;

public class LoginService
{
	final static Logger log = Logger.getLogger(LoginService.class);

	public Object authenticate(String username, String password, String url)
	{
		log.debug("Authencation using username :" + username + " URL :" + url);
		UserSessionCache cache = new UserSessionCache();
		StringBuilder urlBuffer = new StringBuilder();
		urlBuffer.append(url);
		urlBuffer.append("/linshare/webservice/rest/user/v2/authentication/authorized");
		String authString = username + ":" + password;
		String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
		log.debug("Base64 encoded auth string: " + authStringEnc);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + authStringEnc).get(ClientResponse.class);
		if (resp.getStatus() != 200)
		{
			log.error("Unable to connect to the server");
		}
		// MultivaluedMap<String, String> headder = ;
		String sessionDetails = getSessionDetails(resp.getHeaders());
		if(sessionDetails == null)
		{
			log.error("Unable to authenticate!");
			return new String("Unable to authenticate!");
		}
		cache.doInsert(sessionDetails);

		String output = resp.getEntity(String.class);
		log.debug("response: " + output);
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
