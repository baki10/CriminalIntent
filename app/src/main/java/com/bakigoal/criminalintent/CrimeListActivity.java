package com.bakigoal.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by ilmir on 23.01.16.
 */
public class CrimeListActivity extends SingleFragmentActivity {
  @Override
  protected Fragment createFragment() {
    return new CrimeListFragment();
  }
}
