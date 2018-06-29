package com.twitter.text;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Range implements Comparable<Range> {

  public static final Range EMPTY = new Range(-1, -1);

  public final int start;
  public final int end;

  public Range(int start, int end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    return this == obj || obj instanceof Range && ((Range) obj).start == this.start &&
        ((Range) obj).end == this.end;
  }

  @Override
  public int hashCode() {
    return 31 * start + 31 * end;
  }

  @Override
  public int compareTo(@NonNull Range other) {
    if (this.start < other.start) {
      return -1;
    } else if (this.start == other.start) {
      if (this.end < other.end) {
        return -1;
      } else {
        return this.end == other.end ? 0 : 1;
      }
    } else {
      return 1;
    }
  }

  public boolean isInRange(int val) {
    return val >= start && val <= end;
  }
}
