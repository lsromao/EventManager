package com.uni.lu.micseventmanager.controller;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseControllerTest {

	private FirebaseController firebaseController;

	@Mock
	FirebaseFirestore firestore;

	@Mock
	CollectionReference reference;

	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		firebaseController = mock(FirebaseController.class);
	}

	@Test
	public void getFirestoreInstance(){
		when(firebaseController.getFirestoreInstance()).thenReturn(firestore);

		assertThat(firebaseController.getFirestoreInstance(), instanceOf(FirebaseFirestore.class));
	}

	@Test
	public void getUserName(){
		String userName = "Test Controller";

		when(firebaseController.getUserName()).thenReturn(userName);

		assertEquals(userName, firebaseController.getUserName());
	}

	@Test
	public void getUserEmail(){
		String userEmail = "test@test.com";

		when(firebaseController.getUserEmail()).thenReturn(userEmail);

		assertEquals(userEmail, firebaseController.getUserEmail());
	}

	@Test
	public void getUserImageUrl(){
		String userImageUrl = "gs://misc-eventmanager.appspot.com/profile_pictures/default.png";

		when(firebaseController.getUserImageUrl()).thenReturn(userImageUrl);

		assertEquals(userImageUrl, firebaseController.getUserImageUrl());
	}

	@Test
	public void getUserId(){
		String userId = "test1235auto";

		when(firebaseController.getUserId()).thenReturn(userId);

		assertEquals(userId, firebaseController.getUserId());
	}

	@Test
	public void getCommentsCollectionReference(){
		when(firebaseController.getCommentsCollectionReference()).thenReturn(reference);

		assertThat(firebaseController.getCommentsCollectionReference(), instanceOf(CollectionReference.class));
	}

	@Test
	public void getEventsCollectionReference(){
		when(firebaseController.getEventsCollectionReference()).thenReturn(reference);

		assertThat(firebaseController.getEventsCollectionReference(), instanceOf(CollectionReference.class));
	}

	@Test
	public void getLikesCollectionReference(){
		when(firebaseController.getLikesCollectionReference()).thenReturn(reference);

		assertThat(firebaseController.getLikesCollectionReference(), instanceOf(CollectionReference.class));
	}
}