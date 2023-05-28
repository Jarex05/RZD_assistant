package com.example.rzdassistant;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rzdassistant.adapter.ListItemNechet;
import com.example.rzdassistant.adapter.MainAdapterNechet;
import com.example.rzdassistant.db.AppExecuter;
import com.example.rzdassistant.db.AppExecuterNechet;
import com.example.rzdassistant.db.MyDbManagerNechet;
import com.example.rzdassistant.db.OnDataReceivedNechet;

import java.util.List;

public class MainActivityNechet extends AppCompatActivity implements LocListenerInterfaceNechet, OnDataReceivedNechet {

    private MyDbManagerNechet myDbManagerNechet;
    private RecyclerView rcViewNechet;
    private MainAdapterNechet mainAdapterNechet;

    private LocationManager locationManagerNechet;
    private MyLocListenerNechet myLocListenerNechet;
    private Location lastLocationNechet;
    private TextView tvDistanceNechet, tvVelocityNechet, tvStartNechet;

    private int progressBarNechet;
    private int start_distance_chet;
    private int start_distance_nechet = 0;
    private int distanceStationStart = 0;
    private int distanceStationFinish = 9999999;
//    private int distanceStationStart = 6904000;
//    private int distanceStationFinish = 7208000;
    private ProgressBar pb;

    private TextView tvNameNechet;
    private TextView tvSpeedNechet;

    private TextView tvInfoNechet;
    private TextView tvRowNechet;

    private TextView tv_proba_nechet;

    private TextView faktKmNechet;
    private int faktKmProsledovanieNechet;
    private int faktPiketProsledovanieNechet;
    public int faktNachKmNechet;
    public int piketNachKmNechet;
    public int faktEndKmNechet;
    public int piketEndKmNechet;

    private TextView tvUslNechet;
    private int uslDlNechet;
    private int sumCalculateUslNechet = 1;

    private MediaPlayer ogr15, ogr25, ogr40, ogr50, ogr55, ogr60, ogr65, ogr70, ogr75, probatormozov, probatormozov2, ojevlenie, voice15, voice25, voice40, voice50, voice55, voice60, voice65, voice70, voice75, voiceprev, tokopriemniki, tokopriemniki2, songvipolneno;

