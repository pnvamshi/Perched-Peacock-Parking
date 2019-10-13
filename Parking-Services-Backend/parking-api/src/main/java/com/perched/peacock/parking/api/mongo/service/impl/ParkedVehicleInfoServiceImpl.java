package com.perched.peacock.parking.api.mongo.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perched.peacock.parking.api.mongo.dao.ParkedVehicleInfoDAO;
import com.perched.peacock.parking.api.mongo.model.ParkedVehicleInfo;
import com.perched.peacock.parking.api.mongo.service.ParkedVehicleInfoService;

/**
 * 
 * @author pnvamshi
 *
 */

@Service
public class ParkedVehicleInfoServiceImpl implements ParkedVehicleInfoService {

	@Autowired
	ParkedVehicleInfoDAO parkedVehicleInfoDAO;
	
	@Override
	public boolean saveParkedVehicleInfo(ParkedVehicleInfo parkedVehicleInfo) {
		return parkedVehicleInfoDAO.saveParkedVehicleInfo(parkedVehicleInfo);
	}
	
	@Override
	public ParkedVehicleInfo getParkedVehicleInfo(String vehicleNumber) {
		return parkedVehicleInfoDAO.getParkedVehicleInfo(vehicleNumber);
	}
	
	@Override
	public Double generateParkingBill(String vehicleNumber) {
		Double parkingAmount = 0.0;
		ParkedVehicleInfo parkedVehicleInfo = parkedVehicleInfoDAO.getParkedVehicleInfo(vehicleNumber);
		if(parkedVehicleInfo == null) {
			
		}
		long secs = (new Date().getTime() - parkedVehicleInfo.getEntryTime().getTime()) / 1000;
		long hours = secs / 3600;    
		secs = secs % 3600;
		long mins = secs / 60;
		parkingAmount = (double) ((10 * hours) + (10*mins/60));
		return parkingAmount;
	}
}
