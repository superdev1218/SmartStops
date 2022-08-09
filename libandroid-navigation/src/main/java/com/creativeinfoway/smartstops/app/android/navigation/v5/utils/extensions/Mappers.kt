@file:JvmName("Mappers")

package com.creativeinfoway.smartstops.app.android.navigation.v5.utils.extensions

import com.mapbox.api.directions.v5.WalkingOptions
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteLeg
import com.mapbox.navigation.base.route.model.Route
import com.mapbox.navigation.base.route.model.WalkingOptionsNavigation

fun WalkingOptionsNavigation.mapToWalkingOptions(): WalkingOptions = WalkingOptions
    .builder()
    .walkingSpeed(walkingSpeed)
    .walkwayBias(walkwayBias)
    .alleyBias(alleyBias)
    .build()

fun Route.mapToDirectionsRoute(): DirectionsRoute {
    val duration = duration.toDouble()
    val legs = legs?.legs?.let { it as List<RouteLeg> }

    return DirectionsRoute.builder()
        .distance(distance)
        .duration(duration)
        .geometry(geometry)
        .weight(weight)
        .weightName(weightName)
        .voiceLanguage(voiceLanguage)
        .legs(legs)
        .build()
}
