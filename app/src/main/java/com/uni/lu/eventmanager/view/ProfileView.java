package com.uni.lu.eventmanager.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileView extends ViewModel {

	private MutableLiveData<String> mText;

	public ProfileView() {
		mText = new MutableLiveData<>();
		mText.setValue("Profile");
	}

	public LiveData<String> getText() {
		return mText;
	}
}