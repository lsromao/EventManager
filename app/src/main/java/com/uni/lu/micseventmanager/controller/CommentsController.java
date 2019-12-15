package com.uni.lu.micseventmanager.controller;

import com.google.android.gms.tasks.Task;
import com.uni.lu.micseventmanager.dao.CommentsDAOFirestore;
import com.uni.lu.micseventmanager.model.CommentModel;
import com.uni.lu.micseventmanager.model.EventModel;

import java.util.Random;

public class CommentsController {

	private CommentsDAOFirestore daoEvents;

	public CommentsController(){
		this.daoEvents = new CommentsDAOFirestore();
	}

	public Task<Void> saveComment(EventModel event, String comment){
		String docName = "comments-" + new Random().nextInt(1000);

		CommentModel commentModel = new CommentModel(comment, docName, event.getDocName(), FirebaseController.getInstance().getUserId());

		return daoEvents.save(commentModel);

	}
}
