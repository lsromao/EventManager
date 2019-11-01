package com.uni.lu.eventmanager.model;

import android.net.Uri;

import com.uni.lu.eventmanager.controller.FirebaseController;

public class UserModel {

	private String userName;
	private String email;
	private Uri    photoUrl;

	public UserModel(){
		this.userName = FirebaseController.getInstance().getmAuth().getCurrentUser().getDisplayName();
		this.email = FirebaseController.getInstance().getmAuth().getCurrentUser().getEmail();
		this.photoUrl = FirebaseController.getInstance().getmAuth().getCurrentUser().getPhotoUrl();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Uri getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(Uri photoUrl) {
		this.photoUrl = photoUrl;
	}
}
