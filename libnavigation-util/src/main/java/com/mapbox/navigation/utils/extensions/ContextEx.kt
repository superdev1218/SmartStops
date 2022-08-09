@file:JvmName("ContextEx")

package com.mapbox.navigation.utils.extensions

import android.content.Context
import android.os.Build
import com.mapbox.navigation.utils.SmartStopsPrefLanguage
import java.util.*

/**
 * Returns the device language to default to if no locale was specified
 *
 * @return language of device
 */
fun Context.inferDeviceLanguage(): String = inferDeviceLocale().language

/**
 * Returns the device locale for which to use as a default if no language is specified
 *
 * @return locale of device
 */
fun Context.inferDeviceLocale(): Locale =

        when (SmartStopsPrefLanguage(applicationContext).getString("lang")) {
            "english" -> Locale("eng")  //Locale.US
            "german" -> Locale.GERMAN
            "french" -> Locale.FRENCH
            "mandarin" -> Locale("cmn") //Locale.CHINESE
            "cantonoese" -> Locale("yue") //Locale.CHINESE
            else -> {
                Locale("eng")
            }
        }
/*
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    this.resources.configuration.locales.get(0)
} else {
    this.resources.configuration.locale
}*/


