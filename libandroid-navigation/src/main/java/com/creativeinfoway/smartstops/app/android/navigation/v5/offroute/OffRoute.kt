package com.creativeinfoway.smartstops.app.android.navigation.v5.offroute

import android.location.Location
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.MapboxNavigationOptions
import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.RouteProgress

abstract class OffRoute {

    abstract fun isUserOffRoute(
        location: Location,
        routeProgress: RouteProgress,
        options: MapboxNavigationOptions
    ): Boolean
}