    DBHelperNechet dbHelperNechet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nechet);
        init();
    }

    private void init()
    {
        myDbManagerNechet = new MyDbManagerNechet(this);
        rcViewNechet = findViewById(R.id.rcViewNechet);
        mainAdapterNechet = new MainAdapterNechet(this);
        rcViewNechet.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelperNechet().attachToRecyclerView(rcViewNechet);
        rcViewNechet.setAdapter(mainAdapterNechet);

        locationManagerNechet = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListenerNechet = new MyLocListenerNechet();
        myLocListenerNechet.setLocListenerInterfaceNechet(this);
        tvStartNechet = findViewById(R.id.tvStartNechet);
        tvDistanceNechet = findViewById(R.id.tvDistanceNechet);
        tvVelocityNechet = findViewById(R.id.tvVelocityNechet);

        tvNameNechet = findViewById(R.id.tvNameNechet);
        tvSpeedNechet = findViewById(R.id.tvSpeedNechet);

        tvInfoNechet = findViewById(R.id.tvInfoNechet);
        tvRowNechet = findViewById(R.id.tvRowNechet);

        tv_proba_nechet = findViewById(R.id.tv_proba_nechet);

        faktKmNechet = findViewById(R.id.faktKmNechet);

        tvUslNechet = findViewById(R.id.tvUslNechet);

        dbHelperNechet = new DBHelperNechet(this);

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

        pb = findViewById(R.id.progressBarNechet);
        pb.setMax(0);

        checkPermissions();
    }



    // Начало условной длины

    private void setTvUslNechet(String uslNechet)
    {
        tvUslNechet.setText(uslNechet);
        uslDlNechet = Integer.parseInt(uslNechet);
    }

    private <ConstrainLayout> void showDialogUslNechet()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_usl_nechet);
        ConstrainLayout cl = (ConstrainLayout) getLayoutInflater().inflate(R.layout.dialog_layout_usl_nechet,null);
        builder.setPositiveButton(R.string.dialog_button_usl_nechet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog uslnechet = (AlertDialog) dialog;
                EditText edsuslnechet = uslnechet.findViewById(R.id.edUslNechet);
                if(edsuslnechet != null){
                    if(!edsuslnechet.getText().toString().equals(""))setTvUslNechet(edsuslnechet.getText().toString());
                }
            }
        });
        builder.setView((View) cl);
        builder.show();
    }

    public void onClickUslNechet(View view)
    {
        showDialogUslNechet();
    }

    // Конец условной длины




    private void setDistanceStartNechet(String disStartNechet)
    {
        tvStartNechet.setText(disStartNechet);
        start_distance_nechet = Integer.parseInt(disStartNechet);
        tvDistanceNechet.setText(disStartNechet);
        pb.setMax(start_distance_nechet - distanceStationStart);
    }

    private <ConstrainLayout> void showDialogStartNechet()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_start_nechet);
        ConstrainLayout cl = (ConstrainLayout) getLayoutInflater().inflate(R.layout.dialog_layout_start_nechet,null);
        builder.setPositiveButton(R.string.dialog_button_start_nechet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog nechet = (AlertDialog) dialog;
                EditText edsnechet = nechet.findViewById(R.id.edStartNechet);
                if(edsnechet != null){
                    if(!edsnechet.getText().toString().equals(""))setDistanceStartNechet(edsnechet.getText().toString());
                }
            }
        });
        builder.setView((View) cl);
        builder.show();
    }

    public void onClickDistanceStartNechet(View view)
    {
        showDialogStartNechet();
    }

    @SuppressLint("SetTextI18n")
    private void updateDistanceNechet(Location locNechet)
    {
        if(locNechet.hasSpeed() && lastLocationNechet != null)
        {

            progressBarNechet = start_distance_nechet - distanceStationStart;
            sumCalculateUslNechet = (uslDlNechet * 14) + 50;

            if ((Math.round((locNechet.getSpeed() / 1000) * 3600)) >= 1)
            {
                if (start_distance_nechet > distanceStationStart)
                {
                    start_distance_nechet -= lastLocationNechet.distanceTo(locNechet) - 0.55;
                    pb.setProgress(progressBarNechet);
                }

            }

        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase database = dbHelperNechet.getWritableDatabase();

                Cursor cursor = database.query(dbHelperNechet.TABLE_CONTACTS_NECHET, null, null, null, null, null, null);

                while (cursor.moveToNext()){
                    int titleNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME))));
                    int piketstartNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME_START_PIKET))));
                    int title_finishNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME_FINISH))));
                    int piketfinishNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME_FINISH_PIKET))));
                    int speedNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_SPEED))));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Расчёт начала ограничений по киллометро


                            int h = 9999901, kmj = 10000, pkj = 10;
                            while (h > 0) {

                                h = h - 1000;
                                kmj = kmj - 1;

                                if (title_finishNechet == kmj && piketfinishNechet == pkj) {
                                    faktEndKmNechet = h;
                                    piketEndKmNechet = pkj;
                                }

                            }

                            int h1 = 9999801, kmj1 = 10000, pkj1 = 9;
                            while (h1 > 0) {

                                h1 = h1 - 1000;
                                kmj1 = kmj1 - 1;

                                if (title_finishNechet == kmj1 && piketfinishNechet == pkj1) {
                                    faktEndKmNechet = h1;
                                    piketEndKmNechet = pkj1;
                                }

                            }

                            int h2 = 9999701, kmj2 = 10000, pkj2 = 8;
                            while (h2 > 0) {

                                h2 = h2 - 1000;
                                kmj2 = kmj2 - 1;

                                if (title_finishNechet == kmj2 && piketfinishNechet == pkj2) {
                                    faktEndKmNechet = h2;
                                    piketEndKmNechet = pkj2;
                                }

                            }

                            int h3 = 9999601, kmj3 = 10000, pkj3 = 7;
                            while (h3 > 0) {

                                h3 = h3 - 1000;
                                kmj3 = kmj3 - 1;

                                if (title_finishNechet == kmj3 && piketfinishNechet == pkj3) {
                                    faktEndKmNechet = h3;
                                    piketEndKmNechet = pkj3;
                                }

                            }

                            int h4 = 9999501, kmj4 = 10000, pkj4 = 6;
                            while (h4 > 0) {

                                h4 = h4 - 1000;
                                kmj4 = kmj4 - 1;

                                if (title_finishNechet == kmj4 && piketfinishNechet == pkj4) {
                                    faktEndKmNechet = h4;
                                    piketEndKmNechet = pkj4;
                                }

                            }

                            int h5 = 9999401, kmj5 = 10000, pkj5 = 5;
                            while (h5 > 0) {

                                h5 = h5 - 1000;
                                kmj5 = kmj5 - 1;

                                if (title_finishNechet == kmj5 && piketfinishNechet == pkj5) {
                                    faktEndKmNechet = h5;
                                    piketEndKmNechet = pkj5;
                                }

                            }

                            int h6 = 9999301, kmj6 = 10000, pkj6 = 4;
                            while (h6 > 0) {

                                h6 = h6 - 1000;
                                kmj6 = kmj6 - 1;

                                if (title_finishNechet == kmj6 && piketfinishNechet == pkj6) {
                                    faktEndKmNechet = h6;
                                    piketEndKmNechet = pkj6;
                                }

                            }

                            int h7 = 9999201, kmj7 = 10000, pkj7 = 3;
                            while (h7 > 0) {

                                h7 = h7 - 1000;
                                kmj7 = kmj7 - 1;

                                if (title_finishNechet == kmj7 && piketfinishNechet == pkj7) {
                                    faktEndKmNechet = h7;
                                    piketEndKmNechet = pkj7;
                                }

                            }

                            int h8 = 9999101, kmj8 = 10000, pkj8 = 2;
                            while (h8 > 0) {

                                h8 = h8 - 1000;
                                kmj8 = kmj8 - 1;

                                if (title_finishNechet == kmj8 && piketfinishNechet == pkj8) {
                                    faktEndKmNechet = h8;
                                    piketEndKmNechet = pkj8;
                                }

                            }

                            int h9 = 9999001, kmj9 = 10000, pkj9 = 1;
                            while (h9 > 0) {

                                h9 = h9 - 1000;
                                kmj9 = kmj9 - 1;

                                if (title_finishNechet == kmj9 && piketfinishNechet == pkj9) {
                                    faktEndKmNechet = h9;
                                    piketEndKmNechet = pkj9;
                                }

                            }

                            // Конец расчёта начала ограничений по киллометро


                            // Расчёт конца ограничений по киллометро


                            int l = 9999999, kml = 10000, pkl = 10;
                            while (l > 0) {

                                l = l - 1000;
                                kml = kml - 1;

                                if (titleNechet == kml && piketstartNechet == pkl) {
                                    faktNachKmNechet = l;
                                    piketNachKmNechet = pkl;
                                }

                            }

                            int l1 = 9999899, kml1 = 10000, pkl1 = 9;
                            while (l1 > 0) {

                                l1 = l1 - 1000;
                                kml1 = kml1 - 1;

                                if (titleNechet == kml1 && piketstartNechet == pkl1) {
                                    faktNachKmNechet = l1;
                                    piketNachKmNechet = pkl1;
                                }

                            }

                            int l2 = 9999799, kml2 = 10000, pkl2 = 8;
                            while (l2 > 0) {

                                l2 = l2 - 1000;
                                kml2 = kml2 - 1;

                                if (titleNechet == kml2 && piketstartNechet == pkl2) {
                                    faktNachKmNechet = l2;
                                    piketNachKmNechet = pkl2;
                                }

                            }

                            int l3 = 9999699, kml3 = 10000, pkl3 = 7;
                            while (l3 > 0) {

                                l3 = l3 - 1000;
                                kml3 = kml3 - 1;

                                if (titleNechet == kml3 && piketstartNechet == pkl3) {
                                    faktNachKmNechet = l3;
                                    piketNachKmNechet = pkl3;
                                }

                            }

                            int l4 = 9999599, kml4 = 10000, pkl4 = 6;
                            while (l4 > 0) {

                                l4 = l4 - 1000;
                                kml4 = kml4 - 1;

                                if (titleNechet == kml4 && piketstartNechet == pkl4) {
                                    faktNachKmNechet = l4;
                                    piketNachKmNechet = pkl4;
                                }

                            }

                            int l5 = 9999499, kml5 = 10000, pkl5 = 5;
                            while (l5 > 0) {

                                l5 = l5 - 1000;
                                kml5 = kml5 - 1;

                                if (titleNechet == kml5 && piketstartNechet == pkl5) {
                                    faktNachKmNechet = l5;
                                    piketNachKmNechet = pkl5;
                                }

                            }

                            int l6 = 9999399, kml6 = 10000, pkl6 = 4;
                            while (l6 > 0) {

                                l6 = l6 - 1000;
                                kml6 = kml6 - 1;

                                if (titleNechet == kml6 && piketstartNechet == pkl6) {
                                    faktNachKmNechet = l6;
                                    piketNachKmNechet = pkl6;
                                }

                            }

                            int l7 = 9999299, kml7 = 10000, pkl7 = 3;
                            while (l7 > 0) {

                                l7 = l7 - 1000;
                                kml7 = kml7 - 1;

                                if (titleNechet == kml7 && piketstartNechet == pkl7) {
                                    faktNachKmNechet = l7;
                                    piketNachKmNechet = pkl7;
                                }

                            }

                            int l8 = 9999199, kml8 = 10000, pkl8 = 2;
                            while (l8 > 0) {

                                l8 = l8 - 1000;
                                kml8 = kml8 - 1;

                                if (titleNechet == kml8 && piketstartNechet == pkl8) {
                                    faktNachKmNechet = l8;
                                    piketNachKmNechet = pkl8;
                                }

                            }

                            int l9 = 9999099, kml9 = 10000, pkl9 = 1;
                            while (l9 > 0) {

                                l9 = l9 - 1000;
                                kml9 = kml9 - 1;

                                if (titleNechet == kml9 && piketstartNechet == pkl9) {
                                    faktNachKmNechet = l9;
                                    piketNachKmNechet = pkl9;
                                }

                            }

                            // Конец расчёта конца ограничений по киллометро



                            // Начало опускания токоприемников

                            if (start_distance_nechet <= faktNachKmNechet + 5000 && start_distance_nechet >= faktNachKmNechet + 4900 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 02){
                                soundPlay(tokopriemniki);
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 02){
                                soundPlay(tokopriemniki2);
                            }

                            // Конец опускания токоприемников


                            // Начало пробы тормозов

                            if (start_distance_nechet <= faktNachKmNechet + 5000 && start_distance_nechet >= faktNachKmNechet + 4900 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 00){
                                soundPlay(probatormozov);
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 00){
                                soundPlay(probatormozov2);
                                tv_proba_nechet.setText("Проба тормозов");
                            }

                            if (start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktNachKmNechet - 50 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 00){
                                tv_proba_nechet.setText("");
                            }

                            // Конец пробы тормозов

                            // Начало оживления тормозов

                            if (start_distance_nechet <= faktNachKmNechet + 3000 && start_distance_nechet >= faktNachKmNechet + 2900 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 01){
                                soundPlay(ojevlenie);
                                tv_proba_nechet.setText("Оживление тормозов");
                            }

                            if (start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktNachKmNechet - 70 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 01){
                                tv_proba_nechet.setText("");
                            }

                            // Конец оживления тормозов




                            // Начало высвечивания выполненного предупреждения на экране, а так же стирание по порядку предупреждения

                            if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 15){
                                tvNameNechet.setText("");
                                tvRowNechet.setText("");
                                tvSpeedNechet.setText("");
                                tvInfoNechet.setText("");
                                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
                                if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 35) - sumCalculateUslNechet && speedNechet == 15) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 25){
                                tvNameNechet.setText("");
                                tvRowNechet.setText("");
                                tvSpeedNechet.setText("");
                                tvInfoNechet.setText("");
                                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
                                if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 35) - sumCalculateUslNechet && speedNechet == 25) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 40){
                                tvNameNechet.setText("");
                                tvRowNechet.setText("");
                                tvSpeedNechet.setText("");
                                tvInfoNechet.setText("");
                                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
                                if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 35) - sumCalculateUslNechet && speedNechet == 40) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 50){
                                tvNameNechet.setText("");
                                tvRowNechet.setText("");
                                tvSpeedNechet.setText("");
                                tvInfoNechet.setText("");
                                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
                                if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 35) - sumCalculateUslNechet && speedNechet == 50) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 55){
                                tvNameNechet.setText("");
                                tvRowNechet.setText("");
                                tvSpeedNechet.setText("");
                                tvInfoNechet.setText("");
                                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
                                if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 35) - sumCalculateUslNechet && speedNechet == 55) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 60){
                                tvNameNechet.setText("");
                                tvRowNechet.setText("");
                                tvSpeedNechet.setText("");
                                tvInfoNechet.setText("");
                                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
                                if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 35) - sumCalculateUslNechet && speedNechet == 60) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 65){
                                tvNameNechet.setText("");
                                tvRowNechet.setText("");
                                tvSpeedNechet.setText("");
                                tvInfoNechet.setText("");
                                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
                                if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 35) - sumCalculateUslNechet && speedNechet == 65) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 70){
                                tvNameNechet.setText("");
                                tvRowNechet.setText("");
                                tvSpeedNechet.setText("");
                                tvInfoNechet.setText("");
                                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
                                if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 35) - sumCalculateUslNechet && speedNechet == 70) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 75){
                                tvNameNechet.setText("");
                                tvRowNechet.setText("");
                                tvSpeedNechet.setText("");
                                tvInfoNechet.setText("");
                                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
                                if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 35) - sumCalculateUslNechet && speedNechet == 75) {
                                    soundPlay(songvipolneno);
                                }
                            }

                            // Конец высвечивания выполненного предупреждения на экране, а так же стирания по порядку предупреждения



                            // Предупреждение о превышении ограничения скорости

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 15){
                                soundPlay(ogr15);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 25){
                                soundPlay(ogr25);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 40){
                                soundPlay(ogr40);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 50){
                                soundPlay(ogr50);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 55){
                                soundPlay(ogr55);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 60){
                                soundPlay(ogr60);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 65){
                                soundPlay(ogr65);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 70){
                                soundPlay(ogr70);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 75){
                                soundPlay(ogr75);
                            }

                            // Конец предупреждения о превышении ограничения скорости


                            // Начало оповещения о снижении скорости за 400 метров до ограничения

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 15){
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 25){
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 40){
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 50){
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 55){
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 60){
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 65){
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 70){
                                soundPlay(voiceprev);
                            }

                            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 75){
                                soundPlay(voiceprev);
                            }

                            // Конец оповещения о снижении скорости за 400 метров до ограничения





                            // Начало ограничений скорости

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 15){
                                soundPlay(voice15);
                                tvInfoNechet.setText("");
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 15){
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }


                            //---------------------------------------------------------------------------------------------


                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 25){
                                soundPlay(voice25);
                                tvInfoNechet.setText("");
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 25){
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }


                            //---------------------------------------------------------------------------------------------


                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 40){
                                soundPlay(voice40);
                                tvInfoNechet.setText("");
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 40){
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }



                            //---------------------------------------------------------------------------------------------


                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 50){
                                soundPlay(voice50);
                                tvInfoNechet.setText("");
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 50){
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }


                            //---------------------------------------------------------------------------------------------


                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 55){
                                soundPlay(voice55);
                                tvInfoNechet.setText("");
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 55){
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }


                            //---------------------------------------------------------------------------------------------


                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 60){
                                soundPlay(voice60);
                                tvInfoNechet.setText("");
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 60){
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }


                            //---------------------------------------------------------------------------------------------


                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet +1900 && speedNechet == 65){
                                soundPlay(voice65);
                                tvInfoNechet.setText("");
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 65){
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }


                            //---------------------------------------------------------------------------------------------


                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 70){
                                soundPlay(voice70);
                                tvInfoNechet.setText("");
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 70){
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }


                            //---------------------------------------------------------------------------------------------


                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 75){
                                soundPlay(voice75);
                                tvInfoNechet.setText("");
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 75){
                                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
                                tvRowNechet.setText("=>");
                                tvSpeedNechet.setText(String.valueOf(speedNechet));
                            }

                            // Конец ограничений скорости
                        }
                    });

                }
                cursor.close();
                database.close();

            }
        }).start();


