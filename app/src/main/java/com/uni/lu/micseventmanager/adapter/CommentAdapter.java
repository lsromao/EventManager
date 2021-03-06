package com.uni.lu.micseventmanager.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.uni.lu.micseventmanager.R;
import com.uni.lu.micseventmanager.model.CommentModel;
import com.uni.lu.micseventmanager.view.CommentViewHolder;

public class CommentAdapter extends FirestoreRecyclerAdapter<CommentModel, CommentViewHolder> {


	private Activity activity;

	/**
	 * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
	 * FirestoreRecyclerOptions} for configuration options.
	 *
	 * @param options
	 */
	public CommentAdapter(@NonNull FirestoreRecyclerOptions<CommentModel> options, Activity activity) {
		super(options);
		this.activity=activity;

	}

	@Override
	protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull CommentModel model) {
		holder.setComment(model.getComment());
		//TODO Check
		holder.setProfilePic(model.getUserPic());
		holder.setDate(model.getDate().toString());
		holder.setName(model.getUserName());
	}

	@NonNull
	@Override
	public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,
				parent, false);

		return new CommentViewHolder(view, activity);
	}
}
