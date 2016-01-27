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
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ilmir on 27.01.16.
 */
public class TimePickerFragment extends DialogFragment {
  public static final String EXTRA_TIME = "com.bakigoal.criminalintent.time";
  private Date date;

  public static TimePickerFragment newInstance(Date date) {
    Bundle args = new Bundle();
    args.putSerializable(EXTRA_TIME, date);

    TimePickerFragment fragment = new TimePickerFragment();
    fragment.setArguments(args);

    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    date = (Date) getArguments().getSerializable(EXTRA_TIME);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);

    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);

    TimePicker timePicker = (TimePicker) view.findViewById(R.id.dialog_time_timePicker);
    timePicker.setCurrentHour(hour);
    timePicker.setCurrentMinute(minute);
    timePicker.setIs24HourView(true);
    timePicker.setEnabled(true);
    timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
      @Override
      public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        date = cal.getTime();
        //Update argument to preserve selected value on rotation
        getArguments().putSerializable(EXTRA_TIME, date);
      }
    });

    return new AlertDialog.Builder(getActivity())
        .setTitle(R.string.time_picker_title)
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
    intent.putExtra(EXTRA_TIME, date);

    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
  }
}
