package com.uni.lu.micseventmanager.activities;

import android.app.Activity;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shivtechs.maplocationpicker.MapUtility;
import com.uni.lu.micseventmanager.R;
import com.uni.lu.micseventmanager.adapter.CommentAdapter;
import com.uni.lu.micseventmanager.controller.CommentsController;
import com.uni.lu.micseventmanager.controller.EventsController;
import com.uni.lu.micseventmanager.controller.FireStorageController;
import com.uni.lu.micseventmanager.controller.FirebaseController;
import com.uni.lu.micseventmanager.controller.LikesController;
import com.uni.lu.micseventmanager.media.GlideApp;
import com.uni.lu.micseventmanager.model.CommentModel;
import com.uni.lu.micseventmanager.model.EventModel;
import com.uni.lu.micseventmanager.util.Categories;
import com.uni.lu.micseventmanager.util.DateUtils;
import com.uni.lu.micseventmanager.util.Gallery;
import com.uni.lu.micseventmanager.util.MapsUtil;
import com.uni.lu.micseventmanager.util.Privacy;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

	private static final String         TAG = "SaveComments";
	private              CommentAdapter adapter;
	private              EventModel     event;

	private ProgressBar bar;

	private EditText     title;
	private EditText     start;
	private EditText     location;
	private EditText     desc;
	private EditText     cat;
	private EditText     comment;
	private TextView     startDate;
	private TextView     startTime;
	private Button       saveComment;
	private Button       editEvent;
	private ImageView    iconAccept;
	private ImageView    iconCancel;
	private ImageView    iconEditPicture;
	private ImageView    like;
	private ImageView    cover;
	private ImageView    iconCategory;
	private ImageView    eventClockIcon;
	private Spinner      categories;
	private Spinner      privacy;
	private LinearLayout startTimeEdit;
	private RecyclerView recyclerView;
	private LinearLayout emptyView;

	private LikesController       likesController;
	private CommentsController    commentsController;
	private EventsController      eventsController;
	private FireStorageController fireStorageController;


	private Gallery  gallery;
	private MapsUtil maps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		event = getIntent().getParcelableExtra("Event Model");

		gallery = new Gallery();
		maps = new MapsUtil();

		commentsController = new CommentsController();
		eventsController = new EventsController();
		fireStorageController = new FireStorageController();

		likesController = new LikesController();
		likesController.isLiked(event.getTitle());
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
		iconCategory = findViewById(R.id.eventCategoryIcon);
		iconEditPicture = findViewById(R.id.iconEditCover);
		categories = findViewById(R.id.categoryEvents);
		privacy = findViewById(R.id.privacyEvents);
		cover = findViewById(R.id.eventCover);
		bar = findViewById(R.id.progressBarEvents);
		startTimeEdit = findViewById(R.id.eventStartDateLayoutEdit);
		startDate = findViewById(R.id.startDateEvents);
		startTime = findViewById(R.id.startTimeEvents);
		eventClockIcon = findViewById(R.id.eventClockIcon);
		emptyView = findViewById(R.id.emptyViewComments);

		categories.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Categories.CategoriesOptions.values()));
		privacy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Privacy.PrivacyOptions.values()));


		editOff();
		loadOriginalInformation();

		bar.setVisibility(View.GONE);

		like.setOnClickListener(this);
		editEvent.setOnClickListener(this);
		iconAccept.setOnClickListener(this);
		iconCancel.setOnClickListener(this);
		iconEditPicture.setOnClickListener(this);
		saveComment.setOnClickListener(this);
		location.setOnClickListener(this);
		startDate.setOnClickListener(this);
		startTime.setOnClickListener(this);

		setRecyclerView();

		if (event.getUserId().equals(FirebaseController.getInstance().getUserId())) {
			editEvent.setVisibility(View.VISIBLE);
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
						public void onSuccess(Void aVoid) {
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

		adapter = new CommentAdapter(options, this) {
			@Override
			public void onDataChanged() {
				// Show/hide content if the query returns empty.
				if (getItemCount() == 0) {
					recyclerView.setVisibility(View.GONE);
					emptyView.setVisibility(View.VISIBLE);
				} else {
					recyclerView.setVisibility(View.VISIBLE);
					emptyView.setVisibility(View.GONE);
				}
			}
		};

		recyclerView = findViewById(R.id.recyclerComments);
		recyclerView.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);
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

		cat.setEnabled(false);
		cat.setFocusableInTouchMode(false);

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

	private void selectDateTimePicker(int code) {
		eventsController.getDtFormat().selectDateTimePicker(code, startDate, startTime, this);
	}

	private void saveChanges() {
		boolean isNewCover = true;
		if (gallery.getUrlTemp() == null) {
			gallery.setUrlTemp(Uri.parse(event.getUriCover()));
			isNewCover = false;
		}

		final EventModel eventMdl = new EventModel(
				event.getDocName(),
				title.getText().toString(),
				desc.getText().toString(),
				categories.getSelectedItem().toString(),
				location.getText().toString(),
				!privacy.getSelectedItem().toString().equals("Public"),
				gallery.getUrlTemp().toString(),
				FirebaseController.getInstance().getUserId(),
				null,
				null
		);

		if (eventsController.eventValidation(this, eventMdl, startDate.getText().toString(), startTime.getText().toString())) {
			bar.setVisibility(View.VISIBLE);
			if (isNewCover) {
				fireStorageController.saveCoverPicture(eventMdl, gallery);
			}
			eventsController.saveEvent()
					.addOnSuccessListener(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void aVoid) {
							bar.setVisibility(View.GONE);
							Toast.makeText(EventActivity.this, "Update in Database!", Toast.LENGTH_SHORT).show();
							event = eventMdl;
							event.setStartDate(eventsController.getEvent().getStartDate());
							editOff();
						}
					})
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							bar.setVisibility(View.GONE);
							Toast.makeText(EventActivity.this, "Error to update in Database!", Toast.LENGTH_SHORT).show();
							Log.w(TAG, "Error adding document", e);
						}
					});
		}


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
			case R.id.editEvent:
				editOn();
				break;
			case R.id.eventIconCancel:
				loadOriginalInformation();
				editOff();
				break;
			case R.id.eventIconAccept:
				saveChanges();
				break;
			case R.id.iconEditCover:
				Intent storage = gallery.pickFromGallery(this);
				if (storage != null){
					startActivityForResult(Intent.createChooser(storage, "Select picture"), Gallery.GALLERY_REQUEST_CODE);
				}
				break;
			case R.id.saveComment:
				saveComment();
				break;
			case R.id.eventLocation:
				Intent map = maps.getCurrentPlaceItems(this);
				if (map != null) {
					startActivityForResult(maps.getCurrentPlaceItems(this), MapsUtil.ADDRESS_PICKER_REQUEST);
				}
				break;
			case R.id.startDateEvents:
				selectDateTimePicker(DateUtils.DATE);
				break;
			case R.id.startTimeEvents:
				selectDateTimePicker(DateUtils.TIME);
				break;
		}
	}
}
