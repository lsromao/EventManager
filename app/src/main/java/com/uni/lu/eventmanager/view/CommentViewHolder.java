package com.uni.lu.eventmanager.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.lu.eventmanager.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
	private View itemView;

	public CommentViewHolder(@NonNull View itemView) {
		super(itemView);
		this.itemView = itemView;
	}

	public void setProfilePic(String uri){
		ImageView profile = itemView.findViewById(R.id.profilePic);
	}

	public void setComment(String commentText){
		TextView comment = itemView.findViewById(R.id.commentText);
		comment.setText(commentText);
	}
}
