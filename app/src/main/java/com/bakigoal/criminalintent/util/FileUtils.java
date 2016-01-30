package com.bakigoal.criminalintent.util;

import android.util.Log;

import java.io.File;

/**
 * Created by ilmir on 30.01.16.
 */
public class FileUtils {

  private static final String TAG = "FileUtils";

  public static void deleteFile(String absolutePath) {

    File file = new File(absolutePath);
    boolean deleted = file.delete();
    Log.d(TAG, "Deleted: " + deleted);
  }
}
