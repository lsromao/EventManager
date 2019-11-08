package com.uni.lu.eventmanager.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventsView extends ViewModel {

	private MutableLiveData<String> mText;

	public EventsView() {
		mText = new MutableLiveData<>();
		mText.setValue("Event - List Page - List firt 5 events - need model");

	}

	public LiveData<String> getText() {
		return mText;
	}
}