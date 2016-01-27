package com.bakigoal.criminalintent;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bakigoal.criminalintent.model.Crime;
import com.bakigoal.criminalintent.model.CrimeLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeFragment extends Fragment {

  public static final String EXTRA_CRIME_ID = "com.bakigoal.criminalintent.crime_id_key";
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy 'Time:'hh:mm", Locale.US);
  private static final String DIALOG_DATE = "date";
  private static final int REQUEST_DATE = 0;
  private static final int REQUEST_TIME = 1;

  private Crime crime;
  private EditText titleField;
  private Button dateButton;
  private CheckBox solvedCheckBox;

  public CrimeFragment() {
    // Required empty public constructor
  }

  public static CrimeFragment newInstance(UUID crimeId) {
    Bundle args = new Bundle();
    args.putSerializable(EXTRA_CRIME_ID, crimeId);

    CrimeFragment crimeFragment = new CrimeFragment();
    crimeFragment.setArguments(args);

    return crimeFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
    crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_crime, container, false);

    titleField = (EditText) view.findViewById(R.id.crime_title);
    titleField.setText(crime.getTitle());
    titleField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        crime.setTitle(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    dateButton = (Button) view.findViewById(R.id.crime_date);
    updateDate();
    dateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showAlertDialog();
      }
    });

    solvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
    solvedCheckBox.setChecked(crime.isSolved());
    solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        crime.setSolved(isChecked);
      }
    });

    return view;
  }

  private void showAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Update date or time?");
    builder.setPositiveButton(R.string.date_name, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        startDateDialog();
        dialog.dismiss();
      }
    });
    builder.setNegativeButton(R.string.time_name, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        startTimeDialog();
        dialog.dismiss();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void startDateDialog() {
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
    dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
    dialog.show(fragmentManager, DIALOG_DATE);
  }

  private void startTimeDialog() {
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    TimePickerFragment dialog = TimePickerFragment.newInstance(crime.getDate());
    dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
    dialog.show(fragmentManager, DIALOG_DATE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    if (requestCode == REQUEST_DATE) {
      Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
      crime.setDate(date);
      updateDate();
    }
    if (requestCode == REQUEST_TIME) {
      Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
      crime.setDate(date);
      updateDate();
    }
  }

  private void updateDate() {
    dateButton.setText(dateFormat.format(crime.getDate()));
  }
}
