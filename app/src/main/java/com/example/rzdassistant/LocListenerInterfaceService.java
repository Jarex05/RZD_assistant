package com.example.rzdassistant;

import android.location.Location;
import android.os.Bundle;

public interface LocListenerInterfaceService {
    void onCreate(Location location);

    void onCreate(Bundle savedInstanceState);

    public void OnLocationChanged(Location location);
}
