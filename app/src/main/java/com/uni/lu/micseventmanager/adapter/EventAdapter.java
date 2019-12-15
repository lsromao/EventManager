package com.uni.lu.micseventmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.uni.lu.micseventmanager.R;
import com.uni.lu.micseventmanager.model.EventModel;
import com.uni.lu.micseventmanager.view.EventViewHolder;

public abstract class EventAdapter extends FirestoreRecyclerAdapter<EventModel, EventViewHolder> {

	private OnItemListener mOnItemListener;

	public EventAdapter(@NonNull FirestoreRecyclerOptions<EventModel> options, OnItemListener onItemListener) {
		super(options);
		mOnItemListener = onItemListener;
	}

	@Override
	protected void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i, @NonNull EventModel eventModel) {
		eventViewHolder.setTitle(eventModel.getTitle());
		eventViewHolder.setCategory(eventModel.getCategory());
		eventViewHolder.setLocation(eventModel.getLocation());
		eventViewHolder.setStartDate(eventModel.getStartDate());
		eventViewHolder.setThumbnail(eventModel.getUriCover());
	}

	@NonNull
	@Override
	public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,
				parent, false);

		return new EventViewHolder(view, mOnItemListener);
	}
}
