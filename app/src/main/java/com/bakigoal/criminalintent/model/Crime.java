package com.bakigoal.criminalintent.model;

import java.util.UUID;

/**
 * Created by ilmir on 22.01.16.
 */
public class Crime {

  private UUID id;
  private String title;

  public Crime() {
    id = UUID.randomUUID();
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
}
