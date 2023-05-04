package com.elastic.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.elastic.authModel.LoginRequest;
import com.elastic.authModel.UserDTO;




@Component
public class KeyCloakService {

	@Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-secret}")
	private String SECRETKEY;

	@Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
	private String CLIENTID;

	@Value("${keycloak.auth-server-url}")
	private String AUTHURL;

	@Value("${keycloak.realm}")
	private String REALM;
	
	//private static final String KEYCLOAK_TOKEN = "http://localhost:8085/auth/realms/test-trueid/protocol/openid-connect/token";
	private static final String KEYCLOAK_CLIENT_ID = "keycloak-security";
    private static final String KEYCLOAK_CLIENT_SECRET = "client-secret";
    private static final String INTROSPECTION_URL = "http://localhost:8085/auth/realm/test-trueid/protocol/openid-connect/token/introspect";

	public String getToken(LoginRequest userCredentials) {

		String responseToken = null;
		try {

			String username = userCredentials.getUsername();

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("grant_type", "password"));
			urlParameters.add(new BasicNameValuePair("client_id", CLIENTID));
			urlParameters.add(new BasicNameValuePair("username", username));
			urlParameters.add(new BasicNameValuePair("password", userCredentials.getPassword()));
			urlParameters.add(new BasicNameValuePair("client_secret", SECRETKEY));

			responseToken = sendPost(urlParameters);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseToken;

	}

	public String getByRefreshToken(String refreshToken) {

		String responseToken = null;
		try {

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
			urlParameters.add(new BasicNameValuePair("client_id", CLIENTID));
			urlParameters.add(new BasicNameValuePair("refresh_token", refreshToken));
			urlParameters.add(new BasicNameValuePair("client_secret", SECRETKEY));

			responseToken = sendPost(urlParameters);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return responseToken;
	}

	public int createUserInKeyCloak(UserDTO userDTO) {

		int statusId = 0;
		try {

			UsersResource userRessource = getKeycloakUserResource();

			UserRepresentation user = new UserRepresentation();
			user.setUsername(userDTO.getUserName());
			user.setEmail(userDTO.getEmailAddress());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setEnabled(true);

			// Create user
			Response result = userRessource.create(user);
			System.out.println("Keycloak create user response code>>>>" + result.getStatus());

			statusId = result.getStatus();

			if (statusId == 201) {

				String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

				System.out.println("User created with userId:" + userId);

				// Define password credential
				CredentialRepresentation passwordCred = new CredentialRepresentation();
				passwordCred.setTemporary(false);
				passwordCred.setType(CredentialRepresentation.PASSWORD);
				passwordCred.setValue(userDTO.getPassword());

				// Set password credential
				userRessource.get(userId).resetPassword(passwordCred);

				// set role
				RealmResource realmResource = getRealmResource();
				RoleRepresentation savedRoleRepresentation = realmResource.roles().get("user").toRepresentation();
				realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(savedRoleRepresentation));

				System.out.println("Username==" + userDTO.getUserName() + " created in keycloak successfully");

			}

			else if (statusId == 409) {
				System.out.println("Username==" + userDTO.getUserName() + " already present in keycloak");

			} else {
				System.out.println("Username==" + userDTO.getUserName() + " could not be created in keycloak");

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return statusId;

	}

	// after logout user from the keycloak system. No new access token will be
	// issued.
	public void logoutUser(String userId) {

		UsersResource userRessource = getKeycloakUserResource();

		userRessource.get(userId).logout();

	}

	// Reset passowrd
	public void resetPassword(String newPassword, String userId) {

		UsersResource userResource = getKeycloakUserResource();

		// Define password credential
		CredentialRepresentation passwordCred = new CredentialRepresentation();
		passwordCred.setTemporary(false);
		passwordCred.setType(CredentialRepresentation.PASSWORD);
		passwordCred.setValue(newPassword.toString().trim());

		// Set password credential
		userResource.get(userId).resetPassword(passwordCred);

	}

	private UsersResource getKeycloakUserResource() {

		Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("admin").password("admin")
				.clientId("admin-cli").resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
				.build();

		RealmResource realmResource = kc.realm(REALM);
		UsersResource userRessource = realmResource.users();

		return userRessource;
	}
	
	@SuppressWarnings("unused")
	private String getKeycloakSessionID(LoginRequest request) {

		Keycloak keycloak = KeycloakBuilder.builder()
		        .serverUrl(AUTHURL)
		        .realm(REALM)
		        .clientId(CLIENTID)
		        .clientSecret(SECRETKEY)
		        .username(request.getUsername())
		        .password(request.getPassword())
		        .build();

		 UserRepresentation user = keycloak.realm(REALM).users().search(request.getUsername()).get(0);

	        // Get the user's session ID
	        String sessionId = keycloak.realm(REALM).users().get(user.getId()).getUserSessions().get(0).getId();

	        System.out.println("Session ID for " + request.getUsername() + ": " + sessionId);


		return sessionId;
	}

	private RealmResource getRealmResource() {

		Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("admin").password("admin")
				.clientId("admin-cli").resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
				.build();

		RealmResource realmResource = kc.realm(REALM);

		return realmResource;

	}

	private String sendPost(List<NameValuePair> urlParameters) throws Exception {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(AUTHURL + "/realms/" + REALM + "/protocol/openid-connect/token");
		
		//HttpPost post = new HttpPost(KEYCLOAK_TOKEN);

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = client.execute(post);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();

	}
	
	public AccessToken authorizeToken(String token) {
        ResteasyClient client = new ResteasyClientBuilderImpl().build();
        //String introspection_url = AUTHURL + "/realms/" + REALM + "/protocol/openid-connect/auth";
        ResteasyWebTarget target = client.target(INTROSPECTION_URL.replace("realm", REALM));
        target.register(new ClientRequestFilter() {
            @Override
            public void filter(ClientRequestContext requestContext) throws IOException {
                String credentials = KEYCLOAK_CLIENT_ID + ":" + SECRETKEY;
                String authorization = "Basic " + java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
                requestContext.getHeaders().add("Authorization", authorization);
            }
        });
        Form form = new Form();
        form.param("token", token);
        form.param("token_type_hint", "access_token");
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.form(form));
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed to introspect token");
        }
        AccessToken introspectionResponse = response.readEntity(AccessToken.class);
        if (!introspectionResponse.isActive()) {
            throw new RuntimeException("Token is not active");
        }
        return introspectionResponse;
    }

}
