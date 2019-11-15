package com.uni.lu.eventmanager.view;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.media.GlideApp;

public class CommentViewHolder extends RecyclerView.ViewHolder {
	private View itemView;

	public CommentViewHolder(@NonNull View itemView) {
		super(itemView);
		this.itemView = itemView;
	}

	public void setProfilePic(String uri, Activity activity){
		ImageView profile = itemView.findViewById(R.id.itemProfilePic);
		GlideApp.with(activity)
				.setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_user))
				.load(uri).centerCrop()
				.into(profile);
	}

	public void setComment(String commentText){
		TextView comment = itemView.findViewById(R.id.itemComment);
		comment.setText(commentText);
	}

	public void setDate(String date){
		TextView dt = itemView.findViewById(R.id.itemCommentDate);
		dt.setText(date);
	}
}
