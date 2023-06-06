package com.example.rzdassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rzdassistant.adapter.ListItemChet;
import com.example.rzdassistant.adapter.MainAdapterChet;
import com.example.rzdassistant.db.AppExecuter;
import com.example.rzdassistant.db.MyDbManagerChet;
import com.example.rzdassistant.db.OnDataReceived;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocListenerInterfaceChet, OnDataReceived {

//    Button btnStart, btnStop;

    private SharedPreferences prefChet;
    private SharedPreferences prefChetUsl;
    private final String save_key_chet = "save_key_chet";
    private final String save_key_chet_usl = "save_key_chet_usl";

    private MyDbManagerChet myDbManagerChet;
    private RecyclerView rcViewChet;
    private MainAdapterChet mainAdapterChet;

    private LocationManager locationManagerChet;
    private MyLocListenerChet myLocListenerChet;
    private Location lastLocationChet;
    private TextView tvDistanceChet;
    private TextView tvVelocityChet;
    private TextView tvStartChet;

    private int progressBarChet;
    private int start_distance_chet = 0;
    private int getStart_distance_chet2;
    public int i;
    public int j;
    public int km;
    public int pk;
    public int w;
    private int start_distance_nechet;
    private int distanceStationStart = 0;
    private int distanceStationFinish = 9999999;
    private ProgressBar pb;

    private TextView tvNameChet;
    private TextView tvSpeedChet;

    private TextView tvInfoChet;
    private TextView tvRowChet;

    private TextView tv_proba_chet;

    private TextView faktKm;
    private int faktKmProsledovanie;
    private int faktPiketProsledovanie;
    public int faktNachKm;
    public int piketNachKm;
    public int faktEndKm;
    public int piketEndKm;

    private TextView tvUslChet;
    private int uslDlChet;
    private int sumCalculateUslChet = 1;

    DBHelper dbHelper;

    private MediaPlayer ogr15, ogr25, ogr40, ogr50, ogr55, ogr60, ogr65, ogr70, ogr75, probatormozov, probatormozov2, ojevlenie, voice15, voice25, voice40, voice50, voice55, voice60, voice65, voice70, voice75, voiceprev, tokopriemniki, tokopriemniki2, songvipolneno;

    private int chet2;

    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Активно = " + savedInstanceState);
        init();
    }

    private void init()
    {


        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("Test_Channel",
                    "Test Description",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Intent intent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new NotificationCompat.Builder(this, "Test_Channel")
                    .setContentTitle("RZD assistant")
                    .setContentText("RZD assistant продолжает работу")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            notificationManager.notify(101, notification);
        }


//        Intent intent = new Intent(this, MyServiceChet.class);
//        startService(intent.putExtra("age", 500));

        int Chet = getIntent().getIntExtra("Chet", 0);
        start_distance_chet = Chet;
        Log.d(LOG_TAG, "Данные полученные с активити логирования  = " + start_distance_chet);

        int Chet1 = getIntent().getIntExtra("Nechet", 0);
        chet2 = Chet1;
        Log.d(LOG_TAG, "Данные полученные с активити логирования для возвращения нечётному активити  = " + chet2);

        int Chet3 = getIntent().getIntExtra("chetIsNechet", 0);
        getStart_distance_chet2 = Chet3;
        Log.d(LOG_TAG, "Данные полученные с активити логирования chetIsNechet  = " + getStart_distance_chet2);
        if (start_distance_chet == 0) {
            start_distance_chet = getStart_distance_chet2;
        }

        myDbManagerChet = new MyDbManagerChet(this);
        rcViewChet = findViewById(R.id.rcViewChet);
        mainAdapterChet = new MainAdapterChet(this);
        rcViewChet.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelperChet().attachToRecyclerView(rcViewChet);
        rcViewChet.setAdapter(mainAdapterChet);

        locationManagerChet = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListenerChet = new MyLocListenerChet();
        myLocListenerChet.setLocListenerInterfaceChet(this);
        tvStartChet = findViewById(R.id.tvStartChet);

        prefChet = getSharedPreferences("chet", MODE_PRIVATE);
        prefChetUsl = getSharedPreferences("chetusl", MODE_PRIVATE);
        tvDistanceChet = findViewById(R.id.tvDistanceChet);
//        tvDistanceChet.setText(prefChet.getString("save_key_chet", "0"));
//        getStart_distance_chet2 = Integer.parseInt(prefChet.getString("save_key_chet", "0"));
//        start_distance_chet = Integer.parseInt(prefChet.getString("save_key_chet", "0"));
        tvUslChet = findViewById(R.id.tvUslChet);
        tvUslChet.setText(prefChetUsl.getString("save_key_chet_usl", "0"));
        uslDlChet = Integer.parseInt(prefChetUsl.getString("save_key_chet_usl", "0"));



        tvVelocityChet = findViewById(R.id.tvVelocityChet);

        tvNameChet = findViewById(R.id.tvNameChet);
        tvSpeedChet = findViewById(R.id.tvSpeedChet);

        tvInfoChet = findViewById(R.id.tvInfoChet);
        tvRowChet = findViewById(R.id.tvRowChet);

        tv_proba_chet = findViewById(R.id.tv_proba_chet);

        faktKm = findViewById(R.id.faktKm);

        dbHelper = new DBHelper(this);

        probatormozov = MediaPlayer.create(this, R.raw.probatormozov);
        probatormozov2 = MediaPlayer.create(this, R.raw.probatormozov2);
        voice15 = MediaPlayer.create(this, R.raw.voice15);
        voice25 = MediaPlayer.create(this, R.raw.voice25);
        voice40 = MediaPlayer.create(this, R.raw.voice40);
        voice50 = MediaPlayer.create(this, R.raw.voice50);
        voice55 = MediaPlayer.create(this, R.raw.voice55);
        voice60 = MediaPlayer.create(this, R.raw.voice60);
        voice65 = MediaPlayer.create(this, R.raw.voice65);
        voice70 = MediaPlayer.create(this, R.raw.voice70);
        voice75 = MediaPlayer.create(this, R.raw.voice75);
        voiceprev = MediaPlayer.create(this, R.raw.voiceprev);
        tokopriemniki = MediaPlayer.create(this, R.raw.tokopriemniki);
        tokopriemniki2 = MediaPlayer.create(this, R.raw.tokopriemniki2);
        ogr15 = MediaPlayer.create(this, R.raw.ogr15);
        ogr25 = MediaPlayer.create(this, R.raw.ogr25);
        ogr40 = MediaPlayer.create(this, R.raw.ogr40);
        ogr50 = MediaPlayer.create(this, R.raw.ogr50);
        ogr55 = MediaPlayer.create(this, R.raw.ogr55);
        ogr60 = MediaPlayer.create(this, R.raw.ogr60);
        ogr65 = MediaPlayer.create(this, R.raw.ogr65);
        ogr70 = MediaPlayer.create(this, R.raw.ogr70);
        ogr75 = MediaPlayer.create(this, R.raw.ogr75);
        ojevlenie = MediaPlayer.create(this, R.raw.ojevlenie);
        songvipolneno = MediaPlayer.create(this, R.raw.songvipolneno);

        pb = findViewById(R.id.progressBarChet);
        pb.setMax(0);

        checkPermissions();
    }

