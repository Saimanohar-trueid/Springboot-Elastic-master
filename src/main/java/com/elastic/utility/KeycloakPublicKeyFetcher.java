package com.elastic.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

public class KeycloakPublicKeyFetcher {
	
	public static String fetchRealmPublicKey(String keycloakUrl, String realmName) throws Exception {
        URL url = new URL(keycloakUrl + "/realms/" + realmName + "/protocol/openid-connect/certs");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed to fetch realm public key. HTTP error code: " + responseCode);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String json = response.toString();
        JSONObject jsonObj = new JSONObject(json);
        JSONArray keys = jsonObj.getJSONArray("keys");
        JSONObject key = keys.getJSONObject(0);
        System.out.println("Key val "+key);
        JSONArray x5c =  key.getJSONArray("x5c");
        String pKeyA = x5c.getJSONArray(0).toString();
      //  String pKey = pKeyA.getString(0).replace("\\n", "");
        String publicKey = key.getString("x5c").replace("\\n", "");
     //   String publicKey = key.getString("x5c");
        return pKeyA.replace("\\n", "");
    }

	/*
	 * public static void main(String[] args) throws Exception { String keycloakUrl
	 * = "http://localhost:8085"; String realmName = "test-trueid"; String publicKey
	 * = fetchRealmPublicKey(keycloakUrl, realmName);
	 * System.out.println("Realm Public Key: " + publicKey); }
	 */

}
