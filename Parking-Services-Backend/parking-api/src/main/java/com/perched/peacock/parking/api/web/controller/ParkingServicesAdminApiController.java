package com.perched.peacock.parking.api.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perched.peacock.parking.api.mongo.model.UserProfileInfo;
import com.perched.peacock.parking.api.mongo.service.UserProfileInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author pnvamshi
 *
 */

@RestController
@RequestMapping("/parking/admin/service")
@Api(value = "/parking/admin/service", produces = APPLICATION_JSON_VALUE, tags = "Parking Operator API Service")
public class ParkingServicesAdminApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParkingServicesAdminApiController.class);
	
	@Autowired
	UserProfileInfoService userProfileInfoService;
	
	@ApiOperation(value = "Save User Profile info", notes = "Return true if save successful")
	@RequestMapping(value = "save/user/profile/info", method = {POST}, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	public boolean saveUserProfileInfo(@Valid @RequestBody @ApiParam(value = "value", required = true) UserProfileInfo userProfileInfo) {
		LOGGER.info("Saving record for request : {}", userProfileInfo);
		boolean response = false;
		try {
			response = userProfileInfoService.saveUserProfileInfo(userProfileInfo);
		}catch(Exception e){
			LOGGER.error("Exception occured while processing request : {} as {}", userProfileInfo.getUserName(), e);
		}
		
		return response;
	}
}