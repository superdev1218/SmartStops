package com.creativeinfoway.smartstops.app.android.navigation.ui.v5.instruction;

import com.mapbox.api.directions.v5.models.BannerComponents;

interface NodeVerifier {
  boolean isNodeType(BannerComponents bannerComponents);
}
