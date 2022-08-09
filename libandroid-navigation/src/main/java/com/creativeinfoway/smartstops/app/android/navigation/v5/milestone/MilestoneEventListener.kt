package com.creativeinfoway.smartstops.app.android.navigation.v5.milestone

import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.RouteProgress

interface MilestoneEventListener {

    fun onMilestoneEvent(
        routeProgress: RouteProgress,
        instruction: String,
        milestone: Milestone
    )
}
