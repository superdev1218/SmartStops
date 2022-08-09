package com.creativeinfoway.smartstops.app.example.ui.navigation

import android.location.Location
import com.creativeinfoway.smartstops.app.example.ui.ExampleViewModel
import com.creativeinfoway.smartstops.app.android.navigation.v5.offroute.OffRouteListener

class ExampleOffRouteListener(private val viewModel: ExampleViewModel) : OffRouteListener {

    override fun userOffRoute(location: Location) {
        viewModel.isOffRoute = true
        viewModel.findRouteToDestination()
    }
}
