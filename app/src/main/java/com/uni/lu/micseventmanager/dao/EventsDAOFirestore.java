package com.uni.lu.micseventmanager.dao;

import com.google.android.gms.tasks.Task;
import com.uni.lu.micseventmanager.model.EventModel;

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
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Task<Void> update(EventModel pojo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
