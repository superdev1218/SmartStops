package com.creativeinfoway.smartstops.app.android.navigation.ui.v5.instruction.turnlane;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.R;

class TurnLaneViewHolder extends RecyclerView.ViewHolder {

  TurnLaneView turnLaneView;

  TurnLaneViewHolder(View itemView) {
    super(itemView);
    turnLaneView = itemView.findViewById(R.id.turnLaneView);
  }
}
