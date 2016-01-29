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

  private UUID id;
  private String title;
  private Date date;
  private boolean solved;

  public Crime() {
    id = UUID.randomUUID();
    date = new Date();
  }
  public Crime(JSONObject json)throws JSONException{
    id = UUID.fromString(json.getString(JSON_ID));
    title = json.getString(JSON_TITLE);
    date = new Date(json.getLong(JSON_DATE));
    solved = json.getBoolean(JSON_SOLVED);
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
    return json;
  }

}
