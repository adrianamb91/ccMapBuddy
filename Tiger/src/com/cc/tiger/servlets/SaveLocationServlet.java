package com.cc.tiger.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cc.tiger.model.UserLocation;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class SaveLocationServlet extends HttpServlet{

	private static final Logger log = Logger.getLogger(SaveLocationServlet.class.getName());
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String userIndex = request.getParameter("myindex");
		String latitude = request.getParameter("lat");
		String longitude = request.getParameter("long");
		String radius = request.getParameter("radius");
		
		MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
		memcacheService.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		
		UserLocation value = new UserLocation(Float.parseFloat(latitude), 
				Float.parseFloat(longitude), Long.parseLong(radius));
		memcacheService.put(userIndex, value, Expiration.byDeltaSeconds(1800), SetPolicy.SET_ALWAYS);
		
		log.info("New location was stored in the memcache - [userIndex: " 
				+ userIndex + ", lat: " + latitude + ", long: " + longitude 
				+ ", radius: " + radius + "]");
		
		response.setContentType("application/json; charset=UTF-8");
		JSONObject responseMessage = new JSONObject();
		try {
			responseMessage.put("success", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().write(responseMessage.toString());
	}
}
