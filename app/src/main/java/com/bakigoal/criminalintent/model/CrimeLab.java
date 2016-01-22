package com.bakigoal.criminalintent.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ilmir on 22.01.16.
 */
public class CrimeLab {
  private List<Crime> crimes;

  private static CrimeLab crimeLab;
  private Context appContext;

  private CrimeLab(Context appContext) {
    this.appContext = appContext;
    crimes = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      Crime c = new Crime();
      c.setTitle("Crime #" + i);
      c.setSolved(i % 2 == 0); // Every other one
      crimes.add(c);
    }
  }

  public static CrimeLab getInstance(Context context) {
    if (crimeLab == null) {
      crimeLab = new CrimeLab(context);
    }
    return crimeLab;
  }

  public List<Crime> getCrimes() {
    return crimes;
  }

  public Crime getCrime(UUID id) {
    for (Crime crime : crimes) {
      if (crime.getId().equals(id)) {
        return crime;
      }
    }
    return null;
  }
}
