package com.uni.lu.eventmanager.dao;

import com.google.android.gms.tasks.Task;
import com.uni.lu.eventmanager.model.CommentModel;

public class CommentsDAOFirestore extends DAO<CommentModel> {

	private final String collection = "comments";

	@Override
	public Task<Void> save(CommentModel pojo) {
		return getSessionFirestore()
				.collection(this.collection)
				.document(pojo.getDocName())
				.set(pojo);
	}

	@Override
	public void delete(CommentModel pojo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Task<Void> update(CommentModel pojo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
