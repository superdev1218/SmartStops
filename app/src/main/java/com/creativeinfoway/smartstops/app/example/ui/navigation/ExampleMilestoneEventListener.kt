package com.creativeinfoway.smartstops.app.example.ui.navigation

import androidx.lifecycle.MutableLiveData
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.voice.NavigationSpeechPlayer
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.voice.SpeechAnnouncement
import com.creativeinfoway.smartstops.app.android.navigation.v5.milestone.Milestone
import com.creativeinfoway.smartstops.app.android.navigation.v5.milestone.MilestoneEventListener
import com.creativeinfoway.smartstops.app.android.navigation.v5.milestone.VoiceInstructionMilestone
import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.RouteProgress

class ExampleMilestoneEventListener(
    private val milestone: MutableLiveData<Milestone>,
    private val speechPlayer: NavigationSpeechPlayer
) : MilestoneEventListener {

    override fun onMilestoneEvent(
        routeProgress: RouteProgress,
        instruction: String,
        milestone: Milestone
    ) {
        this.milestone.value = milestone
        if (milestone is VoiceInstructionMilestone) {
            play(milestone)
        }
    }

    private fun play(milestone: VoiceInstructionMilestone) {
        val announcement = SpeechAnnouncement.builder()
            .voiceInstructionMilestone(milestone)
            .build()
        speechPlayer.play(announcement)
    }
}
