package com.uni.lu.eventmanager.controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.uni.lu.eventmanager.dao.EventsDAOFirestore;
import com.uni.lu.eventmanager.model.EventModel;

public class EventsController {

	private EventsDAOFirestore daoEvents;
	private EventModel event;

	public EventsController(){
		daoEvents = new EventsDAOFirestore();
	}

	private static final String TAG = "EventController";

	public boolean eventValidation(Activity activity, EventModel event){

		if (event.getTitle().length() < 1) {
			Toast.makeText(activity, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Error Event: Title Empty");
		} else if (event.getDescription().length() < 1) {
			Toast.makeText(activity, "Description cannot be empty or less than 10 chars!", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Error Event: Description Error!");
		} else if (event.getLocation().length() < 1) {
			Toast.makeText(activity, "Location cannot be empty!", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Error Event: Location empty!");
		} else if (event.getCategory().equals("Select")) {
			Toast.makeText(activity, "Please select a category!", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Error Event: Category not select!");
		} else {
			this.event = event;

			return true;
		}

		return false;
	}

	public Task<Void> saveEvent(){
		this.event.setDocName(
				event.getTitle().replaceAll("\\s+", "") + event.getStartDate().hashCode());

		return daoEvents.save(this.event);
	}

}
