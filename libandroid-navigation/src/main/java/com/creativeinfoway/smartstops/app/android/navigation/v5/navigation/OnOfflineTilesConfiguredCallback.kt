package com.creativeinfoway.smartstops.app.android.navigation.v5.navigation

/**
 * Listener that needs to be added to [MapboxOfflineRouter.configure] to know when
 * offline data is initialized an [MapboxOfflineRouter.findRoute] could be called.
 */
interface OnOfflineTilesConfiguredCallback {

    /**
     * Called whe the offline data is initialized and
     * [MapboxOfflineRouter.findRoute].
     * could be called safely.
     *
     * @param numberOfTiles initialized in the path provided
     */
    fun onConfigured(numberOfTiles: Int)

    /**
     * Called when an error has occurred configuring
     * the offline tile data.
     *
     * @param error with message explanation
     */
    fun onConfigurationError(error: OfflineError)
}
