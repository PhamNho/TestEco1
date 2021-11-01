package com.nhodev.service_power.listener;

import com.nhodev.service_power.models.MusicModel;

/**
 * Được tạo bởi Phạm Nhớ ngày 01/11/2021.
 **/
public interface OnRecyclerViewItemClickListener {
    void onRecyclerViewItemClicked(MusicModel model, int position);
}
