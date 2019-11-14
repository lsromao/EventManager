package com.uni.lu.eventmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.model.CommentModel;
import com.uni.lu.eventmanager.view.CommentViewHolder;

public class CommentAdapter extends FirestoreRecyclerAdapter<CommentModel, CommentViewHolder> {


	/**
	 * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
	 * FirestoreRecyclerOptions} for configuration options.
	 *
	 * @param options
	 */
	public CommentAdapter(@NonNull FirestoreRecyclerOptions<CommentModel> options) {
		super(options);
	}

	@Override
	protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull CommentModel model) {
		holder.setComment(model.getComment());
		//holder.setProfilePic(model.getUserName());
	}

	@NonNull
	@Override
	public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,
				parent, false);

		return new CommentViewHolder(view);
	}
}