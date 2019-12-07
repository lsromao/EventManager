package com.uni.lu.eventmanager.controller;

import android.text.TextUtils;

import com.google.firebase.firestore.Query;

public class Filters {
	private static final Filters ourInstance = new Filters();

	private String category;
	private String title;
	private String sort;

	private boolean isChanged;

	public static Filters getInstance() {
		return ourInstance;
	}

	private Filters() {
	}

	public Query getQueryDefault(){
		return FirebaseController.getInstance().getEventsCollectionReference()
				.whereEqualTo("privacy", false)
				.orderBy("startDate", Query.Direction.DESCENDING);
	}

	public Query buildQuery(){

		Query query = FirebaseController.getInstance().getEventsCollectionReference();

		if (!TextUtils.isEmpty(category)){
			query = query.whereEqualTo("category", category);
		}

		if (!TextUtils.isEmpty(title)){
			query = query.whereEqualTo("title", title);
		}

		if (TextUtils.equals(sort, "Sort by Newest") || TextUtils.isEmpty(sort)){
			query = query
					.whereEqualTo("privacy", false)
					.orderBy("startDate", Query.Direction.DESCENDING);
		}else if (TextUtils.equals(sort, "Sort by Oldest")) {
			query = query
					.whereEqualTo("privacy", false)
					.orderBy("startDate", Query.Direction.ASCENDING);
		}else if (TextUtils.equals(sort, "Sort by My Events")) {
			query = query
					.whereEqualTo("userId", FirebaseController.getInstance().getUserId())
					.orderBy("startDate", Query.Direction.DESCENDING);
		}

		resetFilter();
		return query;
	}

	public void resetFilter(){
		category = "";
		title = "";
		sort = "Sort by Newest";
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean changed) {
		isChanged = changed;
	}
}
