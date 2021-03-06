package com.bakigoal.criminalintent.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.Display;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ilmir on 30.01.16.
 */
public class PictureUtils {

  private static final String TAG = "PictureUtils";

  /**
   * Get a BitmapDrawable from a local file that is scaled down
   * to fit the current Window size.
   */
  @SuppressWarnings("deprecation")
  public static BitmapDrawable getScaledDrawable(Activity a, String path) {
    Display display = a.getWindowManager().getDefaultDisplay();
    float destWidth = display.getWidth();
    float destHeight = display.getHeight();
    // Read in the dimensions of the image on disk
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);
    float srcWidth = options.outWidth;
    float srcHeight = options.outHeight;
    int inSampleSize = 1;
    if (srcHeight > destHeight || srcWidth > destWidth) {
      if (srcWidth > srcHeight) {
        inSampleSize = Math.round(srcHeight / destHeight);
      } else {
        inSampleSize = Math.round(srcWidth / destWidth);
      }
    }
    options = new BitmapFactory.Options();
    options.inSampleSize = inSampleSize;
    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
    return new BitmapDrawable(a.getResources(), bitmap);
  }

  public static void cleanImageView(ImageView imageView) {
    if (!(imageView.getDrawable() instanceof BitmapDrawable))
      return;
    // Clean up the view's image for the sake of memory
    BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
    if (b.getBitmap() == null) {
      return;
    }
    b.getBitmap().recycle();
    imageView.setImageDrawable(null);
  }

  public static File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    );
    return image;
  }
}
