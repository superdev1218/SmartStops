package com.creativeinfoway.smartstops.app.android.navigation.v5.offroute

import android.location.Location

interface OffRouteListener {
    fun userOffRoute(location: Location)
}
