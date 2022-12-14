package com.creativeinfoway.smartstops.app.example.ui.navigation

import android.location.Location
import android.preference.PreferenceManager
import android.widget.Toast
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.creativeinfoway.smartstops.app.NavigationApplication
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.MapboxOfflineRouter
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.NavigationRoute
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.OfflineError
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.OfflineRoute
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.OnOfflineRouteFoundCallback
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.OnOfflineTilesConfiguredCallback
import timber.log.Timber

private const val BEARING_TOLERANCE = 90.0

class OfflineRouteFinder(
    offlinePath: String,
    version: String,
    private val callback: OnRoutesFoundCallback
) {

    private val offlineRouter: MapboxOfflineRouter = MapboxOfflineRouter(offlinePath)
    private var isConfigured = false

    init {
        configureWith(version)
    }

    fun configureWith(version: String) {
        if (isOfflineEnabled() && version.isEmpty()) {
            val errorMessage = "Offline version not set, please do so in settings"
            Toast.makeText(NavigationApplication.instance, errorMessage, Toast.LENGTH_SHORT).show()
            return
        }

        offlineRouter.configure(version, object : OnOfflineTilesConfiguredCallback {
            override fun onConfigured(numberOfTiles: Int) {
                Timber.d("Offline tiles configured: $numberOfTiles")
                isConfigured = true
            }

            override fun onConfigurationError(error: OfflineError) {
                Timber.d("Offline tiles configuration error: {${error.message}}")
                isConfigured = false
            }
        })
    }

    fun findRoute(location: Location, destination: Point) {
        if (isConfigured) {
            findOfflineRoute(location, destination)
        } else {
            handleRoutingError()
        }
    }

    private fun isOfflineEnabled(): Boolean {
        val context = NavigationApplication.instance
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(context.getString(R.string.offline_enabled), false)
    }

    private fun findOfflineRoute(location: Location, destination: Point) {
        val offlineRoute = buildOfflineRoute(location, destination)
        offlineRouter.findRoute(offlineRoute, object : OnOfflineRouteFoundCallback {
            override fun onRouteFound(route: DirectionsRoute) {
                callback.onRoutesFound(listOf(route))
            }

            override fun onError(error: OfflineError) {
                callback.onError(error.message)
            }
        })
    }

    private fun buildOfflineRoute(location: Location, destination: Point): OfflineRoute {
        val origin = Point.fromLngLat(location.longitude, location.latitude)
        val bearing = location.bearing.toDouble()
        return NavigationRoute.builder(NavigationApplication.instance)
            .origin(origin, bearing, BEARING_TOLERANCE)
            .destination(destination)
            .accessToken(Mapbox.getAccessToken()!!) // OZ: Ok to use "!!". This code should crash if getAccessToken() returns null
            .let {
                OfflineRoute.builder(it).build()
            }
    }

    private fun handleRoutingError() {
        val errorMessage = "Offline routing is not configured, try again"
        Toast.makeText(NavigationApplication.instance, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
