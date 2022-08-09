package com.creativeinfoway.smartstops.app.android.navigation.v5.navigation

import com.mapbox.geojson.BoundingBox

/**
 * Listener that needs to be added to
 * [MapboxOfflineRouter.removeTiles] to know when the routing
 * tiles within the provided [BoundingBox] have been removed
 */
interface OnOfflineTilesRemovedCallback {

    /**
     * Called when the routing tiles within the provided [BoundingBox] have been removed completely.
     *
     * @param numberOfTiles removed within the [BoundingBox] provided
     */
    fun onRemoved(numberOfTiles: Long)
}