//    public void onClickStart(View view) {
//        startService(new Intent(this, MyServiceChet.class));
//    }
//
//    public void onClickStop(View view) {
//        stopService(new Intent(this, MyServiceChet.class));
//    }

    // Начало условной длины

    private void setUslChet(String uslChet)
    {
        tvUslChet.setText(uslChet);
        uslDlChet = Integer.parseInt(uslChet);
    }

    private <ConstrainLayout> void showDialogUslChet()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_usl_chet);
        ConstrainLayout cl = (ConstrainLayout) getLayoutInflater().inflate(R.layout.dialog_layout_usl_chet,null);
        builder.setPositiveButton(R.string.dialog_button_usl_chet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog uslchet = (AlertDialog) dialog;
                EditText edsuslchet = uslchet.findViewById(R.id.edUslChet);
                if(edsuslchet != null){
                    if(!edsuslchet.getText().toString().equals(""))setUslChet(edsuslchet.getText().toString());
                }
            }
        });
        builder.setView((View) cl);
        builder.show();
    }

    public void onClickUslChet(View view)
    {
        showDialogUslChet();
    }

    // Конец условной длины

    private void setDistanceStartChet(String disStart)
    {
                tvStartChet.setText(disStart);
                start_distance_chet = Integer.parseInt(disStart);
                tvDistanceChet.setText(disStart);
                pb.setMax(distanceStationFinish - start_distance_chet);
    }

    private <ConstrainLayout> void showDialogStartChet()
    {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_title_start_chet);
                ConstrainLayout cl = (ConstrainLayout) getLayoutInflater().inflate(R.layout.dialog_layout_start_chet,null);
                builder.setPositiveButton(R.string.dialog_button_start_chet, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog chet = (AlertDialog) dialog;
                        EditText edschet = chet.findViewById(R.id.edStartChet);
                        tvInfoChet.setText("");
                        tvNameChet.setText("");
                        tvRowChet.setText("");
                        tvSpeedChet.setText("");
                        if(edschet != null){
                            if(!edschet.getText().toString().equals(""))setDistanceStartChet(edschet.getText().toString());
                        }
                    }
                });
                builder.setView((View) cl);
                builder.show();
    }

    public void onClickDistanceStartChet(View view)

    {
        showDialogStartChet();
    }

    @SuppressLint("SetTextI18n")
    private void updateDistanceChet(Location locChet)
    {
        if (locChet.hasSpeed() && lastLocationChet != null)
        {

            progressBarChet = distanceStationFinish - start_distance_chet;
            sumCalculateUslChet = (uslDlChet * 14) + 50;

                if (start_distance_chet != 0 && (Math.round((locChet.getSpeed() / 1000) * 3600)) >= 1) {
                    if (start_distance_chet < distanceStationFinish) {
                        start_distance_chet += lastLocationChet.distanceTo(locChet) + 0.45;
                        pb.setProgress(progressBarChet);
                    }
                }


        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                Cursor cursor = database.query(dbHelper.TABLE_CONTACTS_CHET, null, null, null, null, null, null);

                while (cursor.moveToNext()) {
                    int titleChet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelper.KEY_CHET_NAME))));
                    int piketstartChet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelper.KEY_CHET_NAME_START_PIKET))));
                    int title_finishChet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelper.KEY_CHET_NAME_FINISH))));
                    int piketfinishChet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelper.KEY_CHET_NAME_FINISH_PIKET))));
                    int speedChet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelper.KEY_CHET_SPEED))));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // Расчёт начала ограничений по киллометро

                            int z = 1001, kmc = 2, pkc = 1;
                            while (z < 9999999) {

                                z = z + 1000;
                                kmc = kmc + 1;

                                if (titleChet == kmc && piketstartChet == pkc) {
                                    faktNachKm = z;
                                    piketNachKm = pkc;
                                }

                            }


                            int z1 = 1101, kmc1 = 2, pkc1 = 2;
                            while (z1 < 9999999) {

                                z1 = z1 + 1000;
                                kmc1 = kmc1 + 1;

                                if (titleChet == kmc1 && piketstartChet == pkc1) {
                                    faktNachKm = z1;
                                    piketNachKm = pkc1;
                                }

                            }

                            int z2 = 1201, kmc2 = 2, pkc2 = 3;
                            while (z2 < 9999999) {

                                z2 = z2 + 1000;
                                kmc2 = kmc2 + 1;

                                if (titleChet == kmc2 && piketstartChet == pkc2) {
                                    faktNachKm = z2;
                                    piketNachKm = pkc2;
                                }

                            }

                            int z3 = 1301, kmc3 = 2, pkc3 = 4;
                            while (z3 < 9999999) {

                                z3 = z3 + 1000;
                                kmc3 = kmc3 + 1;

                                if (titleChet == kmc3 && piketstartChet == pkc3) {
                                    faktNachKm = z3;
                                    piketNachKm = pkc3;
                                }

                            }

                            int z4 = 1401, kmc4 = 2, pkc4 = 5;
                            while (z4 < 9999999) {

                                z4 = z4 + 1000;
                                kmc4 = kmc4 + 1;

                                if (titleChet == kmc4 && piketstartChet == pkc4) {
                                    faktNachKm = z4;
                                    piketNachKm = pkc4;
                                }

                            }

                            int z5 = 1501, kmc5 = 2, pkc5 = 6;
                            while (z5 < 9999999) {

                                z5 = z5 + 1000;
                                kmc5 = kmc5 + 1;

                                if (titleChet == kmc5 && piketstartChet == pkc5) {
                                    faktNachKm = z5;
                                    piketNachKm = pkc5;
                                }

                            }

                            int z6 = 1601, kmc6 = 2, pkc6 = 7;
                            while (z6 < 9999999) {

                                z6 = z6 + 1000;
                                kmc6 = kmc6 + 1;

                                if (titleChet == kmc6 && piketstartChet == pkc6) {
                                    faktNachKm = z6;
                                    piketNachKm = pkc6;
                                }

                            }

                            int z7 = 1701, kmc7 = 2, pkc7 = 8;
                            while (z7 < 9999999) {

                                z7 = z7 + 1000;
                                kmc7 = kmc7 + 1;

                                if (titleChet == kmc7 && piketstartChet == pkc7) {
                                    faktNachKm = z7;
                                    piketNachKm = pkc7;
                                }

                            }

                            int z8 = 1801, kmc8 = 2, pkc8 = 9;
                            while (z8 < 9999999) {

                                z8 = z8 + 1000;
                                kmc8 = kmc8 + 1;

                                if (titleChet == kmc8 && piketstartChet == pkc8) {
                                    faktNachKm = z8;
                                    piketNachKm = pkc8;
                                }

                            }

                            int z9 = 1901, kmc9 = 2, pkc9 = 10;
                            while (z9 < 9999999) {

                                z9 = z9 + 1000;
                                kmc9 = kmc9 + 1;

                                if (titleChet == kmc9 && piketstartChet == pkc9) {
                                    faktNachKm = z9;
                                    piketNachKm = pkc9;
                                }

                            }

                            // Конец расчёта начала ограничений по киллометро


                            // Расчёт конца ограничений по киллометро


                            int x = 1099, kmx = 2, pkx = 1;
                            while (x < 9999999) {

                                x = x + 1000;
                                kmx = kmx + 1;

                                if (title_finishChet == kmx && piketfinishChet == pkx) {
                                    faktEndKm = x;
                                    piketEndKm = pkx;
                                }

                            }

                            int x1 = 1199, kmx1 = 2, pkx1 = 2;
                            while (x1 < 9999999) {

                                x1 = x1 + 1000;
                                kmx1 = kmx1 + 1;

                                if (title_finishChet == kmx1 && piketfinishChet == pkx1) {
                                    faktEndKm = x1;
                                    piketEndKm = pkx1;
                                }

                            }

                            int x2 = 1299, kmx2 = 2, pkx2 = 3;
                            while (x2 < 9999999) {

                                x2 = x2 + 1000;
                                kmx2 = kmx2 + 1;

                                if (title_finishChet == kmx2 && piketfinishChet == pkx2) {
                                    faktEndKm = x2;
                                    piketEndKm = pkx2;
                                }

                            }

                            int x3 = 1399, kmx3 = 2, pkx3 = 4;
                            while (x3 < 9999999) {

                                x3 = x3 + 1000;
                                kmx3 = kmx3 + 1;

                                if (title_finishChet == kmx3 && piketfinishChet == pkx3) {
                                    faktEndKm = x3;
                                    piketEndKm = pkx3;
                                }

                            }

                            int x4 = 1499, kmx4 = 2, pkx4 = 5;
                            while (x4 < 9999999) {

                                x4 = x4+ 1000;
                                kmx4 = kmx4 + 1;

                                if (title_finishChet == kmx4 && piketfinishChet == pkx4) {
                                    faktEndKm = x4;
                                    piketEndKm = pkx4;
                                }

                            }

                            int x5 = 1599, kmx5 = 2, pkx5 = 6;
                            while (x5 < 9999999) {

                                x5 = x5 + 1000;
                                kmx5 = kmx5 + 1;

                                if (title_finishChet == kmx5 && piketfinishChet == pkx5) {
                                    faktEndKm = x5;
                                    piketEndKm = pkx5;
                                }

                            }

                            int x6 = 1699, kmx6 = 2, pkx6 = 7;
                            while (x6 < 9999999) {

                                x6 = x6 + 1000;
                                kmx6 = kmx6 + 1;

                                if (title_finishChet == kmx6 && piketfinishChet == pkx6) {
                                    faktEndKm = x6;
                                    piketEndKm = pkx6;
                                }

                            }

                            int x7 = 1799, kmx7 = 2, pkx7 = 8;
                            while (x7 < 9999999) {

                                x7 = x7 + 1000;
                                kmx7 = kmx7 + 1;

                                if (title_finishChet == kmx7 && piketfinishChet == pkx7) {
                                    faktEndKm = x7;
                                    piketEndKm = pkx7;
                                }

                            }

                            int x8 = 1899, kmx8 = 2, pkx8 = 9;
                            while (x8 < 9999999) {

                                x8 = x8 + 1000;
                                kmx8 = kmx8 + 1;

                                if (title_finishChet == kmx8 && piketfinishChet == pkx8) {
                                    faktEndKm = x8;
                                    piketEndKm = pkx8;
                                }

                            }

                            int x9 = 1999, kmx9 = 2, pkx9 = 10;
                            while (x9 < 9999999) {

                                x9 = x9 + 1000;
                                kmx9 = kmx9 + 1;

                                if (title_finishChet == kmx9 && piketfinishChet == pkx9) {
                                    faktEndKm = x9;
                                    piketEndKm = pkx9;
                                }

                            }

                            // Конец расчёта конца ограничений по киллометро



                            // Начало опускания токоприемников

                            if (start_distance_chet >= faktNachKm - 5000 && start_distance_chet <= faktNachKm - 4900 && title_finishChet == 00 && piketfinishChet == 00 && speedChet == 02) {
                                soundPlay(tokopriemniki);
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1900 && title_finishChet == 00 && piketfinishChet == 00 && speedChet == 02) {
                                soundPlay(tokopriemniki2);
                            }

                            // Конец опускания токоприемников


                            // Начало пробы тормозов

                            if (start_distance_chet >= faktNachKm - 5000 && start_distance_chet <= faktNachKm - 4900 && title_finishChet == 00 && piketfinishChet == 00 && speedChet == 00) {
                                soundPlay(probatormozov);
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1900 && title_finishChet == 00 && piketfinishChet == 00 && speedChet == 00) {
                                soundPlay(probatormozov2);
                                tv_proba_chet.setText("Проба тормозов");
                            }

                            if (start_distance_chet >= faktNachKm && start_distance_chet <= faktNachKm + 50 && title_finishChet == 00 && piketfinishChet == 00 && speedChet == 00) {
                                tv_proba_chet.setText("");
                            }

                            // Конец пробы тормозов

                            // Начало оживления тормозов

                            if (start_distance_chet >= faktNachKm - 3000 && start_distance_chet <= faktNachKm - 2900 && title_finishChet == 00 && piketfinishChet == 00 && speedChet == 01) {
                                soundPlay(ojevlenie);
                                tv_proba_chet.setText("Оживление тормозов");
                            }

                            if (start_distance_chet >= faktNachKm && start_distance_chet <= faktNachKm + 70 && title_finishChet == 00 && piketfinishChet == 00 && speedChet == 01) {
                                tv_proba_chet.setText("");
                            }

                            // Конец оживления тормозов




                            // Начало высвечивания выполненного предупреждения на экране, а так же стирание по порядку предупреждения

                            if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 80 + sumCalculateUslChet) && speedChet == 15) {
                                tvNameChet.setText("");
                                tvRowChet.setText("");
                                tvSpeedChet.setText("");
                                tvInfoChet.setText("");
                                tvInfoChet.setText(titleChet + " " + piketstartChet + " -- " + title_finishChet + " " + piketfinishChet + " => " + speedChet + " Выполнено!");
                                if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 35 + sumCalculateUslChet) && speedChet == 15) {
                                    soundPlay(songvipolneno);
                                }
                            }
                            else if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 80 + sumCalculateUslChet) && speedChet == 25) {
                                tvNameChet.setText("");
                                tvRowChet.setText("");
                                tvSpeedChet.setText("");
                                tvInfoChet.setText("");
                                tvInfoChet.setText(titleChet + " " + piketstartChet + " -- " + title_finishChet + " " + piketfinishChet + " => " + speedChet + " Выполнено!");
                                if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 35 + sumCalculateUslChet) && speedChet == 25) {
                                    soundPlay(songvipolneno);
                                }
                            }
                            else if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 80 + sumCalculateUslChet) && speedChet == 40) {
                                tvNameChet.setText("");
                                tvRowChet.setText("");
                                tvSpeedChet.setText("");
                                tvInfoChet.setText("");
                                tvInfoChet.setText(titleChet + " " + piketstartChet + " -- " + title_finishChet + " " + piketfinishChet + " => " + speedChet + " Выполнено!");
                                if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 35 + sumCalculateUslChet) && speedChet == 40) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            else if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 80 + sumCalculateUslChet) && speedChet == 50) {
                                tvNameChet.setText("");
                                tvRowChet.setText("");
                                tvSpeedChet.setText("");
                                tvInfoChet.setText("");
                                tvInfoChet.setText(titleChet + " " + piketstartChet + " -- " + title_finishChet + " " + piketfinishChet + " => " + speedChet + " Выполнено!");
                                if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 35 + sumCalculateUslChet) && speedChet == 50) {
                                    soundPlay(songvipolneno);
                                }
                            }
                            else if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 80 + sumCalculateUslChet) && speedChet == 55) {
                                tvNameChet.setText("");
                                tvRowChet.setText("");
                                tvSpeedChet.setText("");
                                tvInfoChet.setText("");
                                tvInfoChet.setText(titleChet + " " + piketstartChet + " -- " + title_finishChet + " " + piketfinishChet + " => " + speedChet + " Выполнено!");
                                if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 35 + sumCalculateUslChet) && speedChet == 55) {
                                    soundPlay(songvipolneno);
                                }
                            }
                            else if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 80 + sumCalculateUslChet) && speedChet == 60) {
                                tvNameChet.setText("");
                                tvRowChet.setText("");
                                tvSpeedChet.setText("");
                                tvInfoChet.setText("");
                                tvInfoChet.setText(titleChet + " " + piketstartChet + " -- " + title_finishChet + " " + piketfinishChet + " => " + speedChet + " Выполнено!");
                                if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 35 + sumCalculateUslChet) && speedChet == 60) {
                                    soundPlay(songvipolneno);
                                }
                            }
                            else if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 80 + sumCalculateUslChet) && speedChet == 65) {
                                tvNameChet.setText("");
                                tvRowChet.setText("");
                                tvSpeedChet.setText("");
                                tvInfoChet.setText("");
                                tvInfoChet.setText(titleChet + " " + piketstartChet + " -- " + title_finishChet + " " + piketfinishChet + " => " + speedChet + " Выполнено!");
                                if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 35 + sumCalculateUslChet) && speedChet == 65) {
                                    soundPlay(songvipolneno);
                                }
                            }
                            else if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 80 + sumCalculateUslChet) && speedChet == 70) {
                                tvNameChet.setText("");
                                tvRowChet.setText("");
                                tvSpeedChet.setText("");
                                tvInfoChet.setText("");
                                tvInfoChet.setText(titleChet + " " + piketstartChet + " -- " + title_finishChet + " " + piketfinishChet + " => " + speedChet + " Выполнено!");
                                if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 35 + sumCalculateUslChet) && speedChet == 70) {
                                    soundPlay(songvipolneno);
                                }
                            }
                            else if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 80 + sumCalculateUslChet) && speedChet == 75) {
                                tvNameChet.setText("");
                                tvRowChet.setText("");
                                tvSpeedChet.setText("");
                                tvInfoChet.setText("");
                                tvInfoChet.setText(titleChet + " " + piketstartChet + " -- " + title_finishChet + " " + piketfinishChet + " => " + speedChet + " Выполнено!");
                                if (start_distance_chet >= (faktEndKm + sumCalculateUslChet) && start_distance_chet <= (faktEndKm + 35 + sumCalculateUslChet) && speedChet == 75) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            // Конец высвечивания выполненного предупреждения на экране, а так же стирания по порядку предупреждения




                            // Предупреждение о превышении ограничения скорости

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm && start_distance_chet <= (faktEndKm + sumCalculateUslChet) && speedChet == 15) {
                                soundPlay(ogr15);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm && start_distance_chet <= (faktEndKm + sumCalculateUslChet) && speedChet == 25) {
                                soundPlay(ogr25);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm && start_distance_chet <= (faktEndKm + sumCalculateUslChet) && speedChet == 40) {
                                soundPlay(ogr40);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm && start_distance_chet <= (faktEndKm + sumCalculateUslChet) && speedChet == 50) {
                                soundPlay(ogr50);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm && start_distance_chet <= (faktEndKm + sumCalculateUslChet) && speedChet == 55) {
                                soundPlay(ogr55);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm && start_distance_chet <= (faktEndKm + sumCalculateUslChet) && speedChet == 60) {
                                soundPlay(ogr60);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm && start_distance_chet <= (faktEndKm + sumCalculateUslChet) && speedChet == 65) {
                                soundPlay(ogr65);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm && start_distance_chet <= (faktEndKm + sumCalculateUslChet) && speedChet == 70) {
                                soundPlay(ogr70);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm && start_distance_chet <= (faktEndKm + sumCalculateUslChet) && speedChet == 75) {
                                soundPlay(ogr75);
                            }

                            // Конец предупреждения о превышении ограничения скорости


                            // Начало оповещения о снижении скорости за 400 метров до ограничения

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm - 400 && start_distance_chet <= faktNachKm - 300 && speedChet == 15) {
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm - 400 && start_distance_chet <= faktNachKm - 300 && speedChet == 25) {
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm - 400 && start_distance_chet <= faktNachKm - 300 && speedChet == 40) {
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm - 400 && start_distance_chet <= faktNachKm - 300 && speedChet == 50) {
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm - 400 && start_distance_chet <= faktNachKm - 300 && speedChet == 55) {
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm - 400 && start_distance_chet <= faktNachKm - 300 && speedChet == 60) {
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm - 400 && start_distance_chet <= faktNachKm - 300 && speedChet == 65) {
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm - 400 && start_distance_chet <= faktNachKm - 300 && speedChet == 70) {
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locChet.getSpeed() / 1000 * 3600) >= speedChet && start_distance_chet >= faktNachKm - 400 && start_distance_chet <= faktNachKm - 300 && speedChet == 75) {
                                soundPlay(voiceprev);
                            }

                            // Конец оповещения о снижении скорости за 400 метров до ограничения





                            // Начало ограничений скорости


                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1902 && speedChet == 15) {
                                soundPlay(voice15);
                                tvInfoChet.setText("");
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= (faktEndKm + 30 + sumCalculateUslChet) && speedChet == 15) {
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            //---------------------------------------------------------------------------------------------


                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1902 && speedChet == 25) {
                                soundPlay(voice25);
                                tvInfoChet.setText("");
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= (faktEndKm + 30 + sumCalculateUslChet) && speedChet == 25) {
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            //---------------------------------------------------------------------------------------------

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1902 && speedChet == 40) {
                                soundPlay(voice40);
                                tvInfoChet.setText("");
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= (faktEndKm + 30 + sumCalculateUslChet) && speedChet == 40) {
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            //---------------------------------------------------------------------------------------------


                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1902 && speedChet == 50) {
                                soundPlay(voice50);
                                tvInfoChet.setText("");
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= (faktEndKm + 30 + sumCalculateUslChet) && speedChet == 50) {
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            //---------------------------------------------------------------------------------------------


                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1902 && speedChet == 55) {
                                soundPlay(voice55);
                                tvInfoChet.setText("");
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= (faktEndKm + 30 + sumCalculateUslChet) && speedChet == 55) {
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            //---------------------------------------------------------------------------------------------


                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1902 && speedChet == 60) {
                                soundPlay(voice60);
                                tvInfoChet.setText("");
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= (faktEndKm + 30 + sumCalculateUslChet) && speedChet == 60) {
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            //---------------------------------------------------------------------------------------------


                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1902 && speedChet == 65) {
                                soundPlay(voice65);
                                tvInfoChet.setText("");
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= (faktEndKm + 30 + sumCalculateUslChet) && speedChet == 65) {
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            //---------------------------------------------------------------------------------------------


                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1902 && speedChet == 70) {
                                soundPlay(voice70);
                                tvInfoChet.setText("");
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= (faktEndKm + 30 + sumCalculateUslChet) && speedChet == 70) {
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            //---------------------------------------------------------------------------------------------


                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= faktNachKm - 1902 && speedChet == 75) {
                                soundPlay(voice75);
                                tvInfoChet.setText("");
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            if (start_distance_chet >= faktNachKm - 2000 && start_distance_chet <= (faktEndKm + 30 + sumCalculateUslChet) && speedChet == 75) {
                                tvNameChet.setText(titleChet + " км " + piketstartChet + " пк " + " -- " + title_finishChet + " км " + piketfinishChet + " пк ");
                                tvRowChet.setText("=>");
                                tvSpeedChet.setText(String.valueOf(speedChet));
                            }

                            // Конец ограничения скорости


                        }
                    });


                }

                cursor.close();
                database.close();

            }
        }).start();


            int q = 1000, q1 = 1100, q2 = 1200, q3 = 1300, q4 = 1400, q5 = 1500, q6 = 1600, q7 = 1700, q8 = 1800, q9 = 1900, w = 1100, w1 = 1200, w2 = 1300, w3 = 1400, w4 = 1500, w5 = 1600, w6 = 1700, w7 = 1800, w8 = 1900, w9 = 2000, kme = 2, pke = 1;
            while (q < 9999999) {
                q = q + 1000;
                q1 = q1 + 1000;
                q2 = q2 + 1000;
                q3 = q3 + 1000;
                q4 = q4 + 1000;
                q5 = q5 + 1000;
                q6 = q6 + 1000;
                q7 = q7 + 1000;
                q8 = q8 + 1000;
                q9 = q9 + 1000;

                w = w + 1000;
                w1 = w1 + 1000;
                w2 = w2 + 1000;
                w3 = w3 + 1000;
                w4 = w4 + 1000;
                w5 = w5 + 1000;
                w6 = w6 + 1000;
                w7 = w7 + 1000;
                w8 = w8 + 1000;
                w9 = w9 + 1000;

                kme = kme + 1;

                // Цикл подсчёта киллометров

                if (start_distance_chet >= q && start_distance_chet <= w) {
                    pke = 1;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                if (start_distance_chet >= q1 && start_distance_chet <= w1) {
                    pke = 2;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                if (start_distance_chet >= q2 && start_distance_chet <= w2) {
                    pke = 3;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                if (start_distance_chet >= q3 && start_distance_chet <= w3) {
                    pke = 4;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                if (start_distance_chet >= q4 && start_distance_chet <= w4) {
                    pke = 5;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                if (start_distance_chet >= q5 && start_distance_chet <= w5) {
                    pke = 6;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                if (start_distance_chet >= q6 && start_distance_chet <= w6) {
                    pke = 7;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                if (start_distance_chet >= q7 && start_distance_chet <= w7) {
                    pke = 8;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                if (start_distance_chet >= q8 && start_distance_chet <= w8) {
                    pke = 9;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                if (start_distance_chet >= q9 && start_distance_chet <= w9) {
                    pke = 10;
                    faktKm.setText(kme + " км. " + pke + " пк. ");
                    faktKmProsledovanie = kme;
                    faktPiketProsledovanie = pke;
                }

                // Конец цикла подсчёта киллометров
            }

            lastLocationChet = locChet;
            tvDistanceChet.setText(String.valueOf(start_distance_chet));
            tvVelocityChet.setText(String.valueOf(Math.round((locChet.getSpeed() / 1000) * 3600)));


//        SharedPreferences.Editor edit = prefChet.edit();
        SharedPreferences.Editor editorusl = prefChetUsl.edit();
//        edit.putString("save_key_chet", tvDistanceChet.getText().toString());
        editorusl.putString("save_key_chet_usl", tvUslChet.getText().toString());
//        edit.apply();
        editorusl.apply();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == RESULT_OK)
        {
            checkPermissions();
        }
        else
        {
            //Toast.makeText(this, "Включите GPS!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissions()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            int requestCode = 100;
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
        }
        else
        {
            locationManagerChet.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, myLocListenerChet);
        }
    }

    @Override
    public void OnLocationChangedChet(Location locChet) {
        updateDistanceChet(locChet);
    }

    public void soundPlay(MediaPlayer sound)
    {
        sound.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myDbManagerChet.openDbChet();
        AppExecuter.getInstance().getSubIO().execute(new Runnable() {
            @Override
            public void run() {
                myDbManagerChet.getFromDbChet(MainActivity.this);
            }
        });
    }


    public void onClickAddChet(View view){
        Intent i_chet = new Intent(MainActivity.this, EditActivityChet.class);
        startActivity(i_chet);
    }

    public void onClickBtnNechet(View view)
    {

        Intent iChet = new Intent(MainActivity.this, MainActivityLogirovanie.class);
        startActivity(iChet.putExtra("chet1", start_distance_chet));
        startActivity(iChet.putExtra("chet2", chet2));
        start_distance_chet = 0;
        finish();
    }


    public void onclick_minus_chet(View view){
        start_distance_chet -= 50;
    }

    public void onclick_plus_chet(View view){
        start_distance_chet += 50;
    }

    protected void onDestroy() {

        super.onDestroy();
        myDbManagerChet.closeDbChet();
    }

    private ItemTouchHelper getItemTouchHelperChet(){
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapterChet.removeItemChet(viewHolder.getAdapterPosition(), myDbManagerChet);
            }
        });
    }

    @Override
    public void onReceived(List<ListItemChet> list) {
        AppExecuter.getInstance().getMainIO().execute(new Runnable() {
            @Override
            public void run() {
                mainAdapterChet.updateAdapterChet(list);
            }
        });
    }


}