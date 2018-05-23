package com.t3c.anchel.client.wsclient.services.dashboard;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.t3c.anchel.client.model.dashboard.FileDetailsDTO;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;

public class DashboardService
{
	final static Logger OUT = Logger.getLogger(DashboardService.class);

	public List<FileDetailsDTO> getMyFiles(String username) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder urlBuffer = new StringBuilder();

		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.MY_FILE);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64).get(ClientResponse.class);
		if (resp.getStatus() != 200)
		{
			OUT.error("Unable to connect to the server");
		}
		String jsonString = resp.getEntity(String.class);
		List<FileDetailsDTO> participantJsonList = mapper.readValue(jsonString, new TypeReference<List<FileDetailsDTO>>()
		{
		});
		if (restClient != null)
		{
			restClient.destroy();
		}
		return participantJsonList;
	}
}
