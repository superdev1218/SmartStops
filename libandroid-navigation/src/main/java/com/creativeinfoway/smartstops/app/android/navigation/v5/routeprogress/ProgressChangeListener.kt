package com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress

import android.location.Location

interface ProgressChangeListener {
    fun onProgressChange(location: Location, routeProgress: RouteProgress)
}
