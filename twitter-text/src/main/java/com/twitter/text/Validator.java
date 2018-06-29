package com.twitter.text;

/**
 * A class for validating Tweet texts.
 */
public class Validator {
  public static final int MAX_TWEET_LENGTH = 280;

  protected int shortUrlLength = 23;
  protected int shortUrlLengthHttps = 23;

  private Extractor extractor = new Extractor();

  public static boolean hasInvalidCharacters(String text) {
    return Regex.INVALID_CHARACTERS_PATTERN.matcher(text).matches();
  }

  public int getShortUrlLength() {
    return shortUrlLength;
  }

  public void setShortUrlLength(int shortUrlLength) {
    this.shortUrlLength = shortUrlLength;
  }

  public int getShortUrlLengthHttps() {
    return shortUrlLengthHttps;
  }

  public void setShortUrlLengthHttps(int shortUrlLengthHttps) {
    this.shortUrlLengthHttps = shortUrlLengthHttps;
  }
}
