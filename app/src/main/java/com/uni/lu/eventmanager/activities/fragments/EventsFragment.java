package com.uni.lu.eventmanager.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.activities.EventActivity;
import com.uni.lu.eventmanager.adapter.EventAdapter;
import com.uni.lu.eventmanager.adapter.OnItemListener;
import com.uni.lu.eventmanager.model.EventModel;
import com.uni.lu.eventmanager.view.EventsView;


public class EventsFragment extends Fragment implements OnItemListener {

	private EventsView          eventsView;
	private RecyclerView        recyclerView;
	private LinearLayoutManager linearLayoutManager;
	private EventAdapter        adapter;
	FirebaseFirestore db =FirebaseFirestore.getInstance();
	private CollectionReference mDatabase =  db.collection("events");
	private FilterDialogFragment mFilterDialog;


	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {

		eventsView = ViewModelProviders.of(this).get(EventsView.class);

		View root = inflater.inflate(R.layout.fragment_events, container, false);

		set();

		recyclerView = root.findViewById(R.id.recyclerItems);
		recyclerView.setHasFixedSize(true);
		linearLayoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);

		// Filter Dialog
		mFilterDialog = new FilterDialogFragment();

		CardView filter = root.findViewById( R.id.filterBar);

		filter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mFilterDialog.show(getActivity().getSupportFragmentManager(), FilterDialogFragment.TAG);
			}
		});


		int i = adapter.getSnapshots().size();

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

	private void set() {
		//db = FirebaseFirestore.getInstance();
		//mDatabase = db.collection("events");
		Query query = mDatabase;

		FirestoreRecyclerOptions<EventModel> options =
				new FirestoreRecyclerOptions.Builder<EventModel>()
						.setQuery(query, EventModel.class)
						.build();


		adapter = new EventAdapter(options, this);
	}


	@Override
	public void onEventClick(View view, int position) {
		Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
		EventModel event = adapter.getSnapshots().get(position);
		Intent start = new Intent(getActivity(), EventActivity.class);
		start.putExtra("Event Model", event);
		startActivity(start);
	}
}