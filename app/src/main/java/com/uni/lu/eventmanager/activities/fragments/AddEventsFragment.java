package com.uni.lu.eventmanager.activities.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.model.EventModel;
import com.uni.lu.eventmanager.util.Categories;
import com.uni.lu.eventmanager.util.DateFormats;

import java.util.Date;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.model.EventModel;
import com.uni.lu.eventmanager.util.Categories;
import com.uni.lu.eventmanager.util.DateFormats;
import com.uni.lu.eventmanager.util.Privacy;

import java.util.Date;

public class AddEventsFragment extends Fragment {

	private static final String TAG                  = "DB Event";
	private static final int    GALLERY_REQUEST_CODE = 2;

    private static final int LOC_REQ_CODE = 1;
    private static final int ADDRESS_PICKER_REQUEST = 1020;
    private final int PICK_IMAGE_REQUEST = 71;

	private DateFormats      dtFormat;
	private TimePickerDialog picker;
	TextView location;
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {

        MapUtility.apiKey = "AIzaSyDfXCxIe_vA2APaofzjGWi_9jKoFmXhE4I";

		final View root = inflater.inflate(R.layout.fragment_add_event, container, false);

		final TextView title       = root.findViewById(R.id.eventTitle);
		final TextView description = root.findViewById(R.id.editDescription);
		final TextView startDate   = root.findViewById(R.id.startDate);
		final TextView endDate     = root.findViewById(R.id.endDate);
		ImageView      cover       = root.findViewById(R.id.eventCover);
		final TextView startTime   = root.findViewById(R.id.startTime);
		final TextView endTime     = root.findViewById(R.id.endTime);
		Button         save        = root.findViewById(R.id.saveEvent);
		final Spinner  categories  = root.findViewById(R.id.categories);
		location    = root.findViewById(R.id.editLocation);
		final Button button = root.findViewById(R.id.select_place);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentPlaceItems();
            }
        });

		dtFormat = new DateFormats();

		startDate.setOnClickListener(new View.OnClickListener() {

		final View root = inflater.inflate(R.layout.fragment_add_event, container, false);

		final TextView title       = root.findViewById(R.id.eventTitle);
		final TextView description = root.findViewById(R.id.editDescription);
		final TextView startDate   = root.findViewById(R.id.startDate);
		ImageView      cover       = root.findViewById(R.id.eventCover);
		final TextView startTime   = root.findViewById(R.id.startTime);
		Button         save        = root.findViewById(R.id.saveEvent);
		final Spinner  categories  = root.findViewById(R.id.categories);
		final Spinner  privacy  = root.findViewById(R.id.privacy);
		final ProgressBar bar = root.findViewById(R.id.progressLoading);
		final TextView location = root.findViewById(R.id.editLocation);

		dtFormat = new DateFormats();

		cover.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pickFromGallery();
			}
		});

		startDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				final Calendar calendar = Calendar.getInstance();
				int            yy       = calendar.get(Calendar.YEAR);
				int            mm       = calendar.get(Calendar.MONTH);
				int            dd       = calendar.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						String dt = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
						startDate.setText(dtFormat.getDateTimeFormatted(dt, DateFormats.DATE));
					}
				}, yy, mm, dd);
				datePicker.show();
			}
		});



		startTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Calendar cldr    = Calendar.getInstance();
				int            hour    = cldr.get(Calendar.HOUR_OF_DAY);
				int            minutes = cldr.get(Calendar.MINUTE);
				// time picker dialog
				picker = new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
								startTime.setText(dtFormat.getDateTimeFormatted(sHour + ":" + sMinute, DateFormats.TIME));
							}
						}, hour, minutes, true);
				picker.show();
			}
		});


		categories.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Categories.CATEGORIES.values()));
		privacy.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Privacy.privacy.values()));


		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String startDt = startDate.getText().toString();
				String startTm = startTime.getText().toString();
				String tt      = title.getText().toString();
				String desc    = description.getText().toString();
				String cat     = categories.getSelectedItem().toString();
				String priv  = privacy.getSelectedItem().toString();
				String loc  = location.getText().toString();
				Date today = new Date();

				if (tt.length() < 1) {
					Toast.makeText(getActivity(), "Title cannot be empty!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Title Empty");
				} else if (desc.length() < 1) {
					Toast.makeText(getActivity(), "Description cannot be empty or less than 10 chars!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Description Error!");
				} else if (startDt.length() < 1) {
					Toast.makeText(getActivity(), "Start Date cannot be empty!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Start Date empty!");
				} else if (startTm.length() < 1) {
					Toast.makeText(getActivity(), "Start Time cannot be empty!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Start Time empty!");
				} else if (loc.length() < 1) {
					Toast.makeText(getActivity(), "Location cannot be empty!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Location empty!");
				} else if (cat.equals("Select")) {
					Toast.makeText(getActivity(), "Please select a category!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Category not select!");
				} else {
					Date start = dtFormat.getDateTime(startDt, startTm);
					if (start.before(today)){
						Toast.makeText(getActivity(), "Start date cannot be in the past!", Toast.LENGTH_SHORT).show();
						Log.w(TAG, "Error Event: Start date in the past");
					}else{
						createEvent(tt, desc, cat, loc, priv.equals("Public") ? true : false, start, today, bar);

					}
				}
			}
		});

		categories.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Categories.CATEGORIES.values()));




		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String startDt = startDate.getText().toString();
				String endDt   = endDate.getText().toString();
				String startTm = startTime.getText().toString();
				String endTm   = endTime.getText().toString();
				String tt      = title.getText().toString();
				String desc    = description.getText().toString();
				String cat     = categories.getSelectedItem().toString();
				String geo     = location.getText().toString();

				if (tt.length() < 1) {
					Toast.makeText(getActivity(), "Title cannot be empty!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Title Empty");
				} else if (desc.length() < 10) {
					Toast.makeText(getActivity(), "Description cannot be empty or less than 10 chars!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Description Error!");
				} else if (startDt.length() < 1) {
					Toast.makeText(getActivity(), "Start Date cannot be empty!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Start Date empty!");
				} else if (endDt.length() < 1) {
					Toast.makeText(getActivity(), "End Date cannot be empty!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: End Date empty!");
				} else if (startTm.length() < 1) {
					Toast.makeText(getActivity(), "Start Time cannot be empty!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Start Time empty!");
				} else if (endTm.length() < 1) {
					Toast.makeText(getActivity(), "End Time cannot be empty!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: End Time empty!");
				} else if (cat.equals("Select")) {
					Toast.makeText(getActivity(), "Please select a category!", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Error Event: Category not select!");
				} else {
					Date start = dtFormat.getDateTime(startDt, startTm);
					Date end   = dtFormat.getDateTime(endDt, endTm);
					createEvent(tt, desc, start, end, true);
				}
			}
		});


		return root;
	}

    private void getCurrentPlaceItems() {
        if (isLocationAccessPermitted()) {
            showPlacePicker();
        } else {
            requestLocationAccessPermission();
        }
    }

    @SuppressLint("MissingPermission")
    private void showPlacePicker() {
        Intent i = new Intent(getContext(), LocationPickerActivity.class);
        startActivityForResult(i, ADDRESS_PICKER_REQUEST);

    }

    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_REQ_CODE);
    }

	private void createEvent(String title, String description, Date startTime, Date endTime, boolean privacy) {

		dtFormat = new DateFormats();
		Toast.makeText(getActivity(), dtFormat.validateDates(startTime,endTime), Toast.LENGTH_SHORT).show();

		String            id    = FirebaseController.getInstance().getmAuth().getCurrentUser().getUid();
		FirebaseFirestore db    = FirebaseFirestore.getInstance();
		EventModel        event = new EventModel(title, description, startTime, endTime, location.getText().toString(), true, id, "", "");

		db.collection("events")
				.add(event)
				.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
					@Override
					public void onSuccess(DocumentReference documentReference) {
						Toast.makeText(getActivity(), "Save in Database!", Toast.LENGTH_SHORT).show();
						Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(getActivity(), "Error to save in Database!", Toast.LENGTH_SHORT).show();
						Log.w(TAG, "Error adding document", e);
					}
				});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {


		if (requestCode == ADDRESS_PICKER_REQUEST) {
			try {
				if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
					String address = data.getStringExtra(MapUtility.ADDRESS);
					double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
					double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
					location.setText(address);
//					eventLocation.setText(address);
//					location = new EventLocation(address,currentLatitude,currentLongitude);

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	private void createEvent(final String title, final  String description, final String category, final String location,
	                         final boolean privacy, final Date startTime, final Date created, final ProgressBar bar) {

		//Image to FireStrore
		StorageReference storageRef    = FirebaseStorage.getInstance().getReference();
		StorageReference eventCoverRef = storageRef.child("event/" + title.replaceAll("\\s+", "") + startTime.hashCode());
		bar.setVisibility(View.VISIBLE);
		eventCoverRef.putFile(tempUrl)
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception exception) {
						Log.w(TAG, "createEvent:failure", exception);

					}
				}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
						String tent = taskSnapshot.getMetadata().getReference().toString();
						//String eventCover = eventCoverRef.getName();
						saveInFirebase(title,description, category, location, privacy, startTime, created, tent, bar);
						Log.w(TAG, "createEvent:success");
					}
		});


	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		// Result code is RESULT_OK only if the user selects an Image
		if (resultCode == Activity.RESULT_OK)
			switch (reqCode) {
				case GALLERY_REQUEST_CODE:
					//data.getData returns the content URI for the selected Image
					Uri selectedImage = data.getData();
					tempUrl = selectedImage;
					ImageView cover = getActivity().findViewById(R.id.eventCover);
					Glide.with(this)
							.load(selectedImage)
							.apply(new RequestOptions().centerCrop().fitCenter())
							.into(cover);
					break;

			}
	}

	private void pickFromGallery() {
		//Create an Intent with action as ACTION_PICK
		Intent intent = new Intent(Intent.ACTION_PICK);
		// Sets the type as image/*. This ensures only components of type image are selected
		intent.setType("image/*");
		//We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
		String[] mimeTypes = {"image/jpeg", "image/png"};
		intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
		// Launching the Intent
		startActivityForResult(intent, GALLERY_REQUEST_CODE);
	}

	private void saveInFirebase(String title, String description, String category, String location, boolean privacy, Date startTime, Date created, String uri, final ProgressBar bar){

		String            id    = FirebaseController.getInstance().getmAuth().getCurrentUser().getUid();
		FirebaseFirestore db    = FirebaseFirestore.getInstance();
		EventModel        event = new EventModel(title, description, category, location, privacy, uri, id, startTime, created);

		db.collection("events")
				.add(event)
				.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
					@Override
					public void onSuccess(DocumentReference documentReference) {
						Toast.makeText(getActivity(), "Save in Database!", Toast.LENGTH_SHORT).show();
						Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
						bar.setVisibility(View.GONE);
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(getActivity(), "Error to save in Database!", Toast.LENGTH_SHORT).show();
						Log.w(TAG, "Error adding document", e);
					}
				});
	}
}