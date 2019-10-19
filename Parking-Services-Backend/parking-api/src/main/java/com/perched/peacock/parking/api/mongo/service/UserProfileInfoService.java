package com.perched.peacock.parking.api.mongo.service;

import java.util.List;

import com.perched.peacock.parking.api.mongo.model.UserProfileInfo;

/**
 * 
 * @author pnvamshi
 *
 */

public interface UserProfileInfoService {

	/**
	 * 
	 * @param userProfileInfo
	 * @return
	 */
	boolean saveUserProfileInfo(UserProfileInfo userProfileInfo);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	UserProfileInfo getUserProfileInfo(String userName);
	
	/**
	 * 
	 * @param userProfileInfo
	 * @return
	 */
	boolean updateUserProfileInfo(UserProfileInfo userProfileInfo);

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	boolean updateUserProfilePassword(String userName, String password);

	/**
	 * 
	 * @param role
	 * @return
	 */
	List<String> getUserNames(String role);

}
