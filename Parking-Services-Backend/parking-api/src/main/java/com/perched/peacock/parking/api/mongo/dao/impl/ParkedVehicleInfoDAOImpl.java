package com.perched.peacock.parking.api.mongo.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.perched.peacock.parking.api.constants.SharedConstants;
import com.perched.peacock.parking.api.exception.VehicleAlreadyPresentException;
import com.perched.peacock.parking.api.mongo.dao.ParkedVehicleInfoDAO;
import com.perched.peacock.parking.api.mongo.model.ParkedVehicleInfo;

/**
 * 
 * @author pnvamshi
 *
 */

@Repository
public class ParkedVehicleInfoDAOImpl implements ParkedVehicleInfoDAO {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * @see com.perched.peacock.parking.api.mongo.dao.ParkedVehicleInfoDAO#saveParkedVehicleInfo
	 */
	@Override
	public boolean saveParkedVehicleInfo(ParkedVehicleInfo parkedVehicleInfo) {
		Query query = new Query();
		String vehicleNumber = StringUtils.isEmpty(parkedVehicleInfo.getVehicleNumber()) ? "" : parkedVehicleInfo.getVehicleNumber().replaceAll("\\s+", "");
		query.addCriteria(Criteria.where("vehicleNumber").is(vehicleNumber));
		if(mongoTemplate.exists(query, ParkedVehicleInfo.class)) {
			throw new VehicleAlreadyPresentException("Vehicle with number + " + vehicleNumber + " already present in parking lot");
		}
		parkedVehicleInfo.setVehicleNumber(vehicleNumber);
		parkedVehicleInfo.setParkingStatus(SharedConstants.VEHICLE_STATUS_PARKED);
		return mongoTemplate.save(parkedVehicleInfo) != null;
	}
	
	/**
	 * @see com.perched.peacock.parking.api.mongo.dao.ParkedVehicleInfoDAO#getParkedVehicleInfo
	 */
	@Override
	public ParkedVehicleInfo getParkedVehicleInfo(String vehicleNumber) {
		String saneVehicleNumber = StringUtils.isEmpty(vehicleNumber) ? "" : vehicleNumber.replaceAll("\\s+", ""); 
		Query query = new Query();
		query.addCriteria(Criteria.where("vehicleNumber").is(saneVehicleNumber));
		return mongoTemplate.findOne(query, ParkedVehicleInfo.class);
	}
	
	/**
	 * @see com.perched.peacock.parking.api.mongo.dao.ParkedVehicleInfoDAO#exitParking
	 */
	@Override
	public boolean exitParking(String vehicleNumber, Double parkingFee) {
		String formattedVehicleNumber = StringUtils.isEmpty(vehicleNumber) ? "" : vehicleNumber.replaceAll("\\s+", "");
		Query query = new Query();
		query.addCriteria(Criteria.where("vehicleNumber").is(formattedVehicleNumber).and("parkingStatus").is(SharedConstants.VEHICLE_STATUS_PARKED));
		Update update = new Update();
		update.set("exitTime", new Date());
		update.set("parkingFee", parkingFee);
		update.set("parkingStatus", SharedConstants.VEHICLE_STATUS_EXITED);
		return mongoTemplate.findAndModify(query, update, ParkedVehicleInfo.class)!=null;
	}
	
	/**
	 * @see com.perched.peacock.parking.api.mongo.dao.ParkedVehicleInfoDAO#getParkedVehicles
	 */
	@Override
	public List<String> getParkedVehicles(String parkingLotId){
		Query query = new Query();
		query.addCriteria(Criteria.where("parkingLotId").is(parkingLotId));
		return mongoTemplate.findDistinct(query, "vehicleNumber", ParkedVehicleInfo.class, String.class);
	}
}
