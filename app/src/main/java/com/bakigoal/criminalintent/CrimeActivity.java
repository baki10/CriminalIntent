package com.bakigoal.criminalintent;

import android.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {

  @Override
  protected Fragment createFragment() {
    return new CrimeFragment();
  }
}
