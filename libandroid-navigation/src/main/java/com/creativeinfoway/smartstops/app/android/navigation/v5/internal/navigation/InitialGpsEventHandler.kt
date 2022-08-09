package com.creativeinfoway.smartstops.app.android.navigation.v5.internal.navigation

import com.creativeinfoway.smartstops.app.android.navigation.v5.internal.navigation.metrics.MetricsReporter

internal class InitialGpsEventHandler(
    private val metricsReporter: MetricsReporter
) {

    fun send(
        elapsedTime: Double,
        sessionId: String,
        metadata: NavigationPerformanceMetadata
    ) {
        val event = InitialGpsEvent(elapsedTime, sessionId, metadata)
        metricsReporter.addEvent(event)
    }
}
