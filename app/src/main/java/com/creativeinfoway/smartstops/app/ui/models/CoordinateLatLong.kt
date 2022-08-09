package com.creativeinfoway.smartstops.app.ui.models

import java.io.Serializable

class CoordinateLatLong : Serializable {
    var latitude : Double? = null
    var longitude : Double? = null

    constructor(latitude: Double?, longitude: Double?) {
        this.latitude = latitude
        this.longitude = longitude
    }
}