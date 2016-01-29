package com.bakigoal.criminalintent.model;

import android.content.Context;
import android.util.Log;

import com.bakigoal.criminalintent.util.CriminalIntentJSONSerializer;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ilmir on 22.01.16.
 */
public class CrimeLab {

  private static final String TAG = "CrimeLab";
  private static final String FILE_NAME = "crimes.json";

  private CriminalIntentJSONSerializer jsonSerializer;

  private List<Crime> crimes;
  private static CrimeLab crimeLab;
  private Context appContext;

  private CrimeLab(Context appContext) {
    this.appContext = appContext;
    jsonSerializer = new CriminalIntentJSONSerializer(appContext, FILE_NAME);
    try {
      crimes = jsonSerializer.loadCrimes();
    } catch (IOException | JSONException e) {
      crimes = new ArrayList<>();
      Log.e(TAG, "Error loading crimes: ", e);
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

  public void addCrime(Crime crime) {
    crimes.add(crime);
  }

  public boolean saveCrimes() {
    try {
      jsonSerializer.saveCrimes(crimes);
      Log.d(TAG, "crimes saved to file " + FILE_NAME);
      return true;
    } catch (JSONException | IOException e) {
      Log.e(TAG, "Error saving crimes: ", e);
      return false;
    }
  }
}
