package com.bakigoal.criminalintent;


import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bakigoal.criminalintent.model.Crime;
import com.bakigoal.criminalintent.model.CrimeLab;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends ListFragment {

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

    ArrayAdapter<Crime> adapter =
        new ArrayAdapter<>(
            getActivity(),
            android.R.layout.simple_list_item_1,
            crimes
        );
    setListAdapter(adapter);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Crime crime = (Crime) getListAdapter().getItem(position);
    Log.d(TAG, crime.getTitle() + " was clicked");
  }
}
