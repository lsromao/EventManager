package com.uni.lu.eventmanager.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.adapter.CommentAdapter;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.model.CommentModel;
import com.uni.lu.eventmanager.model.EventModel;

public class EventActivity extends AppCompatActivity {

	private static final String TAG = "Save Comments" ;
	private CommentAdapter adapter;
	FirebaseFirestore db =FirebaseFirestore.getInstance();
	private CollectionReference eventsCollection =  db.collection("events");
	private CollectionReference commentsCollection =  db.collection("comments");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		EventModel event = getIntent().getParcelableExtra("Event Model");

		TextView       title       = findViewById(R.id.eventTitle);
		TextView       start       = findViewById(R.id.startEvent);
		TextView       location    = findViewById(R.id.locationEvent);
		TextView       desc        = findViewById(R.id.descriptionEvent);
		final Button   saveComment = findViewById(R.id.saveComment);
		final EditText comment     = findViewById(R.id.addComment);

		set();



		title.setText(event.getTitle());
		start.setText(event.getStartDate().toString());
		location.setText(event.getLocation());
		desc.setText(event.getDescription());

		final ImageView like = findViewById(R.id.favorite);

		like.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				like.setImageResource(R.drawable.ic_liked);
			}
		});

		saveComment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String cc = comment.getText().toString();
				if (cc.length() < 1){
					Toast.makeText( EventActivity.this, "Please add your comments", Toast.LENGTH_SHORT).show();
				}else {
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
		CommentModel commentModel = new CommentModel(cc);


		FirebaseController.getInstance().getFirestoreInstance().collection("comments")
				.add(commentModel)
				.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
					@Override
					public void onSuccess(DocumentReference documentReference) {
						Toast.makeText(EventActivity.this, "Save in Database!", Toast.LENGTH_SHORT).show();
						Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
						//bar.setVisibility(View.GONE);
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

	private void set(){


		Query query = commentsCollection;


		FirestoreRecyclerOptions<CommentModel> options =
				new FirestoreRecyclerOptions.Builder<CommentModel>()
						.setQuery(query, CommentModel.class)
						.build();


		adapter = new CommentAdapter(options);

		RecyclerView recyclerView = findViewById(R.id.commentsView);
		recyclerView.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);




	}
}
