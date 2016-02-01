package com.bakigoal.criminalintent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.bakigoal.criminalintent.activity.CrimePagerActivity;
import com.bakigoal.criminalintent.R;
import com.bakigoal.criminalintent.model.Crime;
import com.bakigoal.criminalintent.model.CrimeLab;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends ListFragment {

  private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy 'Time:'HH:mm", Locale.US);
  private static final String TAG = "CrimeListFragment";
  private List<Crime> crimes;
  private Callbacks callbacks;

  /**
   * Required interface for hosting activities.
   */
  public interface Callbacks{
    void onCrimeSelected(Crime crime);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    callbacks = (Callbacks) context;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    callbacks = null;
  }

  public void updateUI(){
    ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
  }

  public CrimeListFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    getActivity().setTitle(R.string.crimes_title);
    crimes = CrimeLab.getInstance(getActivity()).getCrimes();

    ArrayAdapter adapter = new CrimeAdapter(crimes);
    setListAdapter(adapter);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    if (view == null) {
      return null;
    }
    ListView listView = (ListView) view.findViewById(android.R.id.list);
    if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
      // Use floating context menus on Froyo and Gingerbread
      registerForContextMenu(listView);
    }else {
      // Use contextual action bar on Honeycomb and higher
      listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
      setMultiChoiceListener(listView);
    }

    return view;
  }

  private void setMultiChoiceListener(ListView listView) {
    listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
      @Override
      public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

      }

      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.crime_list_item_context, menu);
        return true;
      }

      @Override
      public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
      }

      @Override
      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()){
          case R.id.menu_item_delete_crime:
            CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
            CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
            for(int i = adapter.getCount() - 1; i>=0; i--){
              if(getListView().isItemChecked(i)){
                crimeLab.deleteCrime(adapter.getItem(i));
              }
            }
            mode.finish();
            adapter.notifyDataSetChanged();
            return true;
          default:
            return false;
        }
      }

      @Override
      public void onDestroyActionMode(ActionMode mode) {

      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
    setEmptyText(getResources().getText(R.string.empty_list));
  }


  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Crime crime = ((CrimeAdapter) getListAdapter()).getItem(position);
    Log.d(TAG, crime.getTitle() + " was clicked");

//    Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
//    intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
//    startActivity(intent);
    callbacks.onCrimeSelected(crime);
  }

  //  onResume() is the safest place to take action to update a fragment’s view
  @Override
  public void onResume() {
    super.onResume();
    ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_crime_list, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_new_crime:
        Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).addCrime(crime);
        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
        startActivityForResult(intent, 0);
        return true;
      case R.id.menu_item_show_subtitle:
        if (getActivity() != null && getActivity().getActionBar() != null) {
          getActivity().getActionBar().setSubtitle(R.string.subtitle);
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    //generally we have to check which view was long pressed
    getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    int position = info.position;
    CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
    Crime crime = adapter.getItem(position);

    switch (item.getItemId()){
      case R.id.menu_item_delete_crime:
        CrimeLab.getInstance(getActivity()).deleteCrime(crime);
        adapter.notifyDataSetChanged();
        return true;
    }

    return super.onContextItemSelected(item);
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
