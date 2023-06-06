//package com.example.rzdassistant;
//
//import android.Manifest;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.util.Log;
//import android.location.Location;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//
//public class MyServiceChet extends Service implements LocListenerInterfaceService{
//
//    private LocationManager locationManager;
//    private Location lastLocation;
//    private MyLocListenerChetService myLocListenerChetService;
//
//    public int distance = 25;
//    private int distance2 = 5;
//
//    DBHelper dbHelper;
//
//    final String LOG_TAG = "myLogs";
//    private int m;
//
////    public MyServiceChet() {
////    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//
//        dbHelper = new DBHelper(this);
//        Log.d(LOG_TAG, "onCreate");
//
//        NotificationChannel channel = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            channel = new NotificationChannel("Test_Channel",
//                    "Test Description",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//
//            Intent intent = new Intent(this, MainActivity.class);
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//            Notification notification = new NotificationCompat.Builder(this, "Test_Channel")
//                    .setContentTitle("RZD assistant")
//                    .setContentText("RZD assistant продолжает работу")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
//                    .build();
//
//            notificationManager.notify(101, notification);
//        }
//    }
//    private void init(){
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        myLocListenerChetService = new MyLocListenerChetService();
//        myLocListenerChetService.setLocListenerInterfaceService(this);
//
//        checkPermissions();
//
//    }
//
//    private void checkPermissions()
//    {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//
//        }
//        else
//        {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, myLocListenerChetService);
//        }
//    }
//
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        int distance = intent.getIntExtra("age", 1);
//        Log.d(LOG_TAG, "Успешно onStartCommand  = " + distance);
//        somTask(intent);
//        init();
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d(LOG_TAG, "onDestroy");
//    }
//
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.d(LOG_TAG, "onBind");
//
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//    void somTask(Intent intent){
//        int distance = intent.getIntExtra("age", 1);
//        Log.d(LOG_TAG, "Успешно onStartCommand  = " + distance);
//        distance2 = distance;
//
//    }
//
//    @Override
//    public void onCreate(Location location) {
//
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//    }
//
//    @Override
//    public void OnLocationChanged(Location location) {
//        if (location.hasSpeed() && lastLocation != null){
//
//            distance2 += lastLocation.distanceTo(location);
//        }
//        lastLocation = location;
//        Log.d(LOG_TAG, "Успешно distance = " + distance2);
//    }
//}