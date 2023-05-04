package com.elastic.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Base64;

import org.springframework.stereotype.Service;

//@Service
public class JwtDecoderService {
	
	private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    private static final byte[] secretBytes = secret.getEncoded();
    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);


    public Claims decodeJwt(String jwt) {
        return Jwts.parser().setSigningKey(base64SecretBytes)
        		.parseClaimsJwt(jwt).getBody();
    }
}
