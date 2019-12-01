package com.uni.lu.eventmanager.dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.model.LikeModel;

public class LikesDAOFirestore extends DAO<LikeModel> {

	private final String collection = "likes";

	@Override
	public Task<Void> save(LikeModel pojo) {
		return getSessionFirestore()
				.collection(this.collection)
				.document(pojo.getDocName())
				.set(pojo);
	}

	@Override
	public void delete(LikeModel pojo) {
		getSessionFirestore()
				.collection(this.collection)
				.document(pojo.getDocName())
				.delete();
	}

	@Override
	public void update(LikeModel pojo) {

	}

	public LikeModel search(String eventDocument, String userId){
		Query query = FirebaseController.getInstance().getLikesCollectionReference()
				.whereEqualTo("eventDocument", eventDocument)
				.whereEqualTo("userId", userId);

		Task<QuerySnapshot> result = query.get();
		//TODO Add better task handleing
		while (!result.isComplete()) {

		}

		if (result.getResult().getDocuments().size() != 0){
			return result.getResult().getDocuments().get(0).toObject(LikeModel.class);
		}else {
			return null;
		}

	}
}
