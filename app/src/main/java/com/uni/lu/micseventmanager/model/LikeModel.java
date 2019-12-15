package com.uni.lu.micseventmanager.model;

public class LikeModel {

	private String docName;
	private String userId;
	private String category;
	private String eventDocument;

	public LikeModel(){}

	public LikeModel(String category, String eventDocument, String userId){
		this.category =category;
		this.eventDocument = eventDocument;
		this.userId = userId;
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

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
}
