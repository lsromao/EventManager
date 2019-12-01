package com.uni.lu.eventmanager.dao;

import com.google.android.gms.tasks.Task;
import com.uni.lu.eventmanager.model.EventModel;

public class EventsDAOFirestore extends DAO<EventModel> {

	private final String collection = "events";

	@Override
	public Task<Void> save(EventModel pojo) {
		return getSessionFirestore()
				.collection(this.collection)
				.document(pojo.getDocName())
				.set(pojo);
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
