package com.TestServlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HTTPRequest {
	private final String USER_AGENT = "Mozilla/5.0";
	private String password = new String();
	private String username = new String();
	
	public HTTPRequest(){}
	
	public HTTPRequest(String uname, String pass){
		password = pass;
		username = uname;
	}

	
	public String sendGet(String urladdr) throws Exception {
		String authString = username + ":" + password;
		System.out.println("auth string: " + authString);
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		System.out.println("Base64 encoded auth string: " + authStringEnc);
		URL obj = new URL(urladdr);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("Authorization", "Basic " + authStringEnc);


		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + urladdr);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		//print result
		return response.toString();

	}


}
