package com.cc.tiger.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class UsersDao {

	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public List<String> getFriendsIndexes(int userIndex) {
		
		Filter me = new FilterPredicate("index", FilterOperator.EQUAL, userIndex);
		Query q = new Query("User").setFilter(me);
		PreparedQuery pq = datastore.prepare(q);
		
		Entity meMyselfAndI = pq.asSingleEntity();
		
		@SuppressWarnings("unchecked")
		List<Integer> friendIndexes = (List<Integer>) meMyselfAndI.getProperty("friends");
		
		Filter friends = new FilterPredicate("index", FilterOperator.IN, friendIndexes);
		Query qq = new Query("User").setFilter(friends);
		PreparedQuery ppqq = datastore.prepare(qq);

		List<String> keys = new ArrayList<String>();	
		for (Entity result : ppqq.asIterable()) {
			keys.add(result.getKey().getName());
		}
		
		return keys;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getVisibleFriendsIndexes(int userIndex) {
		Filter meFilter = new FilterPredicate("index", FilterOperator.EQUAL, userIndex);
		Query q = new Query("User").setFilter(meFilter);
		PreparedQuery pq = datastore.prepare(q);
		
		Entity me = pq.asSingleEntity();
		ArrayList<Integer> myFriends = (ArrayList<Integer>) me.getProperty("friends");
		
		Filter friendsFilter = new FilterPredicate("index", FilterOperator.IN, myFriends);
		Query q1 = new Query("User").setFilter(friendsFilter);
		PreparedQuery pq1 = datastore.prepare(q1);
		
		List<String> visibleFriends = new ArrayList<>();
		
		for (Entity e : pq1.asIterable()) {
			List<Long> friendVisibleTo = (List<Long>) e.getProperty("showme");
			for (Long i : friendVisibleTo) {
				if (i == userIndex) {
					visibleFriends.add(e.getKey().getName());
				}
			}
		}
		
		
		return visibleFriends;
	}
	
}
