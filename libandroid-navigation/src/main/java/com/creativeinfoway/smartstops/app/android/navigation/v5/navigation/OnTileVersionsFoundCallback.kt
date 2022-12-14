package com.creativeinfoway.smartstops.app.android.navigation.v5.navigation

/**
 * Callback used with [MapboxOfflineRouter.fetchAvailableTileVersions].
 */
interface OnTileVersionsFoundCallback {

    /**
     * A list of available offline tile versions that can be used
     * with [MapboxOfflineRouter.downloadTiles].
     *
     * @param availableVersions for offline tiles
     */
    fun onVersionsFound(availableVersions: List<String>)

    /**
     * Called when an error has occurred fetching
     * offline versions.
     *
     * @param error with message explanation
     */
    fun onError(error: OfflineError)
}
