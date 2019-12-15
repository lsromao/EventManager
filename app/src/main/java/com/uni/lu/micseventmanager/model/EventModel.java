package com.uni.lu.micseventmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class EventModel implements Parcelable {

	private String  docName;
	private String  title;
	private String  description;
	private String  category;
	private String  location;
	private boolean privacy;
	private String  uriCover;
	private String  userId;
	private Date    startDate;
	private Date    created;

	public EventModel() {
	}

	public EventModel(String docName, String title, String description, String category, String location,
	                  boolean privacy, String uriCover, String userId, Date startDate, Date created) {
		this.docName = docName;
		this.title = title;
		this.description = description;
		this.category = category;
		this.location = location;
		this.privacy = privacy;
		this.uriCover = uriCover;
		this.userId = userId;
		this.startDate = startDate;
		this.created = created;
	}


	protected EventModel(Parcel in) {
		docName = in.readString();
		title = in.readString();
		description = in.readString();
		category = in.readString();
		location = in.readString();
		privacy = in.readByte() != 0;
		uriCover = in.readString();
		userId = in.readString();
		startDate = (Date) in.readSerializable();
		created = (Date) in.readSerializable();
	}

	public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
		@Override
		public EventModel createFromParcel(Parcel in) {
			return new EventModel(in);
		}

		@Override
		public EventModel[] newArray(int size) {
			return new EventModel[size];
		}
	};

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getUriCover() {
		return uriCover;
	}

	public void setUriCover(String uriCover) {
		this.uriCover = uriCover;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(docName);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(category);
		dest.writeString(location);
		dest.writeByte((byte) (privacy ? 1 : 0));
		dest.writeString(uriCover);
		dest.writeString(userId);
		dest.writeSerializable(startDate);
		dest.writeSerializable(created);
	}
}
