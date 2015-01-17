package com.cc.tiger.servlets;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cc.tiger.model.UserLocation;
import com.cc.tiger.utils.LocationUtils;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class GetFriendLocationServlet extends HttpServlet{

	LocationUtils lu = new LocationUtils();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String friendIndex = request.getParameter("friend_index");
		
		String myLongitudeStr = request.getParameter("long");
		String myLatitudeStr = request.getParameter("lat");
		
		MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
		memcacheService.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		
		JSONObject responseMessage = new JSONObject();
		try {
			responseMessage.put("friend_index", friendIndex);
			
			UserLocation userLocation = (UserLocation) memcacheService.get(friendIndex);
			if (userLocation == null) {
				responseMessage.put("found", false);
			} else {
				responseMessage.put("found", true);
				
				if (userLocation.getRadius() > lu.distance(Double.parseDouble(myLatitudeStr), 
						Double.parseDouble(myLongitudeStr), 
						userLocation.getLocation().getLatitude(), 
						userLocation.getLocation().getLongitude())) {
					responseMessage.put("inRadius", true);
				} else {
					responseMessage.put("inRadius", false);
				}
				responseMessage.put("lat", userLocation.getLocation().getLatitude());
				responseMessage.put("long", userLocation.getLocation().getLongitude());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(responseMessage.toString());
		
		
	}
}
