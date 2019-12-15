package com.uni.lu.micseventmanager.activities;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.uni.lu.micseventmanager.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

	@Before
	public void init(){
		Intents.init();
	}

	@After
	public void tearDown(){
		Intents.release();
	}

	@Rule
	public ActivityTestRule<LoginActivity> activityRule =
			new ActivityTestRule<>(LoginActivity.class);

	@Test
	public void invalidEmail() {
		onView(withId(R.id.email))
				.perform(typeText("test@.com"), closeSoftKeyboard());
		onView(withId(R.id.password))
				.perform(typeText("1234567"), closeSoftKeyboard());
		onView(withId(R.id.btnLogin)).perform(click());
		onView(withText("Invalid email address")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
	}

	@Test
	public void invalidPassword() {
		onView(withId(R.id.email))
				.perform(typeText("test@test.com"), closeSoftKeyboard());
		onView(withId(R.id.password))
				.perform(typeText("1234"), closeSoftKeyboard());
		onView(withId(R.id.btnLogin)).perform(click());
		onView(withText("Password need to have 6 characters")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
	}

	@Test
	public void login() throws InterruptedException {
		onView(withId(R.id.email))
				.perform(typeText("automation@automation.com"), closeSoftKeyboard());
		onView(withId(R.id.password))
				.perform(typeText("autom1234"), closeSoftKeyboard());
		onView(withId(R.id.btnLogin)).perform(click());
		Thread.sleep(600);
		intended(hasComponent(ProfileActivity.class.getName()));
	}


}