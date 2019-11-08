package com.uni.lu.eventmanager.util;

import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;

public class DateFormats {

	public static final int DATE = 001;
	public static final int TIME = 002;

	private final String inputFormat_Date  = "yyyy-MM-dd";
	private final String inputFormat_Hours = "h:m";

	private final String outputFormat_Date  = "dd MMMM yyyy";
	private final String outputFormat_Hours = "hh:mm a";


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

	public Date getDateTime(String date, String time){
		String dateTime = date + " " + time;
		Date   dt = null;
		try {
			dt = new SimpleDateFormat("dd MMMM yyyy hh:mm a").parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dt;
	}

	public String validateDates(Date start, Date end) {
		Date dt = new Date();
		if (start.before(dt)) {
			return "Start time cannot be in the past!";
		}else if (end.before(start)){
			return "End time cannot be before start date!";
		}

		return "NO IF";
	}

}
