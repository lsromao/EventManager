package com.uni.lu.micseventmanager.view;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uni.lu.micseventmanager.R;
import com.uni.lu.micseventmanager.media.GlideApp;

public class CommentViewHolder extends RecyclerView.ViewHolder {
	private View itemView;
	private Activity activity;


	public CommentViewHolder(@NonNull View itemView, Activity activity) {
		super(itemView);
		this.itemView = itemView;
		this.activity = activity;
	}

	public void setProfilePic(String uri){
		ImageView                    profile     = itemView.findViewById(R.id.itemProfilePic);
		if (uri.contains("gs://eventmanager-misc.appspot.com")){
			StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(uri);
			GlideApp.with(activity)
					.setDefaultRequestOptions(new RequestOptions()
							.fitCenter()
							.circleCrop()
							.error(R.drawable.ic_user)
							.placeholder(R.drawable.ic_user))
					.load(gsReference)
					.into(profile);
		}else{
			GlideApp.with(activity)
					.setDefaultRequestOptions(new RequestOptions()
							.fitCenter()
							.circleCrop()
							.error(R.drawable.ic_user)
							.placeholder(R.drawable.ic_user))
					.load(uri)
					.into(profile);
		}

	}

	public void setComment(String commentText){
		TextView comment = itemView.findViewById(R.id.itemComment);
		comment.setText(commentText);
	}

	public void setName(String userName){
		TextView name = itemView.findViewById(R.id.itemCommentName);
		name.setText(userName);
	}

	public void setDate(String date){
		TextView dt = itemView.findViewById(R.id.itemCommentDate);
		dt.setText(date);
	}
}
