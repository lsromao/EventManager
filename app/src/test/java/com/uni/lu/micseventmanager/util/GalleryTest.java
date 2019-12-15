package com.uni.lu.micseventmanager.util;

import android.app.Activity;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GalleryTest {

	private Gallery gallery;

	@Mock
	Activity activityMocked;

	@Mock
	Intent intentMock;

	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		gallery = mock(Gallery.class);
	}

	@Test
	public void pickGalleryIntent(){

		when(gallery.pickFromGallery(activityMocked)).thenReturn(intentMock);

		assertThat(gallery.pickFromGallery(activityMocked), instanceOf(Intent.class));
	}

}