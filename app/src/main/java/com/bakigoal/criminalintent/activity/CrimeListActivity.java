package com.bakigoal.criminalintent.activity;

import android.support.v4.app.Fragment;

import com.bakigoal.criminalintent.fragment.CrimeListFragment;

/**
 * Created by ilmir on 23.01.16.
 */
public class CrimeListActivity extends SingleFragmentActivity {
  @Override
  protected Fragment createFragment() {
    return new CrimeListFragment();
  }
}
