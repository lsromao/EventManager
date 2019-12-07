package com.uni.lu.eventmanager.activities.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.Filters;
import com.uni.lu.eventmanager.util.Categories;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

	public static final String TAG = "FilterDialog";

	private DialogInterface.OnDismissListener onDismissListener;

	private View mRootView;

	private Spinner  mCategorySpinner;
	private TextView mEventTitle;
	private Spinner  mSortSpinner;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.dialog_filters, container, false);

		mCategorySpinner = mRootView.findViewById(R.id.spinnerCategory);
		mEventTitle = mRootView.findViewById(R.id.titleFilter);
		mSortSpinner = mRootView.findViewById(R.id.spinnerSort);

		mRootView.findViewById(R.id.buttonSearch).setOnClickListener(this);
		mRootView.findViewById(R.id.buttonCancel).setOnClickListener(this);

		mCategorySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Categories.CategoriesOptions.values()));

		return mRootView;
	}

	private void onSearchClicked() {


		if (mCategorySpinner.getSelectedItem() !=null && !TextUtils.equals(mCategorySpinner.getSelectedItem().toString(), "Select")){
			Filters.getInstance().setCategory(mCategorySpinner.getSelectedItem().toString());
		}

		if (!TextUtils.isEmpty(mEventTitle.getText().toString())){
			Filters.getInstance().setTitle(mEventTitle.getText().toString());
		}

		if (!TextUtils.isEmpty(mSortSpinner.getSelectedItem().toString())){
			Filters.getInstance().setSort(mSortSpinner.getSelectedItem().toString());
		}

		Filters.getInstance().setChanged(true);
		dismiss();

	}

	public void onCancelClicked() {
		Filters.getInstance().setChanged(false);
		dismiss();
	}

	public void resetFilters(){
		if (mCategorySpinner != null && mEventTitle != null && mSortSpinner != null)
		{
			mCategorySpinner.setSelection(0);
			mEventTitle.setText(null);
			mSortSpinner.setSelection(0);
			Filters.getInstance().resetFilter();
		}
	}

	public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
		this.onDismissListener = onDismissListener;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (onDismissListener != null) {
			onDismissListener.onDismiss(dialog);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.buttonSearch:
				onSearchClicked();
				break;
			case R.id.buttonCancel:
				onCancelClicked();
				break;
		}

	}


}
