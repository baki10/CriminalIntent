package com.bakigoal.criminalintent.util;

import android.content.Context;

import com.bakigoal.criminalintent.model.Crime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilmir on 30.01.16.
 */
public class CriminalIntentJSONSerializer {
  private Context context;
  private String fileName;

  public CriminalIntentJSONSerializer(Context context, String fileName) {
    this.context = context;
    this.fileName = fileName;
  }

  public void saveCrimes(List<Crime> crimes) throws JSONException, IOException {
    JSONArray array = new JSONArray();
    for (Crime crime : crimes) {
      array.put(crime.toJSON());
    }

    Writer writer = null;
    try {
      OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      writer = new OutputStreamWriter(out);

      writer.write(array.toString());
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  public List<Crime> loadCrimes() throws IOException, JSONException {
    List<Crime> crimes = new ArrayList<>();
    BufferedReader reader = null;
    try {
      InputStream in = context.openFileInput(fileName);
      reader = new BufferedReader(new InputStreamReader(in));

      StringBuilder jsonString = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        jsonString.append(line);
      }

      JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
      for (int i = 0; i < array.length(); i++) {
        crimes.add(new Crime(array.getJSONObject(i)));
      }
    } catch (FileNotFoundException ignored) {
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    return crimes;
  }
}
