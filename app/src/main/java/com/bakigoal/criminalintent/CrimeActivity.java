package com.bakigoal.criminalintent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CrimeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_crime);
    setTitle(R.string.title_activity_crime);

    FragmentManager fragmentManager = getFragmentManager();
    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

    if(fragment == null){
      fragment = new CrimeFragment();
      fragmentManager.beginTransaction()
          .add(R.id.fragment_container,fragment)
          .commit();
    }

  }
}
