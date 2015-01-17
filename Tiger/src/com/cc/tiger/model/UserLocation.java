package com.cc.tiger.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.GeoPt;

@SuppressWarnings("serial")
public class UserLocation implements Serializable{

	private GeoPt location;
	private long radius;
	
	public UserLocation(float latitude, float longitude, long radius) {
		this.location = new GeoPt(latitude, longitude);
		this.radius = radius;
	}
	
	public GeoPt getLocation() {
		return location;
	}

	public void setLocation(GeoPt location) {
		this.location = location;
	}

	public long getRadius() {
		return radius;
	}

	public void setRadius(long radius) {
		this.radius = radius;
	}
	
}
