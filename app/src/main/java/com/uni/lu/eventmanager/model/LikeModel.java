package com.uni.lu.eventmanager.model;

import com.uni.lu.eventmanager.controller.FirebaseController;

public class LikeModel {

	private String userId;
	private String category;
	private String eventDocument;

	public LikeModel(){}

	public LikeModel(String category, String eventDocument){
		this.category =category;
		this.eventDocument = eventDocument;
		this.userId = FirebaseController.getInstance().getUserId();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	public String getEventDocument() {
		return eventDocument;
	}

	public void setEventDocument(String eventDocument) {
		this.eventDocument = eventDocument;
	}
}
