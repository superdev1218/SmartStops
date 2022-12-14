package com.creativeinfoway.smartstops.app.android.navigation.ui.v5.feedback;

import com.creativeinfoway.smartstops.app.android.navigation.v5.internal.navigation.metrics.FeedbackEvent;

public class FeedbackItem {

  @FeedbackEvent.FeedbackType
  private String feedbackType;
  private String feedbackText;
  private String description;
  private int feedbackImage;

  FeedbackItem(String feedbackText,
               int feedbackImage,
               @FeedbackEvent.FeedbackType String feedbackType,
               String description) {
    this.feedbackText = feedbackText;
    this.feedbackImage = feedbackImage;
    this.feedbackType = feedbackType;
    this.description = description;
  }

  String getFeedbackText() {
    return feedbackText;
  }

  int getFeedbackImageId() {
    return feedbackImage;
  }

  @FeedbackEvent.FeedbackType
  public String getFeedbackType() {
    return feedbackType;
  }

  public String getDescription() {
    return description;
  }
}
