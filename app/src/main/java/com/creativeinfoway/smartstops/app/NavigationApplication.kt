package com.creativeinfoway.smartstops.app

import android.os.StrictMode
import android.text.TextUtils
import androidx.multidex.MultiDexApplication
import com.creativeinfoway.smartstops.app.android.navigation.testapp.BuildConfig
import com.creativeinfoway.smartstops.app.example.utils.DelegatesExt
import com.creativeinfoway.smartstops.app.utils.Utils
import com.mapbox.android.search.MapboxSearch
import com.mapbox.android.search.MapboxSearchOptions
import com.mapbox.crashmonitor.CrashMonitor
import com.mapbox.mapboxsdk.Mapbox
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

private const val DEFAULT_MAPBOX_ACCESS_TOKEN = "pk.eyJ1Ijoic21hcnRzdG9wcyIsImEiOiJjazQwZHJkMGQwMG82M29xdzI3M29vaXhrIn0.KkCpzupptMAiwZP645UQ2A"

class NavigationApplication : MultiDexApplication() {

    companion object {
        var instance: NavigationApplication by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //setupTimber()
        //setupStrictMode()
        //setupCanary()
        setupMapbox()
        //setupCrashMonitor()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }

    private fun setupCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    private fun setupMapbox() {
        val mapboxAccessToken = Utils.getMapboxAccessToken(applicationContext)
        if (TextUtils.isEmpty(mapboxAccessToken) || mapboxAccessToken == DEFAULT_MAPBOX_ACCESS_TOKEN) {
            Timber.w("Mapbox access token isn't set!")
        }

        val cachingMode = MapboxSearchOptions().setCachingEnabled(true)
        MapboxSearch.getInstance(applicationContext, mapboxAccessToken, cachingMode)
        Mapbox.getInstance(applicationContext, mapboxAccessToken)
    }

    private fun setupCrashMonitor() {
        val crashMonitor = CrashMonitor { crashDetails ->
            throw Exception(crashDetails)
        }
        try {
            crashMonitor.monitor(applicationInfo.dataDir)
        } catch (e: Exception) {
            Timber.e("Couldn't monitor for crashes: ${e.message}")
        }
    }
}
