package com.uni.lu.eventmanager.util;

import android.content.Intent;
import android.net.Uri;


public class Gallery {

	public static final int GALLERY_REQUEST_CODE = 2;

	private Uri urlTemp;

	public Uri getUrlTemp() {
		return urlTemp;
	}

	public void setUrlTemp(Uri urlTemp) {
		this.urlTemp = urlTemp;
	}


	public Intent pickFromGallery() {
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
