package com.bakigoal.criminalintent.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ilmir on 22.01.16.
 */
public class Crime {

  private static final String JSON_ID = "id";
  private static final String JSON_TITLE = "title";
  private static final String JSON_DATE = "date";
  private static final String JSON_SOLVED = "solved";
  private static final String JSON_PHOTO = "photo";
  private static final String JSON_SUSPECT = "suspect";
  private static final String JSON_SUSPECT_PHONE = "suspectPhone";

  private UUID id;
  private String title;
  private Date date;
  private boolean solved;
  private Photo photo;
  private String suspect;
  private String suspectPhone;

  public Crime() {
    id = UUID.randomUUID();
    date = new Date();
  }

  public Crime(JSONObject json) throws JSONException {
    id = UUID.fromString(json.getString(JSON_ID));
    title = json.getString(JSON_TITLE);
    date = new Date(json.getLong(JSON_DATE));
    solved = json.getBoolean(JSON_SOLVED);
    if (json.has(JSON_PHOTO)) {
      photo = new Photo(json.getJSONObject(JSON_PHOTO));
    }
    if (json.has(JSON_SUSPECT)) {
      suspect = json.getString(JSON_SUSPECT);
    }
    if (json.has(JSON_SUSPECT_PHONE)) {
      suspectPhone = json.getString(JSON_SUSPECT_PHONE);
    }
  }

  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public boolean isSolved() {
    return solved;
  }

  public void setSolved(boolean solved) {
    this.solved = solved;
  }

  public Photo getPhoto() {
    return photo;
  }

  public void setPhoto(Photo photo) {
    this.photo = photo;
  }

  public String getSuspectPhone() {
    return suspectPhone;
  }

  public void setSuspectPhone(String suspectPhone) {
    this.suspectPhone = suspectPhone;
  }

  public String getSuspect() {
    return suspect;
  }

  public void setSuspect(String suspect) {
    this.suspect = suspect;
  }

  @Override
  public String toString() {
    return title;
  }

  public JSONObject toJSON() throws JSONException {
    JSONObject json = new JSONObject();
    json.put(JSON_ID, id.toString());
    json.put(JSON_TITLE, title);
    json.put(JSON_SOLVED, solved);
    json.put(JSON_DATE, date.getTime());
    if (photo != null) {
      json.put(JSON_PHOTO, photo.toJSON());
    }
    if (suspect != null) {
      json.put(JSON_SUSPECT, suspect);
    }
    if (suspectPhone != null) {
      json.put(JSON_SUSPECT_PHONE, suspectPhone);
    }
    return json;
  }

}
