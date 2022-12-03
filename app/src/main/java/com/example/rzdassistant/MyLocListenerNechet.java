package com.example.rzdassistant;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.List;

public class MyLocListenerNechet implements LocationListener {
    private LocListenerInterfaceNechet locListenerInterfaceNechet;
    @Override
    public void onLocationChanged(@NonNull Location location) {
        locListenerInterfaceNechet.OnLocationChangedNechet(location);
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

    public void setLocListenerInterfaceNechet(LocListenerInterfaceNechet locListenerInterfaceNechet) {
        this.locListenerInterfaceNechet = locListenerInterfaceNechet;
    }
}
