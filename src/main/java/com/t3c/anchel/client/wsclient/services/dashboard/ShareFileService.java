package com.t3c.anchel.client.wsclient.services.dashboard;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.FileDetailsDTO;
import com.t3c.anchel.client.model.dashboard.LdapUsersDto;
import com.t3c.anchel.client.model.dashboard.ShareCreationDto;
import com.t3c.anchel.client.model.dashboard.SharedDetailsDto;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;

public class ShareFileService {

	final static Logger OUT = Logger.getLogger(ShareFileService.class);
	ObjectMapper mapper = null;
	StringBuilder urlBuffer = null;
	ResponseObject response = null;

	public List<LdapUsersDto> getAllUsers(String username)
			throws JsonParseException, JsonMappingException, IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		OUT.debug("Getting a ldap users email id's");
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.LIST_EMAIL_IDS);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
		}
		String jsonString = resp.getEntity(String.class);

		TypeReference<List<LdapUsersDto>> type = new TypeReference<List<LdapUsersDto>>() {
		};
		List<LdapUsersDto> emailList = mapper.readValue(jsonString, type);
		if (restClient != null) {
			restClient.destroy();
		}
		OUT.debug("Getting a ldap user email id's for user :" + username + " found :"
				+ (emailList.size() > 0 ? emailList.size() : "NOT FOUND"));
		return emailList;
	}

	public Object shareFile(ShareCreationDto shareDto, String username)
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		Gson gson = new Gson();
		String jsonInString = gson.toJson(shareDto);
		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.SHARE_FILE);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.type("application/json").post(ClientResponse.class, jsonInString);
		String jsonString = resp.getEntity(String.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
			throw new IOException();
		} else {
			TypeReference<List<SharedDetailsDto>> type = new TypeReference<List<SharedDetailsDto>>() {
			};
			List<SharedDetailsDto> sharedDetailsDtos = mapper.readValue(jsonString, type);
			return sharedDetailsDtos;
		}
	}

	public Object shareFileDetails(String username, String uuid) throws IOException {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		urlBuffer = new StringBuilder();
		OUT.debug("Getting a shared file details..");
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.MY_FILE);
		urlBuffer.append("/" + uuid + "?withShares=true");
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
			throw new IOException();
		} else {
			String jsonString = resp.getEntity(String.class);
			List<FileDetailsDTO> sharedFileList = mapper.readValue(jsonString,
					new TypeReference<List<FileDetailsDTO>>() {
					});
			if (restClient != null) {
				restClient.destroy();
			}
			OUT.debug("Getting a shared file details for user :" + username + " found :"
					+ (sharedFileList.size() > 0 ? sharedFileList.size() : "NOT FOUND"));
			return sharedFileList;
		}
	}

}
