package com.uni.lu.micseventmanager.controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.uni.lu.micseventmanager.dao.EventsDAOFirestore;
import com.uni.lu.micseventmanager.model.EventModel;
import com.uni.lu.micseventmanager.util.DateUtils;

import java.util.Date;

public class EventsController {

	private EventsDAOFirestore daoEvents;
	private EventModel         event;
	private DateUtils          dtFormat;

	public EventsController() {
		daoEvents = new EventsDAOFirestore();
		dtFormat = new DateUtils();
	}

	private static final String TAG = "EventController";

	public boolean eventValidation(Activity activity, EventModel event, String date, String time) {
		Date today = new Date();


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
		} else if (date.length() < 1) {
			Toast.makeText(activity, "Start Date cannot be empty!", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Error Event: Start Date empty!");
		} else if (time.length() < 1) {
			Toast.makeText(activity, "Start Time cannot be empty!", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Error Event: Start Time empty!");
		} else {
			Date start = dtFormat.getDateTime(date, time);
			if (start.before(today)) {
				Toast.makeText(activity, "Start date cannot be in the past!", Toast.LENGTH_SHORT).show();
				Log.w(TAG, "Error Event: Start date in the past");
			}else{
				this.event = event;
				this.event.setStartDate(start);
				this.event.setCreated(today);
			}

			return true;
		}

		return false;
	}

	public Task<Void> saveEvent() {
		return daoEvents.save(this.event);
	}

	public DateUtils getDtFormat() {
		return dtFormat;
	}

	public EventModel getEvent(){
		return event;
	}
}
