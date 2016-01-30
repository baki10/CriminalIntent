package com.bakigoal.criminalintent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bakigoal.criminalintent.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("deprecation")
public class CrimeCameraFragment extends Fragment {

  private static final String TAG = "CrimeCameraFragment";
  public static final String EXTRA_PHOTO_FILENAME = "com.bakigoal.criminal.photo_philename";
  private Camera camera;
  private SurfaceView surfaceView;
  private View progressContainer;

  private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
    @Override
    public void onShutter() {
      progressContainer.setVisibility(View.VISIBLE);
    }
  };

  private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
      // Create a filename
      String filename = UUID.randomUUID().toString() + ".jpg";
      // Save the jpeg data to disk
      FileOutputStream os = null;
      boolean success = true;
      try {
        os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
        os.write(data);
      } catch (Exception e) {
        Log.e(TAG, "Error writing to file " + filename, e);
        success = false;
      } finally {
        try {
          if (os != null)
            os.close();
        } catch (Exception e) {
          Log.e(TAG, "Error closing file " + filename, e);
          success = false;
        }
      }
      if (success) {
        Log.i(TAG, "JPEG saved at " + filename);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PHOTO_FILENAME, filename);
        getActivity().setResult(Activity.RESULT_OK, intent);
      } else {
        getActivity().setResult(Activity.RESULT_CANCELED);
      }
      getActivity().finish();
    }
  };

  public CrimeCameraFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_crime_camera, container, false);

    Button takePictureButton = (Button) view.findViewById(R.id.crime_camera_takePictureButton);
    takePictureButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (camera != null) {
          camera.takePicture(shutterCallback, null, pictureCallback);
        }
      }
    });
    surfaceView = (SurfaceView) view.findViewById(R.id.crime_camera_surfaceView);
    SurfaceHolder holder = surfaceView.getHolder();
    addCalbackToHolder(holder);

    progressContainer = view.findViewById(R.id.crime_camera_progressContainer);
    progressContainer.setVisibility(View.INVISIBLE);

    return view;
  }

  private void addCalbackToHolder(SurfaceHolder holder) {
    holder.addCallback(new SurfaceHolder.Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder holder) {
        // Tell the camera to use this surface as its preview
        try {
          if (camera != null) {
            camera.setPreviewDisplay(holder);
          }
        } catch (IOException e) {
          Log.e(TAG, "Error setting up preview display", e);
        }
      }

      @Override
      public void surfaceDestroyed(SurfaceHolder holder) {
        // We can no longer display on this surface, so stop the
        if (camera != null) {
          camera.stopPreview();
        }
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (camera == null) {
          return;
        }

        // the surface has changed size; update the camera preview size
        Camera.Parameters parameters = camera.getParameters();
        Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
        parameters.setPreviewSize(s.width, s.height);
        s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
        parameters.setPictureSize(s.width, s.height);
        camera.setParameters(parameters);
        try {
          camera.startPreview();
        } catch (Exception e) {
          Log.e(TAG, "Could not start preview", e);
          camera.release();
          camera = null;
        }
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    camera = Camera.open(0);
  }

  @Override
  public void onPause() {
    super.onPause();
    if (camera != null) {
      camera.release();
      camera = null;
    }
  }

  /**
   * a simple algorithm to get the largest size available. For a more
   * robust version, see CameraPreview.java in the ApiDemos
   * sample app from Android.
   */
  private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
    Size bestSize = sizes.get(0);
    int largestArea = bestSize.width * bestSize.height;
    for (Size s : sizes) {
      int area = s.width * s.height;
      if (area > largestArea) {
        bestSize = s;
        largestArea = area;
      }
    }
    return bestSize;
  }
}
