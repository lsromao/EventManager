package com.uni.lu.eventmanager.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.activities.EventActivity;
import com.uni.lu.eventmanager.adapter.EventAdapter;
import com.uni.lu.eventmanager.adapter.OnItemListener;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.model.EventModel;


public class EventsFragment extends Fragment implements OnItemListener {

	private RecyclerView        recyclerView;
	private LinearLayoutManager linearLayoutManager;
	private EventAdapter        adapter;


	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_events, container, false);

		setRecyclerView();

		recyclerView = root.findViewById(R.id.recyclerItems);
		recyclerView.setHasFixedSize(true);
		linearLayoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);

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
		Query query = FirebaseController.getInstance().getEventsCollectionReference()
				.whereEqualTo("privacy", false)
				.orderBy("startDate", Query.Direction.DESCENDING);

		FirestoreRecyclerOptions<EventModel> options =
				new FirestoreRecyclerOptions.Builder<EventModel>()
						.setQuery(query, EventModel.class)
						.build();


		adapter = new EventAdapter(options, this);
	}


	@Override
	public void onEventClick(View view, int position) {
		EventModel event = adapter.getSnapshots().get(position);
		Intent start = new Intent(getActivity(), EventActivity.class);
		start.putExtra("Event Model", event);
		startActivity(start);
	}
}