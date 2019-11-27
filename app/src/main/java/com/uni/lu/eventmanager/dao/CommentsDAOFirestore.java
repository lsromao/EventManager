package com.uni.lu.eventmanager.dao;

import com.uni.lu.eventmanager.model.CommentModel;

public class CommentsDAOFirestore extends DAO<CommentModel> {

	private final String collection = "comments";

	@Override
	public void save(CommentModel pojo) {
		getSessionFirestore()
				.collection(this.collection)
				.add(pojo);
	}

	@Override
	public void delete(CommentModel pojo) {
		getSessionFirestore()
				.collection(this.collection)
				.document(pojo.getEventDocument())// TODO: Add Document name in firebase
				.delete();

	}

	@Override
	public void update(CommentModel pojo) {

	}
}
