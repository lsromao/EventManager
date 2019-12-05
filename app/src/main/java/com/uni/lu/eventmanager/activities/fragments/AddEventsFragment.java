package com.uni.lu.eventmanager.activities.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.shivtechs.maplocationpicker.MapUtility;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.EventsController;
import com.uni.lu.eventmanager.controller.FireStorageController;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.media.GlideApp;
import com.uni.lu.eventmanager.model.EventModel;
import com.uni.lu.eventmanager.util.Categories;
import com.uni.lu.eventmanager.util.DateUtils;
import com.uni.lu.eventmanager.util.Gallery;
import com.uni.lu.eventmanager.util.MapsUtil;
import com.uni.lu.eventmanager.util.Privacy;

import java.util.Random;

public class AddEventsFragment extends Fragment implements View.OnClickListener {

	private static final String TAG = "DB Event";

	private Gallery gallery = new Gallery();

	private DateUtils dtFormat;

	private TextView title;
	private TextView description;
	private TextView location;
	private TextView startDate;
	private TextView startTime;

	private Button save;

	private Spinner categories;
	private Spinner privacy;

	private ProgressBar bar;

	private View root;

	private EventsController      eventsController;
	private FireStorageController fireStorageController;

	private MapsUtil maps;


	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {

		eventsController = new EventsController();
		fireStorageController = new FireStorageController();
		maps = new MapsUtil();

		MapUtility.apiKey = "AIzaSyDfXCxIe_vA2APaofzjGWi_9jKoFmXhE4I";

		root = inflater.inflate(R.layout.fragment_add_event, container, false);

		//Variables
		title = root.findViewById(R.id.titleEventAddEvents);
		description = root.findViewById(R.id.descriptionAddEvents);
		save = root.findViewById(R.id.saveEvent);
		location = root.findViewById(R.id.locationAddEvents);
		startDate = root.findViewById(R.id.startDateAddEvents);
		startTime = root.findViewById(R.id.startTimeAddEvents);
		categories = root.findViewById(R.id.categoryEvents);
		privacy = root.findViewById(R.id.privacyEvents);

		//Setting Listeners
		root.findViewById(R.id.coverAddEvents).setOnClickListener(this);
		location.setOnClickListener(this);
		startDate.setOnClickListener(this);
		startTime.setOnClickListener(this);
		save.setOnClickListener(this);

		dtFormat = new DateUtils();

		bar = root.findViewById(R.id.progressBarAddAEvents);
		bar.setVisibility(View.GONE);

		dtFormat = new DateUtils();

		categories.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Categories.CategoriesOptions.values()));
		privacy.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Privacy.PrivacyOptions.values()));

		return root;
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK)
			switch (reqCode) {
				case Gallery.GALLERY_REQUEST_CODE:
					Uri selectedImage = data.getData();
					gallery.setUrlTemp(selectedImage);
					ImageView cover = getActivity().findViewById(R.id.coverAddEvents);
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
			case R.id.coverAddEvents:
				startActivityForResult(Intent.createChooser(gallery.pickFromGallery(), "Select picture"), Gallery.GALLERY_REQUEST_CODE);
				break;
			case R.id.locationAddEvents:
				Intent map = maps.getCurrentPlaceItems(getActivity());
				if (map != null){
					startActivityForResult(maps.getCurrentPlaceItems(getActivity()), MapsUtil.ADDRESS_PICKER_REQUEST);
				}
				break;
			case R.id.startDateAddEvents:
				selectDateTimePicker(DateUtils.DATE);
				break;
			case R.id.startTimeAddEvents:
				selectDateTimePicker(DateUtils.TIME);
				break;
			case R.id.saveEvent:
				createEvent();
		}
	}

	private void selectDateTimePicker(int code) {
		TimePickerDialog picker;
		final Calendar   calendar = Calendar.getInstance();

		if (code == DateUtils.DATE) {
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					String dt = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
					startDate.setText(dtFormat.getDateTimeFormatted(dt, DateUtils.DATE));
				}
			}, yy, mm, dd);
			datePicker.show();
		} else if (code == DateUtils.TIME) {
			int hour    = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);

			// time picker dialog
			picker = new TimePickerDialog(getActivity(),
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
							startTime.setText(dtFormat.getDateTimeFormatted(sHour + ":" + sMinute, DateUtils.TIME));
						}
					}, hour, minutes, true);
			picker.show();
		}
	}

	private void createEvent() {

		EventModel event = new EventModel(
				"events-" + + new Random().nextInt(200000),
				title.getText().toString(),
				description.getText().toString(),
				categories.getSelectedItem().toString(),
				location.getText().toString(),
				!privacy.getSelectedItem().toString().equals("Public"),
				gallery.getUrlTemp().toString(),
				FirebaseController.getInstance().getUserId(),
				null,
				null
		);

		if (eventsController.eventValidation(getActivity(), event, startDate.getText().toString(), startTime.getText().toString())) {
			bar.setVisibility(View.VISIBLE);
			fireStorageController.saveCoverPicture(event, gallery);
			eventsController.saveEvent()
					.addOnSuccessListener(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void aVoid) {
							bar.setVisibility(View.GONE);
							getFragmentManager().popBackStackImmediate();
						}
					})
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							bar.setVisibility(View.GONE);
							Toast.makeText(getActivity(), "Error to save in Database!", Toast.LENGTH_SHORT).show();
							Log.w(TAG, "Error adding document", e);
						}
					});
		}
	}
}