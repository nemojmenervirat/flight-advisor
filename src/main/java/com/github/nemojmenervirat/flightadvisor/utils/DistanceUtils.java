package com.github.nemojmenervirat.flightadvisor.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.github.nemojmenervirat.flightadvisor.model.Airport;

public class DistanceUtils {

	public static BigDecimal calclulateDistanceBetweenTwoAirports(Airport a, Airport b) {
		double aLongitude = a.getLongitude().doubleValue();
		double aLatitude = a.getLatitude().doubleValue();
		double bLongitude = b.getLongitude().doubleValue();
		double bLatitude = b.getLatitude().doubleValue();
		return new BigDecimal(calculateDistanceBetweenTwoLocations(aLongitude, aLatitude, bLongitude, bLatitude)).setScale(2, RoundingMode.HALF_EVEN);
	}

	/**
	 * 
	 * @param aLongitude
	 * @param aLatitude
	 * @param bLongitude
	 * @param bLatitude
	 * @return distance in miles
	 */
	public static double calculateDistanceBetweenTwoLocations(double aLongitude, double aLatitude, double bLongitude, double bLatitude) {

		double distance = (Math.sin(Math.toRadians(aLatitude)) * Math.sin(Math.toRadians(bLatitude))
				+ Math.cos(Math.toRadians(aLatitude)) * Math.cos(Math.toRadians(bLatitude)) * Math.cos(Math.toRadians(aLongitude - bLongitude)));

		if (distance > 1) {
			distance = 1;
		}
		return ((Math.toDegrees(Math.acos(distance))) * 111.18957696) * 0.621371192;
	}

	public static BigDecimal calculateDuration(BigDecimal distance) {
		return distance.divide(new BigDecimal(500)).setScale(2, RoundingMode.HALF_EVEN);
	}

}
