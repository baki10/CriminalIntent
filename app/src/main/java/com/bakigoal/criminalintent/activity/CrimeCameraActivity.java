package com.bakigoal.criminalintent.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;

import com.bakigoal.criminalintent.fragment.CrimeCameraFragment;

/**
 * Created by ilmir on 30.01.16.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
  @Override
  protected Fragment createFragment() {
    return new CrimeCameraFragment();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Hide the window title.
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    // Hide the status bar and other OS-level chrome
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    super.onCreate(savedInstanceState);

    // Hide action bar
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }
  }
}
