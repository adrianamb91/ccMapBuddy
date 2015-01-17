package com.cc.tiger.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class SetupServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		//clean up the datastore
		Query qq = new Query("User");
		PreparedQuery ppqq = datastore.prepare(qq);
		List<Key> keys = new ArrayList<Key>();
		for (Entity e : ppqq.asIterable()) {
			keys.add(e.getKey());
		}
		datastore.delete(keys);
		
		for (int i = 0; i < 100; i++) {
			String username = "User" + i;
			Entity entity = new Entity("User", username);
			entity.setProperty("index", i);
			ArrayList<Integer> friends = new ArrayList<>();
			ArrayList<Integer> showMeFriends = new ArrayList<>();
			for (int j = 0; j < 100; j++) {
				if (j != i) {
					if (j % 10 == i % 10) {
						friends.add(j);
						showMeFriends.add(j);
					}
					if ((j / 10) % 10 == (i / 10) % 10) {
						friends.add(j);
					}
				}
			}
			entity.setProperty("friends", friends);
			entity.setProperty("showme", showMeFriends);
			entity.setProperty("authenticated", false);
			datastore.put(entity);
		}
		
		Query q = new Query("User");
		PreparedQuery pq = datastore.prepare(q);
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Setting up");
		
		for (Entity result : pq.asIterable()) {
			resp.getWriter().println(result);
		}
		
		
	}
}
