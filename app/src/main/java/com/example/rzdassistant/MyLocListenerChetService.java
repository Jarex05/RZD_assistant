package com.example.rzdassistant;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.location.LocationListenerCompat;

import java.util.List;

public class MyLocListenerChetService implements LocationListener {
    private LocListenerInterfaceService locListenerInterfaceService;
    @Override
    public void onLocationChanged(@NonNull Location location) {
        locListenerInterfaceService.OnLocationChanged(location);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    public void setLocListenerInterfaceService(LocListenerInterfaceService locListenerInterfaceService) {
        this.locListenerInterfaceService = locListenerInterfaceService;
    }
}
