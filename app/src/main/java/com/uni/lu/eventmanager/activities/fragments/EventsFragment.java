package com.uni.lu.eventmanager.activities.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.activities.EventActivity;
import com.uni.lu.eventmanager.adapter.EventAdapter;
import com.uni.lu.eventmanager.adapter.OnItemListener;
import com.uni.lu.eventmanager.controller.Filters;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.model.EventModel;


public class EventsFragment extends Fragment implements OnItemListener, View.OnClickListener  {

	private RecyclerView        recyclerView;
	private LinearLayoutManager linearLayoutManager;
	private EventAdapter        adapter;

	private CardView filterBar;
	private ImageView resetFilter;
	private FilterDialogFragment mFilterDialog;
	private TextView filterDescription;

	private LinearLayout emptyView;

	private View root;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.fragment_events, container, false);

		filterBar = root.findViewById(R.id.filterBar);
		resetFilter = root.findViewById(R.id.buttonClearFilter);
		filterDescription = root.findViewById(R.id.textCurrentSearch);
		emptyView = root.findViewById(R.id.viewEmpty);
		filterBar.setOnClickListener(this);
		resetFilter.setOnClickListener(this);

		setRecyclerView();

		// Filter Dialog
		mFilterDialog = new FilterDialogFragment();
		mFilterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (Filters.getInstance().isChanged()){
					updateRecyclerView();
					filterDescription.setText("Custom search");
				}
			}
		});

		return root;
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


	private void setRecyclerView() {

		//TODO Abstract for DAOs
		Query query = FirebaseController.getInstance().getEventsCollectionReference()
				.whereEqualTo("privacy", false)
				.orderBy("startDate", Query.Direction.DESCENDING);

		FirestoreRecyclerOptions<EventModel> options =
				new FirestoreRecyclerOptions.Builder<EventModel>()
						.setQuery(Filters.getInstance().buildQuery(), EventModel.class)
						.build();


		adapter = new EventAdapter(options, this){
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

		recyclerView = root.findViewById(R.id.recyclerItems);
		recyclerView.setHasFixedSize(true);
		linearLayoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);
	}

	private void updateRecyclerView(){

		FirestoreRecyclerOptions<EventModel> options =
				new FirestoreRecyclerOptions.Builder<EventModel>()
						.setQuery(Filters.getInstance().buildQuery(), EventModel.class)
						.build();


		adapter = new EventAdapter(options, this) {
			@Override
			public void onDataChanged() {
				// Show/hide content if the query returns empty.
				int a = getItemCount();
				if (getItemCount() == 0) {
					recyclerView.setVisibility(View.GONE);
					emptyView.setVisibility(View.VISIBLE);
				} else {
					recyclerView.setVisibility(View.VISIBLE);
					emptyView.setVisibility(View.GONE);
				}
			}

			@Override
			public void onError(FirebaseFirestoreException e) {
				super.onError(e);
				int a = 0;
			}
		};
		recyclerView.setAdapter(adapter);
		adapter.startListening();
	}

	public void onFilterClicked() {
		// Show the dialog containing filter options
		mFilterDialog.show(getFragmentManager(), FilterDialogFragment.TAG);

	}

	private void resetFilter() {
		filterDescription.setText("All Events");
		mFilterDialog = new FilterDialogFragment();
		if (Filters.getInstance().isChanged()){
			updateRecyclerView();
			Filters.getInstance().setChanged(false);
		}

	}

	@Override
	public void onEventClick(View view, int position) {
		EventModel event = adapter.getSnapshots().get(position);
		Intent start = new Intent(getActivity(), EventActivity.class);
		start.putExtra("Event Model", event);
		startActivity(start);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.filterBar:
				onFilterClicked();
				break;
			case R.id.buttonClearFilter:
				resetFilter();
				break;
		}
	}


}