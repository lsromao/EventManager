package com.uni.lu.eventmanager.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.adapter.CommentAdapter;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.media.GlideApp;
import com.uni.lu.eventmanager.model.CommentModel;
import com.uni.lu.eventmanager.model.EventModel;
import com.uni.lu.eventmanager.model.LikeModel;

public class EventActivity extends AppCompatActivity {

	private static final String TAG = "Save Comments" ;
	private CommentAdapter adapter;
	private EventModel event;

	private ProgressBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		event = getIntent().getParcelableExtra("Event Model");

		TextView       title       = findViewById(R.id.eventTitle);
		TextView       start       = findViewById(R.id.eventStartTime);
		TextView       location    = findViewById(R.id.eventLocation);
		TextView       desc        = findViewById(R.id.eventDescription);
		final Button   saveComment = findViewById(R.id.saveComment);
		final EditText comment     = findViewById(R.id.eventAddComments);

		final ImageView like = findViewById(R.id.eventFavoriteIcon);
		ImageView cover = findViewById(R.id.eventCover);
		bar = findViewById(R.id.progressBarEvents);
		bar.setVisibility(View.GONE);

		final boolean isLiked = checkLike();

		if (isLiked){
			GlideApp.with(EventActivity.this)
					.load(R.drawable.ic_liked)
					.into(like);
		} else {
			GlideApp.with(EventActivity.this)
					.load(R.drawable.ic_like)
					.into(like);
		}

		like.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean check = checkLike();
				//TODO Add a better validation - Like will be blank after events changes
				if (check){
					FirebaseController.getInstance().getLikesCollectionReference().document(
							(event.getDocName() + FirebaseController.getInstance().getUserName()).replaceAll("\\s+", ""))
							.delete();
					GlideApp.with(EventActivity.this)
							.load(R.drawable.ic_like)
							.into(like);
				}else{
					LikeModel likeModel = new LikeModel(event.getCategory(), event.getDocName());
					FirebaseController.getInstance().getLikesCollectionReference().document(
							(event.getDocName() + FirebaseController.getInstance().getUserName()).replaceAll("\\s+", ""))
							.set(likeModel);
					GlideApp.with(EventActivity.this)
							.load(R.drawable.ic_liked)
							.into(like);
				}
			}
		});

		setRecyclerView();

		StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(event.getUriCover());
		GlideApp.with(this)
				.setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_error_sing))
				.load(gsReference).centerCrop().fitCenter()
				.into(cover);

		title.setText(event.getTitle());
		start.setText(event.getStartDate().toString());
		location.setText(event.getLocation());
		desc.setText(event.getDescription());

		saveComment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String cc = comment.getText().toString();
				if (cc.length() < 1){
					Toast.makeText( EventActivity.this, "Please add your comments", Toast.LENGTH_SHORT).show();
				}else {
					bar.setVisibility(View.VISIBLE);
					saveComment(cc);
				}
			}
		});


	}

	@Override
	public void onStart() {
		super.onStart();
		adapter.startListening();
	}

	@Override
	public void onStop() {
		super.onStop();
		adapter.stopListening();
	}

	private void saveComment(String cc){
		CommentModel commentModel = new CommentModel(cc, event.getDocName());

		FirebaseController.getInstance().getFirestoreInstance().collection("comments")
				.add(commentModel)
				.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
					@Override
					public void onSuccess(DocumentReference documentReference) {
						Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
						bar.setVisibility(View.GONE);
						EditText comment     = findViewById(R.id.eventAddComments);
						comment.setText(null);
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(EventActivity.this, "Error to save in Database!", Toast.LENGTH_SHORT).show();
						Log.w(TAG, "Error adding document", e);
					}
				});

	}

	private void setRecyclerView(){


		Query query = FirebaseController.getInstance().getCommentsCollectionReference()
				.whereEqualTo("eventDocument", event.getDocName())
				.orderBy("date", Query.Direction.DESCENDING);

		FirestoreRecyclerOptions<CommentModel> options =
				new FirestoreRecyclerOptions.Builder<CommentModel>()
						.setQuery(query, CommentModel.class)
						.build();

		adapter = new CommentAdapter(options, this);

		RecyclerView recyclerView = findViewById(R.id.recyclerComments);
		recyclerView.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);
	}

	private boolean checkLike(){
		Query query =FirebaseController.getInstance().getLikesCollectionReference()
				.whereEqualTo("eventDocument", event.getDocName())
				.whereEqualTo("userId", event.getUserId());

		Task<QuerySnapshot> r = query.get();
		//TODO Add better task handleing
		while (!r.isComplete()){

		}

		return r.getResult().getDocuments().size() !=0;
	}
}
