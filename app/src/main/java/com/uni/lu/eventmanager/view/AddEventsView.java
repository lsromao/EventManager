package com.uni.lu.eventmanager.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddEventsView extends ViewModel {

	private MutableLiveData<String> mText;

	public AddEventsView() {
		mText = new MutableLiveData<>();
		mText.setValue("View to add events - need a model");
	}

	public LiveData<String> getText() {
		return mText;
	}
}