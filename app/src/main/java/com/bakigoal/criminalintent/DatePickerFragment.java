package com.bakigoal.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ilmir on 27.01.16.
 */
public class DatePickerFragment extends DialogFragment {
  public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";
  private Date date;

  public static DatePickerFragment newInstance(Date date) {
    Bundle args = new Bundle();
    args.putSerializable(EXTRA_DATE, date);

    DatePickerFragment fragment = new DatePickerFragment();
    fragment.setArguments(args);

    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    date = (Date) getArguments().getSerializable(EXTRA_DATE);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

    DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_date_datePicker);
    datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
      @Override
      public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
        //Update argument to preserve selected value on rotation
        getArguments().putSerializable(EXTRA_DATE, date);
      }
    });

    return new AlertDialog.Builder(getActivity())
        .setTitle(R.string.date_picker_title)
        .setView(view)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            sendResult(Activity.RESULT_OK);
          }
        })
        .create();
  }

  private void sendResult(int resultCode) {
    if (getTargetFragment() == null) {
      return;
    }
    Intent intent = new Intent();
    intent.putExtra(EXTRA_DATE, date);

    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
  }
}
