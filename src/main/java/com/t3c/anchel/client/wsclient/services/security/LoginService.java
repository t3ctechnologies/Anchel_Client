package com.t3c.anchel.client.wsclient.services.security;

import java.util.Base64;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class LoginService
{
	final static Logger log = Logger.getLogger(LoginService.class);

	public Object authenticate(String username, String password, String url)
	{
		log.debug("Authencation using username :" + username + " URL :" + url);
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
		String output = resp.getEntity(String.class);
		log.debug("response: " + output);
		return output;
	}

}
