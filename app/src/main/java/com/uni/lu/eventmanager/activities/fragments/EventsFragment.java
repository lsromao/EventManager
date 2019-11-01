package com.uni.lu.eventmanager.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.view.EventsView;


public class EventsFragment extends Fragment {

	private EventsView eventsView;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		eventsView =  ViewModelProviders.of(this).get(EventsView.class);

		View           root     = inflater.inflate(R.layout.fragment_events, container, false);

		final TextView textView = root.findViewById(R.id.text_home);
		eventsView.getText().observe(this, new Observer<String>() {
			@Override
			public void onChanged(@Nullable String s) {
				textView.setText(s);
			}
		});
		return root;
	}
}