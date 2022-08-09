package com.creativeinfoway.smartstops.app.android.navigation.v5.offroute

import android.location.Location
import com.mapbox.navigator.NavigationStatus
import com.mapbox.navigator.RouteState
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.MapboxNavigationOptions
import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.RouteProgress

class OffRouteDetector : OffRoute() {

    override fun isUserOffRoute(
        location: Location,
        routeProgress: RouteProgress,
        options: MapboxNavigationOptions
    ): Boolean {
        // No impl
        return false
    }

    fun isUserOffRouteWith(status: NavigationStatus): Boolean {
        return status.routeState == RouteState.OFFROUTE
    }
}
