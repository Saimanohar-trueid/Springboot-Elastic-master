package com.elastic.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakSessionDtls {
	
	@Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-secret}")
	private String SECRETKEY;

	@Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
	private String CLIENTID;

	@Value("${keycloak.auth-server-url}")
	private String AUTHURL;

	@Value("${keycloak.realm}")
	private String REALM;
	
	

}
