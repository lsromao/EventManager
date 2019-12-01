package com.uni.lu.eventmanager.dao;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class DAO<T> implements IDAO<T> {

	private FirebaseFirestore sessionFirestore;

	public DAO() {
		sessionFirestore = FirebaseFirestore.getInstance();
	}

	public FirebaseFirestore getSessionFirestore() {
		return sessionFirestore;
	}

}
