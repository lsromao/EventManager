package com.uni.lu.eventmanager.util;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import java.text.ParseException;
import java.util.Date;

public class DateUtils {

	public static final int DATE = 001;
	public static final int TIME = 002;

	private final String inputFormat_Date  = "yyyy-MM-dd";
	private final String inputFormat_Hours = "h:m";

	private final String outputFormat_Date  = "dd MMMM yyyy";
	private final String outputFormat_Hours = "hh:mm a";

	private Calendar calendar;

	public DateUtils() {
		this.calendar = Calendar.getInstance();
	}


	public String getDateTimeFormatted(String dateTime, int code) {
		SimpleDateFormat input = null, output = null;
		Date             dtIn  = null;

		if (code == DATE) {
			input = new SimpleDateFormat(inputFormat_Date);
			output = new SimpleDateFormat(outputFormat_Date);
		} else if (code == TIME) {
			input = new SimpleDateFormat(inputFormat_Hours);
			output = new SimpleDateFormat(outputFormat_Hours);
		}

		try {
			dtIn = input.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dateTime = output.format(dtIn);

		return dateTime;
	}

	public Date getDateTime(String date, String time) {
		String dateTime = date + " " + time;
		Date   dt       = null;
		try {
			dt = new SimpleDateFormat("dd MMMM yyyy hh:mm a").parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dt;
	}

}
