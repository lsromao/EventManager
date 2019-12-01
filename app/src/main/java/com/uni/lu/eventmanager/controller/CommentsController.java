package com.uni.lu.eventmanager.controller;

import com.google.android.gms.tasks.Task;
import com.uni.lu.eventmanager.dao.CommentsDAOFirestore;
import com.uni.lu.eventmanager.model.CommentModel;
import com.uni.lu.eventmanager.model.EventModel;

import java.util.Random;

public class CommentsController {

	private CommentsDAOFirestore daoEvents;

	public CommentsController(){
		this.daoEvents = new CommentsDAOFirestore();
	}

	public Task<Void> saveComment(EventModel event, String comment){
		String docName = event.getTitle().replaceAll("\\s+", "") + new Random().nextInt(1000);

		CommentModel commentModel = new CommentModel(comment, docName, event.getDocName(), event.getUserId());

		return daoEvents.save(commentModel);

	}
}
