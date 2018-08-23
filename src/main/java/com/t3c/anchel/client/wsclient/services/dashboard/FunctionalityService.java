package com.t3c.anchel.client.wsclient.services.dashboard;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.t3c.anchel.client.model.dashboard.FunctionalityDto;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;

public class FunctionalityService {

	final static Logger OUT = Logger.getLogger(FunctionalityService.class);
	ObjectMapper mapper = null;
	StringBuilder urlBuffer = null;

	@SuppressWarnings("unchecked")
	public List<FunctionalityDto> findAll(String username)
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		OUT.debug("Getting a rolebased functionalities for user :" + username);
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.FUNCTIONALITIES);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
			throw new IOException();
		} else {
			String jsonString = resp.getEntity(String.class);
			TypeReference<List<FunctionalityDto>> mapType = new TypeReference<List<FunctionalityDto>>() {
			};
			List<FunctionalityDto> funList = mapper.readValue(jsonString, mapType);
			if (restClient != null) {
				restClient.destroy();
			}
			OUT.debug("Getting a rolebased functionalities for user :" + username + " found :"
					+ (funList.size() > 0 ? funList.size() : "NOT FOUND"));
			return funList;
		}
	}

}
