package com.cc.tiger.utils;

/*
 * The methods from computing the distance between two GPS coordinates were
 * taken from the following URL: http://www.geodatasource.com/developers/java
 * and modified to specify the distance in meters.
 *
 */

public class LocationUtils {

	public long distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344 * 1000;
		return (long)dist;
	}
		 
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	
}