//        SQLiteDatabase database = dbHelperNechet.getWritableDatabase();
//
//        Cursor cursor = database.query(dbHelperNechet.TABLE_CONTACTS_NECHET, null, null, null, null, null, null);
//
//        while (cursor.moveToNext()){
//            int titleNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME))));
//            int piketstartNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME_START_PIKET))));
//            int title_finishNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME_FINISH))));
//            int piketfinishNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME_FINISH_PIKET))));
//            int speedNechet = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_SPEED))));



//            // Расчёт начала ограничений по киллометро
//
//
//            int h = 9999901, kmj = 10000, pkj = 10;
//            while (h > 0) {
//
//                h = h - 1000;
//                kmj = kmj - 1;
//
//                if (title_finishNechet == kmj && piketfinishNechet == pkj) {
//                    faktEndKmNechet = h;
//                    piketEndKmNechet = pkj;
//                }
//
//            }
//
//            int h1 = 9999801, kmj1 = 10000, pkj1 = 9;
//            while (h1 > 0) {
//
//                h1 = h1 - 1000;
//                kmj1 = kmj1 - 1;
//
//                if (title_finishNechet == kmj1 && piketfinishNechet == pkj1) {
//                    faktEndKmNechet = h1;
//                    piketEndKmNechet = pkj1;
//                }
//
//            }
//
//            int h2 = 9999701, kmj2 = 10000, pkj2 = 8;
//            while (h2 > 0) {
//
//                h2 = h2 - 1000;
//                kmj2 = kmj2 - 1;
//
//                if (title_finishNechet == kmj2 && piketfinishNechet == pkj2) {
//                    faktEndKmNechet = h2;
//                    piketEndKmNechet = pkj2;
//                }
//
//            }
//
//            int h3 = 9999601, kmj3 = 10000, pkj3 = 7;
//            while (h3 > 0) {
//
//                h3 = h3 - 1000;
//                kmj3 = kmj3 - 1;
//
//                if (title_finishNechet == kmj3 && piketfinishNechet == pkj3) {
//                    faktEndKmNechet = h3;
//                    piketEndKmNechet = pkj3;
//                }
//
//            }
//
//            int h4 = 9999501, kmj4 = 10000, pkj4 = 6;
//            while (h4 > 0) {
//
//                h4 = h4 - 1000;
//                kmj4 = kmj4 - 1;
//
//                if (title_finishNechet == kmj4 && piketfinishNechet == pkj4) {
//                    faktEndKmNechet = h4;
//                    piketEndKmNechet = pkj4;
//                }
//
//            }
//
//            int h5 = 9999401, kmj5 = 10000, pkj5 = 5;
//            while (h5 > 0) {
//
//                h5 = h5 - 1000;
//                kmj5 = kmj5 - 1;
//
//                if (title_finishNechet == kmj5 && piketfinishNechet == pkj5) {
//                    faktEndKmNechet = h5;
//                    piketEndKmNechet = pkj5;
//                }
//
//            }
//
//            int h6 = 9999301, kmj6 = 10000, pkj6 = 4;
//            while (h6 > 0) {
//
//                h6 = h6 - 1000;
//                kmj6 = kmj6 - 1;
//
//                if (title_finishNechet == kmj6 && piketfinishNechet == pkj6) {
//                    faktEndKmNechet = h6;
//                    piketEndKmNechet = pkj6;
//                }
//
//            }
//
//            int h7 = 9999201, kmj7 = 10000, pkj7 = 3;
//            while (h7 > 0) {
//
//                h7 = h7 - 1000;
//                kmj7 = kmj7 - 1;
//
//                if (title_finishNechet == kmj7 && piketfinishNechet == pkj7) {
//                    faktEndKmNechet = h7;
//                    piketEndKmNechet = pkj7;
//                }
//
//            }
//
//            int h8 = 9999101, kmj8 = 10000, pkj8 = 2;
//            while (h8 > 0) {
//
//                h8 = h8 - 1000;
//                kmj8 = kmj8 - 1;
//
//                if (title_finishNechet == kmj8 && piketfinishNechet == pkj8) {
//                    faktEndKmNechet = h8;
//                    piketEndKmNechet = pkj8;
//                }
//
//            }
//
//            int h9 = 9999001, kmj9 = 10000, pkj9 = 1;
//            while (h9 > 0) {
//
//                h9 = h9 - 1000;
//                kmj9 = kmj9 - 1;
//
//                if (title_finishNechet == kmj9 && piketfinishNechet == pkj9) {
//                    faktEndKmNechet = h9;
//                    piketEndKmNechet = pkj9;
//                }
//
//            }
//
//            // Конец расчёта начала ограничений по киллометро
//
//
//            // Расчёт конца ограничений по киллометро
//
//
//            int l = 9999999, kml = 10000, pkl = 10;
//            while (l > 0) {
//
//                l = l - 1000;
//                kml = kml - 1;
//
//                if (titleNechet == kml && piketstartNechet == pkl) {
//                    faktNachKmNechet = l;
//                    piketNachKmNechet = pkl;
//                }
//
//            }
//
//            int l1 = 9999899, kml1 = 10000, pkl1 = 9;
//            while (l1 > 0) {
//
//                l1 = l1 - 1000;
//                kml1 = kml1 - 1;
//
//                if (titleNechet == kml1 && piketstartNechet == pkl1) {
//                    faktNachKmNechet = l1;
//                    piketNachKmNechet = pkl1;
//                }
//
//            }
//
//            int l2 = 9999799, kml2 = 10000, pkl2 = 8;
//            while (l2 > 0) {
//
//                l2 = l2 - 1000;
//                kml2 = kml2 - 1;
//
//                if (titleNechet == kml2 && piketstartNechet == pkl2) {
//                    faktNachKmNechet = l2;
//                    piketNachKmNechet = pkl2;
//                }
//
//            }
//
//            int l3 = 9999699, kml3 = 10000, pkl3 = 7;
//            while (l3 > 0) {
//
//                l3 = l3 - 1000;
//                kml3 = kml3 - 1;
//
//                if (titleNechet == kml3 && piketstartNechet == pkl3) {
//                    faktNachKmNechet = l3;
//                    piketNachKmNechet = pkl3;
//                }
//
//            }
//
//            int l4 = 9999599, kml4 = 10000, pkl4 = 6;
//            while (l4 > 0) {
//
//                l4 = l4 - 1000;
//                kml4 = kml4 - 1;
//
//                if (titleNechet == kml4 && piketstartNechet == pkl4) {
//                    faktNachKmNechet = l4;
//                    piketNachKmNechet = pkl4;
//                }
//
//            }
//
//            int l5 = 9999499, kml5 = 10000, pkl5 = 5;
//            while (l5 > 0) {
//
//                l5 = l5 - 1000;
//                kml5 = kml5 - 1;
//
//                if (titleNechet == kml5 && piketstartNechet == pkl5) {
//                    faktNachKmNechet = l5;
//                    piketNachKmNechet = pkl5;
//                }
//
//            }
//
//            int l6 = 9999399, kml6 = 10000, pkl6 = 4;
//            while (l6 > 0) {
//
//                l6 = l6 - 1000;
//                kml6 = kml6 - 1;
//
//                if (titleNechet == kml6 && piketstartNechet == pkl6) {
//                    faktNachKmNechet = l6;
//                    piketNachKmNechet = pkl6;
//                }
//
//            }
//
//            int l7 = 9999299, kml7 = 10000, pkl7 = 3;
//            while (l7 > 0) {
//
//                l7 = l7 - 1000;
//                kml7 = kml7 - 1;
//
//                if (titleNechet == kml7 && piketstartNechet == pkl7) {
//                    faktNachKmNechet = l7;
//                    piketNachKmNechet = pkl7;
//                }
//
//            }
//
//            int l8 = 9999199, kml8 = 10000, pkl8 = 2;
//            while (l8 > 0) {
//
//                l8 = l8 - 1000;
//                kml8 = kml8 - 1;
//
//                if (titleNechet == kml8 && piketstartNechet == pkl8) {
//                    faktNachKmNechet = l8;
//                    piketNachKmNechet = pkl8;
//                }
//
//            }
//
//            int l9 = 9999099, kml9 = 10000, pkl9 = 1;
//            while (l9 > 0) {
//
//                l9 = l9 - 1000;
//                kml9 = kml9 - 1;
//
//                if (titleNechet == kml9 && piketstartNechet == pkl9) {
//                    faktNachKmNechet = l9;
//                    piketNachKmNechet = pkl9;
//                }
//
//            }
//
//            // Конец расчёта конца ограничений по киллометро





            int s = 9999001, s1 = 9999101, s2 = 9999201, s3 = 9999301, s4 = 9999401, s5 = 9999501, s6 = 9999601, s7 = 9999701, s8 = 9999801, s9 = 9999901, d = 9999099, d1 = 9999199, d2 = 9999299, d3 = 9999399, d4 = 9999499, d5 = 9999599, d6 = 9999699, d7 = 9999799, d8 = 9999899, d9 = 9999999, kmf = 10000, pkf = 1;
            while (d9 > 0) {

                d9 = d9 - 1000;
                d8 = d8 - 1000;
                d7 = d7 - 1000;
                d6 = d6 - 1000;
                d5 = d5 - 1000;
                d4 = d4 - 1000;
                d3 = d3 - 1000;
                d2 = d2 - 1000;
                d1 = d1 - 1000;
                d = d - 1000;

                s9 = s9 - 1000;
                s8 = s8 - 1000;
                s7 = s7 - 1000;
                s7 = s7 - 1000;
                s6 = s6 - 1000;
                s5 = s5 - 1000;
                s4 = s4 - 1000;
                s3 = s3 - 1000;
                s2 = s2 - 1000;
                s1 = s1 - 1000;
                s = s - 1000;

                kmf = kmf - 1;

                // Цикл подсчёта киллометров

                if (start_distance_nechet == 0) {
                    faktKmNechet.setText("");
                }
                else {
                    if (start_distance_nechet <= d9 && start_distance_nechet >= s9) {
                        pkf = 10;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }

                    if (start_distance_nechet <= d8 && start_distance_nechet >= s8) {
                        pkf = 9;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }

                    if (start_distance_nechet <= d7 && start_distance_nechet >= s7) {
                        pkf = 8;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }

                    if (start_distance_nechet <= d6 && start_distance_nechet >= s6) {
                        pkf = 7;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }

                    if (start_distance_nechet <= d5 && start_distance_nechet >= s5) {
                        pkf = 6;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }

                    if (start_distance_nechet <= d4 && start_distance_nechet >= s4) {
                        pkf = 5;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }

                    if (start_distance_nechet <= d3 && start_distance_nechet >= s3) {
                        pkf = 4;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }

                    if (start_distance_nechet <= d2 && start_distance_nechet >= s2) {
                        pkf = 3;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }

                    if (start_distance_nechet <= d1 && start_distance_nechet >= s1) {
                        pkf = 2;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }

                    if (start_distance_nechet <= d && start_distance_nechet >= s) {
                        pkf = 1;
                        faktKmNechet.setText(kmf + " км. " + pkf + " пк. ");
                        faktKmProsledovanieNechet = kmf;
                        faktPiketProsledovanieNechet = pkf;
                    }
                }

                // Конец цикла подсчёта киллометров
            }



