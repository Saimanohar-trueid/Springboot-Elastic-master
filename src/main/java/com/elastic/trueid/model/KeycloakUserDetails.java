package com.elastic.trueid.model;

import lombok.Data;

@Data
public class KeycloakUserDetails {
	
	private String username;
	private String authToken;

}
