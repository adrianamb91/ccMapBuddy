package com.cc.tiger.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cc.tiger.dao.UsersDao;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class GetVisibleFriendsServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		JSONObject responseMessage = new JSONObject();
		
		String userIndexStr = request.getParameter("myindex");
		
		if (userIndexStr == null || userIndexStr == "") {
			try {
				responseMessage.put("message", "who_are_you?");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			int userIndex = Integer.parseInt(userIndexStr);
			
			//TODO: retrieve min and max directly from datastore;
			int min = 0;
			int max = 99;
			
			if (userIndex < min || userIndex > max) {
				try {
					responseMessage.put("message", "invalid_user_index");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				UsersDao usersDao = new UsersDao();
				List<String> keys = usersDao.getVisibleFriendsIndexes(userIndex);
				try {
					responseMessage.put("size", keys.size());
					responseMessage.put("keys", keys);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(responseMessage.toString());
	}

}
