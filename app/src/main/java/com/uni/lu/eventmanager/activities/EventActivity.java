package com.uni.lu.eventmanager.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shivtechs.maplocationpicker.MapUtility;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.adapter.CommentAdapter;
import com.uni.lu.eventmanager.controller.CommentsController;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.controller.LikesController;
import com.uni.lu.eventmanager.media.GlideApp;
import com.uni.lu.eventmanager.model.CommentModel;
import com.uni.lu.eventmanager.model.EventModel;
import com.uni.lu.eventmanager.util.Categories;
import com.uni.lu.eventmanager.util.Gallery;
import com.uni.lu.eventmanager.util.MapsUtil;
import com.uni.lu.eventmanager.util.Privacy;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

	private static final String         TAG = "SaveComments";
	private              CommentAdapter adapter;
	private              EventModel     event;

	private ProgressBar bar;

	private EditText  title;
	private EditText  start;
	private EditText  location;
	private EditText  desc;
	private EditText  cat;
	private EditText  comment;
	private Button    saveComment;
	private Button    editEvent;
	private ImageView iconAccept;
	private ImageView iconCancel;
	private ImageView iconDelete;
	private ImageView iconEditPicture;
	private ImageView like;
	private ImageView cover;
	private ImageView iconCategory;
	private ImageView eventClockIcon;
	private Spinner   categories;
	private Spinner   privacy;
	private LinearLayout startTimeEdit;

	private LikesController likesController;
	private CommentsController commentsController;

	private Gallery gallery;
	private MapsUtil maps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		event = getIntent().getParcelableExtra("Event Model");

		gallery = new Gallery();
		maps = new MapsUtil();

		commentsController = new CommentsController();

		likesController = new LikesController();
		likesController.isLiked(event.getTitle(), event.getUserId());
		like = findViewById(R.id.eventFavoriteIcon);
		likesController.changeIconLike(like, EventActivity.this);


		title = findViewById(R.id.eventTitle);
		start = findViewById(R.id.eventStartTime);
		location = findViewById(R.id.eventLocation);
		desc = findViewById(R.id.eventDescription);
		cat = findViewById(R.id.eventCategory);
		saveComment = findViewById(R.id.saveComment);
		editEvent = findViewById(R.id.editEvent);
		comment = findViewById(R.id.eventAddComments);
		iconAccept = findViewById(R.id.eventIconAccept);
		iconCancel = findViewById(R.id.eventIconCancel);
		iconDelete = findViewById(R.id.eventIconDelete);
		iconCategory = findViewById(R.id.eventCategoryIcon);
		iconEditPicture = findViewById(R.id.iconEditCover);
		categories = findViewById(R.id.categoryEvents);
		privacy = findViewById(R.id.privacyEvents);
		cover = findViewById(R.id.eventCover);
		bar = findViewById(R.id.progressBarEvents);
		startTimeEdit = findViewById(R.id.eventStartDateLayoutEdit);
		eventClockIcon = findViewById(R.id.eventClockIcon);

		categories.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Categories.CategoriesOptions.values()));
		privacy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Privacy.PrivacyOptions.values()));


		editOff();
		loadOriginalInformation();

		bar.setVisibility(View.GONE);

		like.setOnClickListener(this);
		editEvent.setOnClickListener(this);
		iconCancel.setOnClickListener(this);
		iconDelete.setOnClickListener(this);
		iconEditPicture.setOnClickListener(this);
		saveComment.setOnClickListener(this);
		location.setOnClickListener(this);

		setRecyclerView();

		if (event.getUserId().equals(FirebaseController.getInstance().getUserId())) {
			editEvent.setVisibility(View.VISIBLE);
			iconDelete.setVisibility(View.VISIBLE);
		}

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

	private void saveComment() {
		String cc = comment.getText().toString();

		if (cc.length() < 1) {
			Toast.makeText(EventActivity.this, "Please add your comments", Toast.LENGTH_SHORT).show();
		} else {
			bar.setVisibility(View.VISIBLE);
			commentsController.saveComment(this.event, cc)
					.addOnSuccessListener(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void aVoid){
							Log.d(TAG, "Comment saved");
							bar.setVisibility(View.GONE);
							EditText comment = findViewById(R.id.eventAddComments);
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
	}

	private void setRecyclerView() {


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

	private void alertMessageForDelete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name);
		builder.setMessage("Do you want to delete this event?");
		builder.setIcon(R.drawable.ic_danger);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();

			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void editOn() {
		title.setEnabled(true);
		title.setFocusableInTouchMode(true);

		start.setEnabled(true);
		start.setFocusableInTouchMode(true);

		location.setEnabled(true);
		location.setFocusableInTouchMode(true);

		desc.setEnabled(true);
		desc.setFocusableInTouchMode(true);

		iconCategory.setVisibility(View.GONE);
		cat.setVisibility(View.GONE);

		categories.setVisibility(View.VISIBLE);
		privacy.setVisibility(View.VISIBLE);

		iconAccept.setVisibility(View.VISIBLE);
		iconCancel.setVisibility(View.VISIBLE);
		iconEditPicture.setVisibility(View.VISIBLE);

		startTimeEdit.setVisibility(View.VISIBLE);
		eventClockIcon.setVisibility(View.GONE);
		start.setVisibility(View.GONE);
	}

	private void editOff() {
		title.setEnabled(false);
		title.setFocusableInTouchMode(false);

		start.setEnabled(false);
		start.setFocusableInTouchMode(false);

		location.setEnabled(false);
		location.setFocusableInTouchMode(false);

		desc.setEnabled(false);
		desc.setFocusableInTouchMode(false);

		iconCategory.setVisibility(View.VISIBLE);
		cat.setVisibility(View.VISIBLE);

		categories.setVisibility(View.GONE);
		privacy.setVisibility(View.GONE);

		iconAccept.setVisibility(View.GONE);
		iconCancel.setVisibility(View.GONE);
		iconEditPicture.setVisibility(View.GONE);

		startTimeEdit.setVisibility(View.GONE);
		eventClockIcon.setVisibility(View.VISIBLE);
		start.setVisibility(View.VISIBLE);
	}

	private void delete(){
		alertMessageForDelete();
	}

	private void loadOriginalInformation() {
		StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(event.getUriCover());
		GlideApp.with(this)
				.setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_error_sing))
				.load(gsReference).centerCrop()
				.into(cover);

		title.setText(event.getTitle());
		start.setText(event.getStartDate().toString());
		location.setText(event.getLocation());
		desc.setText(event.getDescription());
		cat.setText(event.getCategory());
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK)
			switch (reqCode) {
				case Gallery.GALLERY_REQUEST_CODE:
					Uri selectedImage = data.getData();
					gallery.setUrlTemp(selectedImage);
					GlideApp.with(this)
							.load(selectedImage)
							.apply(new RequestOptions().centerCrop())
							.into(cover);
					break;
				case MapsUtil.ADDRESS_PICKER_REQUEST:
					try {
						if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
							String address = data.getStringExtra(MapUtility.ADDRESS);
							location.setText(address);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					break;
			}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.eventFavoriteIcon:
				likesController.likeEvent(EventActivity.this, event, like);
				break;
			case R.id.eventIconDelete:
				delete();
				break;
			case R.id.editEvent:
				editOn();
				break;
			case R.id.eventIconCancel:
				loadOriginalInformation();
				editOff();
				break;
			case R.id.iconEditCover:
				startActivityForResult(Intent.createChooser(gallery.pickFromGallery(), "Select picture"), Gallery.GALLERY_REQUEST_CODE);
				break;
			case R.id.saveComment:
				saveComment();
				break;
			case R.id.eventLocation:
				maps.getCurrentPlaceItems(this);
				break;
		}
	}
}
