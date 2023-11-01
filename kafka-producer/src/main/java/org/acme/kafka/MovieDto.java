package org.acme.kafka;

public class MovieDto {

  private final String title;

  private final int year;

  public MovieDto(String title, int year) {
    this.title = title;
    this.year = year;
  }

  public String getTitle() {
    return title;
  }

  public int getYear() {
    return year;
  }

}
