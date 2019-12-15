package com.uni.lu.micseventmanager.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shivtechs.maplocationpicker.LocationPickerActivity;

public class MapsUtil {

	public static final int LOC_REQ_CODE           = 1;
	public static final int ADDRESS_PICKER_REQUEST = 1020;

	//TODO Refactor
	public Intent getCurrentPlaceItems(Activity activity) {
		Intent maps;
		if (isLocationAccessPermitted(activity)) {
			return showPlacePicker(activity);
		} else {
			requestLocationAccessPermission(activity);
			if (isLocationAccessPermitted(activity)){
				maps = showPlacePicker(activity);
			}else {
				maps = null;
			}
			return maps;
		}
	}

	@SuppressLint("MissingPermission")
	private Intent showPlacePicker(Activity activity) {
		return new Intent(activity, LocationPickerActivity.class);

	}

	private boolean isLocationAccessPermitted(Activity activity) {
		if (ContextCompat.checkSelfPermission(activity,
				Manifest.permission.ACCESS_FINE_LOCATION)
		    != PackageManager.PERMISSION_GRANTED) {
			return false;
		} else {
			return true;
		}
	}

	private void requestLocationAccessPermission(Activity activity) {
		ActivityCompat.requestPermissions(activity,
				new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
				LOC_REQ_CODE);
	}
}
