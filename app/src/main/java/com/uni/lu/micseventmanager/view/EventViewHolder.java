package com.uni.lu.micseventmanager.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uni.lu.micseventmanager.R;
import com.uni.lu.micseventmanager.adapter.OnItemListener;
import com.uni.lu.micseventmanager.media.GlideApp;

import java.util.Date;

public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
	private View                               view;
	private OnItemListener mClickListener;

	public EventViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
		super(itemView);
		view = itemView;
		mClickListener = onItemListener;

		itemView.setOnClickListener(this);
	}

	public void setTitle(String title) {
		TextView textView = view.findViewById(R.id.itemEventTitle);
		textView.setText(title);
	}

	public void setThumbnail(String uri) {
		ImageView        thumb       = view.findViewById(R.id.itemProfilePic);
		StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(uri);
		GlideApp.with(view.getContext())
				.setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_error_sing))
				.load(gsReference).centerCrop()
				.into(thumb);
	}

	public void setCategory(String cat){
		TextView category = view.findViewById(R.id.itemCategory);
		category.setText(cat);
	}

	public void setStartDate(Date startDate){
		TextView date = view.findViewById(R.id.startDate);
		date.setText(startDate.toString());
	}

	public void setLocation(String location){
		TextView loc = view.findViewById(R.id.itemComment);
		loc.setText(location);
	}

	@Override
	public void onClick(View v) {
		mClickListener.onEventClick(view, getAdapterPosition());

	}
}