//            // Начало опускания токоприемников
//
//            if (start_distance_nechet <= faktNachKmNechet + 5000 && start_distance_nechet >= faktNachKmNechet + 4900 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 01){
//                soundPlay(tokopriemniki);
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 01){
//                soundPlay(tokopriemniki2);
//            }
//
//            // Конец опускания токоприемников
//
//
//            // Начало пробы тормозов
//
//            if (start_distance_nechet <= faktNachKmNechet + 5000 && start_distance_nechet >= faktNachKmNechet + 4900 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 00){
//                soundPlay(probatormozov);
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 00){
//                soundPlay(probatormozov2);
//                tv_proba_nechet.setText("Проба тормозов");
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktNachKmNechet && title_finishNechet == 00 && piketfinishNechet == 00 && speedNechet == 00){
//                tv_proba_nechet.setText("");
//            }
//
//            // Конец пробы тормозов
//
//
//
//
//            // Начало высвечивания выполненного предупреждения на экране, а так же стирание по порядку предупреждения
//
//            if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 15){
//                tvNameNechet.setText("");
//                tvRowNechet.setText("");
//                tvSpeedNechet.setText("");
//                tvInfoNechet.setText("");
//                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
//            }
//
//            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 25){
//                tvNameNechet.setText("");
//                tvRowNechet.setText("");
//                tvSpeedNechet.setText("");
//                tvInfoNechet.setText("");
//                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
//            }
//
//            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 40){
//                tvNameNechet.setText("");
//                tvRowNechet.setText("");
//                tvSpeedNechet.setText("");
//                tvInfoNechet.setText("");
//                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
//            }
//
//            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 50){
//                tvNameNechet.setText("");
//                tvRowNechet.setText("");
//                tvSpeedNechet.setText("");
//                tvInfoNechet.setText("");
//                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
//            }
//
//            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 55){
//                tvNameNechet.setText("");
//                tvRowNechet.setText("");
//                tvSpeedNechet.setText("");
//                tvInfoNechet.setText("");
//                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
//            }
//
//            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 60){
//                tvNameNechet.setText("");
//                tvRowNechet.setText("");
//                tvSpeedNechet.setText("");
//                tvInfoNechet.setText("");
//                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
//            }
//
//            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 65){
//                tvNameNechet.setText("");
//                tvRowNechet.setText("");
//                tvSpeedNechet.setText("");
//                tvInfoNechet.setText("");
//                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
//            }
//
//            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 70){
//                tvNameNechet.setText("");
//                tvRowNechet.setText("");
//                tvSpeedNechet.setText("");
//                tvInfoNechet.setText("");
//                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
//            }
//
//            else if (start_distance_nechet <= faktEndKmNechet - sumCalculateUslNechet && start_distance_nechet >= (faktEndKmNechet - 80) - sumCalculateUslNechet && speedNechet == 75){
//                tvNameNechet.setText("");
//                tvRowNechet.setText("");
//                tvSpeedNechet.setText("");
//                tvInfoNechet.setText("");
//                tvInfoNechet.setText(titleNechet + " " + piketstartNechet + " -- " + title_finishNechet + " " + piketfinishNechet + " => " + speedNechet + " Выполнено!");
//            }
//
//            // Конец высвечивания выполненного предупреждения на экране, а так же стирания по порядку предупреждения
//
//
//
//            // Предупреждение о превышении ограничения скорости
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 15){
//                soundPlay(ogr15);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 25){
//                soundPlay(ogr25);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 40){
//                soundPlay(ogr40);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 50){
//                soundPlay(ogr50);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 55){
//                soundPlay(ogr55);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 60){
//                soundPlay(ogr60);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 65){
//                soundPlay(ogr65);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 70){
//                soundPlay(ogr70);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet && start_distance_nechet >= faktEndKmNechet - sumCalculateUslNechet && speedNechet == 75){
//                soundPlay(ogr75);
//            }
//
//            // Конец предупреждения о превышении ограничения скорости
//
//
//            // Начало оповещения о снижении скорости за 400 метров до ограничения
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 15){
//                soundPlay(voiceprev);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 25){
//                soundPlay(voiceprev);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 40){
//                soundPlay(voiceprev);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 50){
//                soundPlay(voiceprev);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 55){
//                soundPlay(voiceprev);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 60){
//                soundPlay(voiceprev);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 65){
//                soundPlay(voiceprev);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 70){
//                soundPlay(voiceprev);
//            }
//
//            if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= faktNachKmNechet + 400 && start_distance_nechet >= faktNachKmNechet + 300 && speedNechet == 75){
//                soundPlay(voiceprev);
//            }
//
//            // Конец оповещения о снижении скорости за 400 метров до ограничения
//
//
//
//
//
//            // Начало ограничений скорости
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 15){
//                soundPlay(voice15);
//                tvInfoNechet.setText("");
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 15){
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//
//            //---------------------------------------------------------------------------------------------
//
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 25){
//                soundPlay(voice25);
//                tvInfoNechet.setText("");
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 25){
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//
//            //---------------------------------------------------------------------------------------------
//
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 40){
//                soundPlay(voice40);
//                tvInfoNechet.setText("");
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 40){
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//
//
//            //---------------------------------------------------------------------------------------------
//
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 50){
//                soundPlay(voice50);
//                tvInfoNechet.setText("");
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 50){
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//
//            //---------------------------------------------------------------------------------------------
//
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 55){
//                soundPlay(voice55);
//                tvInfoNechet.setText("");
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 55){
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//
//            //---------------------------------------------------------------------------------------------
//
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 60){
//                soundPlay(voice60);
//                tvInfoNechet.setText("");
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 60){
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//
//            //---------------------------------------------------------------------------------------------
//
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet +1900 && speedNechet == 65){
//                soundPlay(voice65);
//                tvInfoNechet.setText("");
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 65){
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//
//            //---------------------------------------------------------------------------------------------
//
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 70){
//                soundPlay(voice70);
//                tvInfoNechet.setText("");
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 70){
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//
//            //---------------------------------------------------------------------------------------------
//
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= faktNachKmNechet + 1900 && speedNechet == 75){
//                soundPlay(voice75);
//                tvInfoNechet.setText("");
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            if (start_distance_nechet <= faktNachKmNechet + 2000 && start_distance_nechet >= (faktEndKmNechet - 30) - sumCalculateUslNechet && speedNechet == 75){
//                tvNameNechet.setText(titleNechet + " км " + piketstartNechet + " пк " + " -- " + title_finishNechet + " км " + piketfinishNechet + " пк ");
//                tvRowNechet.setText("=>");
//                tvSpeedNechet.setText(String.valueOf(speedNechet));
//            }
//
//            // Конец ограничений скорости










            // Начало опускания токоприемников

//                        if (start_distance_nechet <= titleNechet + 4000 && start_distance_nechet >= titleNechet + 3900 && title_finishNechet == 00 && speedNechet == 01){
//                            soundPlay(tokopriemniki);
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && title_finishNechet == 00 && speedNechet == 01){
//                            soundPlay(tokopriemniki2);
//                        }

            // Конец опускания токоприемников


            // Начало пробы тормозов

//                        if (start_distance_nechet <= titleNechet + 4000 && start_distance_nechet >= titleNechet + 3900 && title_finishNechet == 00 && speedNechet == 00){
//                            soundPlay(probatormozov);
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && title_finishNechet == 00 && speedNechet == 00){
//                            soundPlay(probatormozov2);
//                            tv_proba_nechet.setText("Проба тормозов");
//                        }
//
//                        if (start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= titleNechet - 1060 && title_finishNechet == 00 && speedNechet == 00){
//                            tv_proba_nechet.setText("");
//                        }

            // Конец пробы тормозов


            // Начало высвечивания выполненного предупреждения на экране, а так же стирание по порядку предупреждения

//                        if (start_distance_nechet <= (title_finishNechet - 1100) - sumCalculateUslNechet && start_distance_nechet >= (title_finishNechet - 1180) - sumCalculateUslNechet && speedNechet == 15){
//                            tvNameNechet.setText("");
//                            tvRowNechet.setText("");
//                            tvSpeedNechet.setText("");
//                            tvInfoNechet.setText("");
//                            tvInfoNechet.setText(titleNechet + " -- " + title_finishNechet + " => " + speedNechet + " Выполнено!");
//                        }
//
//                        else if (start_distance_nechet <= (title_finishNechet - 1100) - sumCalculateUslNechet && start_distance_nechet >= (title_finishNechet - 1180) - sumCalculateUslNechet && speedNechet == 25){
//                            tvNameNechet.setText("");
//                            tvRowNechet.setText("");
//                            tvSpeedNechet.setText("");
//                            tvInfoNechet.setText("");
//                            tvInfoNechet.setText(titleNechet + " -- " + title_finishNechet + " => " + speedNechet + " Выполнено!");
//                        }
//
//                        else if (start_distance_nechet <= (title_finishNechet - 1100) - sumCalculateUslNechet && start_distance_nechet >= (title_finishNechet - 1180) - sumCalculateUslNechet && speedNechet == 40){
//                            tvNameNechet.setText("");
//                            tvRowNechet.setText("");
//                            tvSpeedNechet.setText("");
//                            tvInfoNechet.setText("");
//                            tvInfoNechet.setText(titleNechet + " -- " + title_finishNechet + " => " + speedNechet + " Выполнено!");
//                        }
//
//                        else if (start_distance_nechet <= (title_finishNechet - 1100) - sumCalculateUslNechet && start_distance_nechet >= (title_finishNechet - 1180) - sumCalculateUslNechet && speedNechet == 50){
//                            tvNameNechet.setText("");
//                            tvRowNechet.setText("");
//                            tvSpeedNechet.setText("");
//                            tvInfoNechet.setText("");
//                            tvInfoNechet.setText(titleNechet + " -- " + title_finishNechet + " => " + speedNechet + " Выполнено!");
//                        }
//
//                        else if (start_distance_nechet <= (title_finishNechet - 1100) - sumCalculateUslNechet && start_distance_nechet >= (title_finishNechet - 1180) - sumCalculateUslNechet && speedNechet == 55){
//                            tvNameNechet.setText("");
//                            tvRowNechet.setText("");
//                            tvSpeedNechet.setText("");
//                            tvInfoNechet.setText("");
//                            tvInfoNechet.setText(titleNechet + " -- " + title_finishNechet + " => " + speedNechet + " Выполнено!");
//                        }
//
//                        else if (start_distance_nechet <= (title_finishNechet - 1100) - sumCalculateUslNechet && start_distance_nechet >= (title_finishNechet - 1180) - sumCalculateUslNechet && speedNechet == 60){
//                            tvNameNechet.setText("");
//                            tvRowNechet.setText("");
//                            tvSpeedNechet.setText("");
//                            tvInfoNechet.setText("");
//                            tvInfoNechet.setText(titleNechet + " -- " + title_finishNechet + " => " + speedNechet + " Выполнено!");
//                        }
//
//                        else if (start_distance_nechet <= (title_finishNechet - 1100) - sumCalculateUslNechet && start_distance_nechet >= (title_finishNechet - 1180) - sumCalculateUslNechet && speedNechet == 65){
//                            tvNameNechet.setText("");
//                            tvRowNechet.setText("");
//                            tvSpeedNechet.setText("");
//                            tvInfoNechet.setText("");
//                            tvInfoNechet.setText(titleNechet + " -- " + title_finishNechet + " => " + speedNechet + " Выполнено!");
//                        }
//
//                        else if (start_distance_nechet <= (title_finishNechet - 1100) - sumCalculateUslNechet && start_distance_nechet >= (title_finishNechet - 1180) - sumCalculateUslNechet && speedNechet == 70){
//                            tvNameNechet.setText("");
//                            tvRowNechet.setText("");
//                            tvSpeedNechet.setText("");
//                            tvInfoNechet.setText("");
//                            tvInfoNechet.setText(titleNechet + " -- " + title_finishNechet + " => " + speedNechet + " Выполнено!");
//                        }
//
//                        else if (start_distance_nechet <= (title_finishNechet - 1100) - sumCalculateUslNechet && start_distance_nechet >= (title_finishNechet - 1180) - sumCalculateUslNechet && speedNechet == 75){
//                            tvNameNechet.setText("");
//                            tvRowNechet.setText("");
//                            tvSpeedNechet.setText("");
//                            tvInfoNechet.setText("");
//                            tvInfoNechet.setText(titleNechet + " -- " + title_finishNechet + " => " + speedNechet + " Выполнено!");
//                        }

            // Конец высвечивания выполненного предупреждения на экране, а так же стирания по порядку предупреждения




            // Предупреждение о превышении ограничения скорости

//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= (title_finishNechet - 1100) - sumCalculateUslNechet && speedNechet == 15){
//                            soundPlay(ogr15);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= (title_finishNechet - 1100) - sumCalculateUslNechet && speedNechet == 25){
//                            soundPlay(ogr25);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= (title_finishNechet - 1100) - sumCalculateUslNechet && speedNechet == 40){
//                            soundPlay(ogr40);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= (title_finishNechet - 1100) - sumCalculateUslNechet && speedNechet == 50){
//                            soundPlay(ogr50);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= (title_finishNechet - 1100) - sumCalculateUslNechet && speedNechet == 55){
//                            soundPlay(ogr55);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= (title_finishNechet - 1100) - sumCalculateUslNechet && speedNechet == 60){
//                            soundPlay(ogr60);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= (title_finishNechet - 1100) - sumCalculateUslNechet && speedNechet == 65){
//                            soundPlay(ogr65);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= (title_finishNechet - 1100) - sumCalculateUslNechet && speedNechet == 70){
//                            soundPlay(ogr70);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 1000 && start_distance_nechet >= (title_finishNechet - 1100) - sumCalculateUslNechet && speedNechet == 75){
//                            soundPlay(ogr75);
//                        }

            // Конец предупреждения о превышении ограничения скорости


            // Начало оповещения о снижении скорости за 400 метров до ограничения

//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 600 && start_distance_nechet >= titleNechet - 700 && speedNechet == 15){
//                            soundPlay(voiceprev);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 600 && start_distance_nechet >= titleNechet - 700 && speedNechet == 25){
//                            soundPlay(voiceprev);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 600 && start_distance_nechet >= titleNechet - 700 && speedNechet == 40){
//                            soundPlay(voiceprev);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 600 && start_distance_nechet >= titleNechet - 700 && speedNechet == 50){
//                            soundPlay(voiceprev);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 600 && start_distance_nechet >= titleNechet - 700 && speedNechet == 55){
//                            soundPlay(voiceprev);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 600 && start_distance_nechet >= titleNechet - 700 && speedNechet == 60){
//                            soundPlay(voiceprev);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 600 && start_distance_nechet >= titleNechet - 700 && speedNechet == 65){
//                            soundPlay(voiceprev);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 600 && start_distance_nechet >= titleNechet - 700 && speedNechet == 70){
//                            soundPlay(voiceprev);
//                        }
//
//                        if (Math.round(locNechet.getSpeed() / 1000 * 3600) >= speedNechet && start_distance_nechet <= titleNechet - 600 && start_distance_nechet >= titleNechet - 700 && speedNechet == 75){
//                            soundPlay(voiceprev);
//                        }

            // Конец оповещения о снижении скорости за 400 метров до ограничения


            // Начало ограничений скорости

//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && speedNechet == 15){
//                            soundPlay(voice15);
//                            tvInfoNechet.setText("");
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= (title_finishNechet - 1130) - sumCalculateUslNechet && speedNechet == 15){
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && speedNechet == 25){
//                            soundPlay(voice25);
//                            tvInfoNechet.setText("");
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= (title_finishNechet - 1130) - sumCalculateUslNechet && speedNechet == 25){
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && speedNechet == 40){
//                            soundPlay(voice40);
//                            tvInfoNechet.setText("");
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= (title_finishNechet - 1130) - sumCalculateUslNechet && speedNechet == 40){
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && speedNechet == 50){
//                            soundPlay(voice50);
//                            tvInfoNechet.setText("");
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= (title_finishNechet - 1130) - sumCalculateUslNechet && speedNechet == 50){
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && speedNechet == 55){
//                            soundPlay(voice55);
//                            tvInfoNechet.setText("");
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= (title_finishNechet - 1130) - sumCalculateUslNechet && speedNechet == 55){
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && speedNechet == 60){
//                            soundPlay(voice60);
//                            tvInfoNechet.setText("");
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= (title_finishNechet - 1130) - sumCalculateUslNechet && speedNechet == 60){
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && speedNechet == 65){
//                            soundPlay(voice65);
//                            tvInfoNechet.setText("");
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= (title_finishNechet - 1130) - sumCalculateUslNechet && speedNechet == 65){
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && speedNechet == 70){
//                            soundPlay(voice70);
//                            tvInfoNechet.setText("");
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= (title_finishNechet - 1130) - sumCalculateUslNechet && speedNechet == 70){
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= titleNechet + 900 && speedNechet == 75){
//                            soundPlay(voice75);
//                            tvInfoNechet.setText("");
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }
//
//                        if (start_distance_nechet <= titleNechet + 1000 && start_distance_nechet >= (title_finishNechet - 1130) - sumCalculateUslNechet && speedNechet == 75){
//                            tvNameNechet.setText(String.valueOf(titleNechet + " -- " + title_finishNechet));
//                            tvRowNechet.setText("=>");
//                            tvSpeedNechet.setText(String.valueOf(speedNechet));
//                        }

            // Конец ограничений скорости
//        }
//        cursor.close();
//        database.close();



        lastLocationNechet = locNechet;
        tvDistanceNechet.setText(String.valueOf(start_distance_nechet));
        tvVelocityNechet.setText(String.valueOf(Math.round((locNechet.getSpeed() / 1000) * 3600)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == RESULT_OK)
        {
            checkPermissions();
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
            locationManagerNechet.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, myLocListenerNechet);
        }
    }

    @Override
    public void OnLocationChangedNechet(Location locNechet) {
        updateDistanceNechet(locNechet);
    }

    public void soundPlay(MediaPlayer sound)
    {
        sound.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myDbManagerNechet.openDbNechet();
        AppExecuterNechet.getInstance().getSubIoNechet().execute(new Runnable() {
            @Override
            public void run() {
                myDbManagerNechet.getFromDbNechet(MainActivityNechet.this);
            }
        });
    }


    public void onClickAddNechet(View view){
        Intent i_nechet = new Intent(MainActivityNechet.this, EditActivityNechet.class);
        startActivity(i_nechet);
    }

    public void onClickBtnChet(View view)
    {
        Intent iNechet = new Intent(MainActivityNechet.this, MainActivity.class);
        startActivity(iNechet);
        finish();
    }

    public void onclick_minus_nechet(View view){
        start_distance_nechet -= 50;
    }

    public void onclick_plus_nechet(View view){
        start_distance_nechet += 50;
    }

    protected void onDestroy() {

        super.onDestroy();
        myDbManagerNechet.closeDbNechet();
    }

    private ItemTouchHelper getItemTouchHelperNechet(){
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapterNechet.removeItemNechet(viewHolder.getAdapterPosition(), myDbManagerNechet);
            }
        });
    }

    @Override
    public void onReceived(List<ListItemNechet> list) {
        AppExecuterNechet.getInstance().getMainIoNechet().execute(new Runnable() {
            @Override
            public void run() {
                mainAdapterNechet.updateAdapterNechet(list);
            }
        });
    }
}