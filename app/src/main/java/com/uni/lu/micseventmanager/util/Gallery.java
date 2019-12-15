package com.uni.lu.micseventmanager.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Gallery {

	public static final int GALLERY_REQUEST_CODE = 2;

	private Uri urlTemp;

	public Uri getUrlTemp() {
		return urlTemp;
	}

	public void setUrlTemp(Uri urlTemp) {
		this.urlTemp = urlTemp;
	}


	public Intent pickFromGallery(Activity activity) {
		if (isStorageAccessPermitted(activity)){
			return createIntent();
		}else {
			Intent intent = null;
			requestStorageAccessPermission(activity);
			if (isStorageAccessPermitted(activity)){
				intent = createIntent();
			}
			return intent;
		}

	}

	private void requestStorageAccessPermission(Activity activity) {
		ActivityCompat.requestPermissions(activity,
				new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
				GALLERY_REQUEST_CODE);
	}

	private boolean isStorageAccessPermitted(Activity activity) {
		if (ContextCompat.checkSelfPermission(activity,
				Manifest.permission.READ_EXTERNAL_STORAGE)
		    != PackageManager.PERMISSION_GRANTED) {
			return false;
		} else {
			return true;
		}
	}

	private Intent createIntent(){
		//Create an Intent with action as ACTION_PICK
		Intent intent = new Intent(Intent.ACTION_PICK);
		// Sets the type as image/*. This ensures only components of type image are selected
		intent.setType("image/*");
		//We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
		String[] mimeTypes = {"image/jpeg", "image/png"};
		intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
		// Launching the Intent
		return intent;
	}
}
