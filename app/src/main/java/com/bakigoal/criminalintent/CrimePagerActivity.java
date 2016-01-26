package com.bakigoal.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bakigoal.criminalintent.model.Crime;
import com.bakigoal.criminalintent.model.CrimeLab;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

  private ViewPager viewPager;
  private List<Crime> crimes;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewPager = new ViewPager(this);
    viewPager.setId(R.id.viewPager);
    setContentView(viewPager);

    crimes = CrimeLab.getInstance(this).getCrimes();

    FragmentManager fm = getSupportFragmentManager();
    viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
      @Override
      public Fragment getItem(int position) {
        return CrimeFragment.newInstance(crimes.get(position).getId());
      }

      @Override
      public int getCount() {
        return crimes.size();
      }
    });

    UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
    for (int i = 0; i < crimes.size(); i++) {
      if (crimes.get(i).getId().equals(crimeId)) {
        viewPager.setCurrentItem(i);
        break;
      }
    }

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        Crime crime = crimes.get(position);
        if (crime.getTitle() != null) {
          setTitle(crime.getTitle());
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }
}
