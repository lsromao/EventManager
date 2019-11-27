package com.uni.lu.eventmanager.dao;

import com.uni.lu.eventmanager.model.EventModel;

public class EventsDAOFirestore extends DAO<EventModel> {

	private final String collection = "events";

	@Override
	public void save(EventModel pojo) {
		getSessionFirestore()
				.collection(this.collection)
				.add(pojo);
	}

	@Override
	public void delete(EventModel pojo) {
		getSessionFirestore()
				.collection(this.collection)
				.document(pojo.getDocName())
				.delete();

	}

	@Override
	public void update(EventModel pojo) {

	}
}
