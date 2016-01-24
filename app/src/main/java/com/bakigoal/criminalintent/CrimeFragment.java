package com.bakigoal.criminalintent;


import android.app.Fragment;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeFragment extends Fragment {

  public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

  private Crime crime;
  private EditText titleField;
  private Button dateButton;
  private CheckBox solvedCheckBox;

  public CrimeFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    crime = new Crime();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_crime, container, false);

    titleField = (EditText) view.findViewById(R.id.crime_title);
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
    dateButton.setText(dateFormat.format(crime.getDate()));
    dateButton.setEnabled(false);

    solvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
    solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        crime.setSolved(isChecked);
      }
    });

    return view;
  }

}
