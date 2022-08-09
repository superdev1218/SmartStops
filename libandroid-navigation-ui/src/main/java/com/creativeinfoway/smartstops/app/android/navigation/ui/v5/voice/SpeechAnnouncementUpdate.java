package com.creativeinfoway.smartstops.app.android.navigation.ui.v5.voice;

import androidx.core.util.Pair;

interface SpeechAnnouncementUpdate {

  Pair<String, String> buildTextAndTypeFrom(SpeechAnnouncement speechAnnouncement);
}
