package com.perched.peacock.parking.api.token.service.impl;

import java.security.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perched.peacock.parking.api.dto.LoginDetails;
import com.perched.peacock.parking.api.encryption.service.EncryptionService;
import com.perched.peacock.parking.api.exception.InvalidTokenException;
import com.perched.peacock.parking.api.exception.UserDoesNotExistException;
import com.perched.peacock.parking.api.mongo.model.UserProfileInfo;
import com.perched.peacock.parking.api.mongo.service.UserProfileInfoService;
import com.perched.peacock.parking.api.token.service.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


/**
 * 
 * @author pnvamshi
 *
 */

@Service
public class TokenServiceImpl implements TokenService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);
	
	@Autowired
	UserProfileInfoService userProfileInfoService;
	
	@Autowired
	private EncryptionService encryptionService;
	
	private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	@Override
	public LoginDetails generateToken(String userName, String password) {
		LoginDetails loginDetails = new LoginDetails();
		UserProfileInfo userProfileInfo = userProfileInfoService.getUserProfileInfo(userName);
		if(userProfileInfo == null) {
			throw new UserDoesNotExistException("Username : " + userName + " does not exist, register first");
		}
		String hashedPassword = userProfileInfo.getPassword();
		if(!encryptionService.comparePassword(password, hashedPassword)) {
			return loginDetails;
		}
		String token = Jwts.builder().setSubject(userProfileInfo.getRole()).signWith(key).compact();
		loginDetails.setRole(userProfileInfo.getRole());
		loginDetails.setToken(token);
		return loginDetails;
	}
	
	@Override
	public String getRoleFromToken(String token) {
		try {
			
			Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			return jwsClaims.getBody().getSubject();
		} catch (JwtException e) {
			LOGGER.error("Invalid token : {}",token);
			throw new InvalidTokenException(e);
		}
	}
}
