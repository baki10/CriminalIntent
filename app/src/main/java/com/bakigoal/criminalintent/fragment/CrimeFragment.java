package com.bakigoal.criminalintent.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bakigoal.criminalintent.R;
import com.bakigoal.criminalintent.activity.CrimeCameraActivity;
import com.bakigoal.criminalintent.activity.CrimeListActivity;
import com.bakigoal.criminalintent.model.Crime;
import com.bakigoal.criminalintent.model.CrimeLab;
import com.bakigoal.criminalintent.model.Photo;
import com.bakigoal.criminalintent.util.FileUtils;
import com.bakigoal.criminalintent.util.PictureUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeFragment extends Fragment {

  public static final String EXTRA_CRIME_ID = "com.bakigoal.criminalintent.crime_id_key";
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy 'Time:'HH:mm", Locale.UK);
  private static final String DIALOG_DATE = "date";
  private static final String DIALOG_TIME = "time";
  private static final String DIALOG_IMAGE = "image";
  private static final String TAG = "CrimeFragment";
  private static final int REQUEST_DATE = 0;
  private static final int REQUEST_TIME = 1;
  private static final int REQUEST_PHOTO = 2;
  private static final int REQUEST_CONTACT = 3;

  private Crime crime;
  private EditText titleField;
  private Button dateButton;
  private CheckBox solvedCheckBox;
  private ImageButton photoButton;
  private ImageView photoView;
  private Button suspectButton;
  private Button suspectCallButton;

  public CrimeFragment() {
    // Required empty public constructor
  }

  public static CrimeFragment newInstance(UUID crimeId) {
    Bundle args = new Bundle();
    args.putSerializable(EXTRA_CRIME_ID, crimeId);

    CrimeFragment crimeFragment = new CrimeFragment();
    crimeFragment.setArguments(args);

    return crimeFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
    crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_crime, container, false);

    titleField = (EditText) view.findViewById(R.id.crime_title);
    titleField.setText(crime.getTitle());
    titleField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        crime.setTitle(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    dateButton = (Button) view.findViewById(R.id.crime_date);
    updateDate();
    dateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showAlertDialog();
      }
    });

    solvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
    solvedCheckBox.setChecked(crime.isSolved());
    solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        crime.setSolved(isChecked);
      }
    });

    photoButton = (ImageButton) view.findViewById(R.id.crime_imageButton);
    photoButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
        startActivityForResult(intent, REQUEST_PHOTO);
      }
    });

    // if camera is not available, disable camera functionality
    PackageManager pm = getActivity().getPackageManager();
    if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
        !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
      photoButton.setEnabled(false);
    }

    photoView = (ImageView) view.findViewById(R.id.crime_imageView);
    photoView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Photo photo = crime.getPhoto();
        if (photo == null) {
          return;
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
        ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
      }
    });

    registerForContextMenu(photoView);

    Button reportButton = (Button) view.findViewById(R.id.crime_reportButton);
    reportButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
        i.putExtra(Intent.EXTRA_SUBJECT,
            getString(R.string.crime_report_subject));
        i = Intent.createChooser(i, getString(R.string.send_report));
        startActivity(i);
      }
    });

    suspectButton = (Button) view.findViewById(R.id.crime_suspectButton);
    suspectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CONTACT);
      }
    });

    if (crime.getSuspect() != null) {
      suspectButton.setText(crime.getSuspect());
    }

    suspectCallButton = (Button) view.findViewById(R.id.crime_suspectCallButton);
    suspectCallButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (crime.getSuspectPhone() != null) {
          Intent intent = new Intent(Intent.ACTION_DIAL);
          Uri uri = Uri.parse(crime.getSuspectPhone());
          intent.setData(uri);
          startActivity(intent);
        }
      }
    });

    return view;
  }

  private void showAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.date_or_time);
    builder.setPositiveButton(R.string.time_name, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        startTimeDialog();
        dialog.dismiss();
      }
    });
    builder.setNegativeButton(R.string.date_name, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        startDateDialog();
        dialog.dismiss();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void startDateDialog() {
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
    dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
    dialog.show(fragmentManager, DIALOG_DATE);
  }

  private void startTimeDialog() {
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    TimePickerFragment dialog = TimePickerFragment.newInstance(crime.getDate());
    dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
    dialog.show(fragmentManager, DIALOG_TIME);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    switch (requestCode) {
      case REQUEST_DATE:
        Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        crime.setDate(date);
        updateDate();
        break;
      case REQUEST_TIME:
        date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
        crime.setDate(date);
        updateDate();
        break;
      case REQUEST_PHOTO:
        String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
        if (filename != null) {
          Log.i(TAG, "filename: " + filename);
          Photo oldPhoto = crime.getPhoto();
          if (oldPhoto != null) {
            deletePhotoFromDisk(oldPhoto);
          }
          Photo newPhoto = new Photo(filename);
          crime.setPhoto(newPhoto);
          showPhoto();
        }
        break;
      case REQUEST_CONTACT:
        Uri contactUri = data.getData();

        // Specify which fields you want your query to return values for.
        String[] queryFields = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER};
        // Perform your query - the contactUri is like a "where" clause here
        Cursor query = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
        // Double-check that you actually got results
        if (query == null) {
          break;
        }
        if (query.getCount() == 0) {
          query.close();
          break;
        }
        // Pull out the first column of the first row of data - that is your suspect's name.
        query.moveToFirst();
        String suspect = query.getString(0);
        String phoneNumber = query.getString(1);
        if (suspect == null || "".equals(suspect)) {
          break;
        }
        crime.setSuspect(suspect);
        crime.setSuspectPhone("tel:" + phoneNumber);
        suspectButton.setText(suspect);
        query.close();
        break;
      default:
        break;
    }
  }

  private void deletePhotoFromDisk(Photo oldPhoto) {
    String path = getActivity().getFileStreamPath(oldPhoto.getFilename()).getAbsolutePath();
    FileUtils.deleteFile(path);
  }

  @Override
  public void onPause() {
    super.onPause();
    CrimeLab.getInstance(getActivity()).saveCrimes();
  }

  private void updateDate() {
    dateButton.setText(dateFormat.format(crime.getDate()));
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.crime_list_item_context, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_delete_crime:
        CrimeLab.getInstance(getActivity()).deleteCrime(crime);
        Intent intent = new Intent(getActivity(), CrimeListActivity.class);
        startActivityForResult(intent, 0);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void showPhoto() {
    // (Re)set the image button's image based on our photo
    Photo photo = crime.getPhoto();
    BitmapDrawable b = null;
    if (photo != null) {
      String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
      b = PictureUtils.getScaledDrawable(getActivity(), path);
    }
    photoView.setImageDrawable(b);
  }

  @Override
  public void onStart() {
    super.onStart();
    showPhoto();
  }

  @Override
  public void onStop() {
    super.onStop();
    PictureUtils.cleanImageView(photoView);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    getActivity().getMenuInflater().inflate(R.menu.crime_item_context, menu);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.photo_delete_crime:
        Photo photo = crime.getPhoto();
        if (photo != null) {
          Log.d(TAG, "deleted " + photo);
          deletePhotoFromDisk(photo);
          crime.setPhoto(null);
          PictureUtils.cleanImageView(photoView);
          return true;
        }
    }

    return super.onContextItemSelected(item);
  }

  private String getCrimeReport() {
    String solvedString;
    if (crime.isSolved()) {
      solvedString = getString(R.string.crime_report_solved);
    } else {
      solvedString = getString(R.string.crime_report_unsolved);
    }

    String dateString = dateFormat.format(crime.getDate());
    String suspect = crime.getSuspect();
    if (suspect == null) {
      suspect = getString(R.string.crime_report_no_suspect);
    } else {
      suspect = getString(R.string.crime_report_suspect, suspect);
    }

    return getString(R.string.crime_report, crime.getTitle(), dateString, solvedString, suspect);
  }
}
