package com.uni.lu.eventmanager.model;

import java.util.Date;

public class EventModel {

	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private String location;
	private boolean privacy;
	private String userId;
	private String uriThumbnail;
	private String uriCover;

	public EventModel(String title, String description, Date startDate, Date endDate, String location,
	                  boolean privacy, String userId, String uriThumbnail, String uriCover) {
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = location;
		this.privacy = privacy;
		this.userId = userId;
		this.uriThumbnail = uriThumbnail;
		this.uriCover = uriCover;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isPrivacy() {
		return privacy;
	}

	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}


}
