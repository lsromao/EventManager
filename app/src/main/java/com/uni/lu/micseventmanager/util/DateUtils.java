package com.uni.lu.micseventmanager.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Date;

public class DateUtils {

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

	public void selectDateTimePicker(int code, final TextView startDate, final TextView startTime, Activity activity) {
		TimePickerDialog picker;
		final Calendar   calendar = Calendar.getInstance();

		if (code == DateUtils.DATE) {
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog datePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					String dt = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
					startDate.setText(getDateTimeFormatted(dt, DateUtils.DATE));
				}
			}, yy, mm, dd);
			datePicker.show();
		} else if (code == DateUtils.TIME) {
			int hour    = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);

			// time picker dialog
			picker = new TimePickerDialog(activity,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
							startTime.setText(getDateTimeFormatted(sHour + ":" + sMinute, DateUtils.TIME));
						}
					}, hour, minutes, true);
			picker.show();
		}
	}

}
