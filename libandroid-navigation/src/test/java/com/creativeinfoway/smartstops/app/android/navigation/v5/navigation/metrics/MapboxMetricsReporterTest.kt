package com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.metrics

import android.os.Parcel
import com.google.gson.Gson
import com.mapbox.android.telemetry.Event
import com.mapbox.android.telemetry.MapboxTelemetry
import com.mapbox.navigation.utils.thread.WorkThreadHandler
import com.creativeinfoway.smartstops.app.android.navigation.v5.internal.navigation.metrics.NavigationMetrics
import com.creativeinfoway.smartstops.app.android.navigation.v5.internal.utils.extensions.toTelemetryEvent
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MapboxMetricsReporterTest {

    @Test
    fun telemetryEnabledWhenReporterInit() {
        val mapboxTelemetry = mockk<MapboxTelemetry>(relaxed = true)
        val threadWorker = mockk<WorkThreadHandler>(relaxed = true)

        MapboxMetricsReporter.init(mapboxTelemetry, threadWorker)

        verify { mapboxTelemetry.enable() }
    }

    @Test
    fun telemetryDisabledWhenReporterDisable() {
        val mapboxTelemetry = initMetricsReporterWithTelemetry()

        MapboxMetricsReporter.disable()

        verify { mapboxTelemetry.disable() }
    }

    @Test
    fun telemetryPushCalledWhenAddValidEvent() {
        val mapboxTelemetry = initMetricsReporterWithTelemetry()
        val metricEvent =
            StubNavigationEvent(NavigationMetrics.ARRIVE)
        val event = metricEvent.toTelemetryEvent()

        MapboxMetricsReporter.addEvent(metricEvent)

        verify { mapboxTelemetry.push(event) }
    }

    @Test
    fun telemetryPushCalledWhenAddInvalidEvent() {
        val mapboxTelemetry = initMetricsReporterWithTelemetry()
        val metricEvent =
            StubNavigationEvent("some_event")
        val event = metricEvent.toTelemetryEvent()

        MapboxMetricsReporter.addEvent(metricEvent)

        verify(exactly = 0) { mapboxTelemetry.push(event) }
    }

    private fun initMetricsReporterWithTelemetry(): MapboxTelemetry {
        val mapboxTelemetry = mockk<MapboxTelemetry>(relaxed = true)
        val threadWorker = mockk<WorkThreadHandler>(relaxed = true)
        MapboxMetricsReporter.init(mapboxTelemetry, threadWorker)

        return mapboxTelemetry
    }

    private class StubNavigationEvent(
        override val metric: String
    ) : Event(), MetricEvent {

        override fun writeToParcel(dest: Parcel?, flags: Int) {}

        override fun describeContents(): Int = 0

        override fun toJson(gson: Gson): String = gson.toJson(this)
    }
}
