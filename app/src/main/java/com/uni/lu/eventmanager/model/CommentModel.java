package com.uni.lu.eventmanager.model;

import com.uni.lu.eventmanager.controller.FirebaseController;

import java.util.Date;

public class CommentModel {

	private String docName;
	private String comment;
	private String eventDocument;
	private String userName;
	private String userId;
	private String userPic;
	private Date   date;

	public CommentModel(String comment, String docName, String eventDocument, String userId){
		this.docName = docName;
		this.comment = comment;
		this.eventDocument = eventDocument;
		this.userName = FirebaseController.getInstance().getUserName();
		this.userId = userId;
		this.userPic = FirebaseController.getInstance().getUserImageUrl();
		this.date = new Date();
	}

	public CommentModel(){}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
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
