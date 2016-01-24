package com.bakigoal.criminalintent;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.bakigoal.criminalintent.model.Crime;
import com.bakigoal.criminalintent.model.CrimeLab;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends ListFragment {

  private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy 'Time:'hh:mm:ss", Locale.US);
  private static final String TAG = "CrimeListFragment";
  private List<Crime> crimes;

  public CrimeListFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().setTitle(R.string.crimes_title);
    crimes = CrimeLab.getInstance(getActivity()).getCrimes();

    ArrayAdapter adapter = new CrimeAdapter(crimes);
    setListAdapter(adapter);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Crime crime = ((CrimeAdapter) getListAdapter()).getItem(position);
    Log.d(TAG, crime.getTitle() + " was clicked");

    Intent intent = new Intent(getActivity(), CrimeActivity.class);
    intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
    startActivity(intent);
  }

  //  onResume() is the safest place to take action to update a fragment’s view
  @Override
  public void onResume() {
    super.onResume();
    ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
  }

  private class CrimeAdapter extends ArrayAdapter<Crime> {

    public CrimeAdapter(List<Crime> crimes) {
      super(getActivity(), 0, crimes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
      }

      Crime crime = getItem(position);

      TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
      titleTextView.setText(crime.getTitle());
      TextView dateTextView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
      dateTextView.setText(dateFormat.format(crime.getDate()));
      CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
      solvedCheckBox.setChecked(crime.isSolved());

      return convertView;
    }
  }
}
