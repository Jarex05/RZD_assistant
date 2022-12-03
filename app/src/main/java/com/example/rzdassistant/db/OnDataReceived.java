package com.example.rzdassistant.db;

import com.example.rzdassistant.adapter.ListItemChet;

import java.util.List;

public interface OnDataReceived {
    void onReceived(List<ListItemChet> list);
}
