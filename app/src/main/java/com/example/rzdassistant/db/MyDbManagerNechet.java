package com.example.rzdassistant.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rzdassistant.DBHelper;
import com.example.rzdassistant.DBHelperNechet;
import com.example.rzdassistant.adapter.ListItemNechet;

import java.util.ArrayList;
import java.util.List;

public class MyDbManagerNechet {
    private Context context;
    private DBHelperNechet dbHelperNechet;
    private SQLiteDatabase dbNechet;

    public MyDbManagerNechet(Context context){
        this.context = context;
        dbHelperNechet = new DBHelperNechet(context);
    }

    public void openDbNechet(){
        dbNechet = dbHelperNechet.getWritableDatabase();
    }

    public void insertToDbNechet(String title, String piketstartnechet, String title_finish, String piketfinishnechet, String speed){
        ContentValues cv = new ContentValues();
        cv.put(DBHelperNechet.KEY_NECHET_NAME, title);
        cv.put(DBHelperNechet.KEY_NECHET_NAME_START_PIKET, piketstartnechet);
        cv.put(DBHelperNechet.KEY_NECHET_NAME_FINISH, title_finish);
        cv.put(DBHelperNechet.KEY_NECHET_NAME_FINISH_PIKET, piketfinishnechet);
        cv.put(DBHelperNechet.KEY_NECHET_SPEED, speed);
        dbNechet.insert(DBHelperNechet.TABLE_CONTACTS_NECHET, null, cv);
    }

    public void deleteNechet(int id){
        String selectionNechet = dbHelperNechet.KEY_NECHET_ID + "=" + id;
        dbNechet.delete(dbHelperNechet.TABLE_CONTACTS_NECHET, selectionNechet, null);
    }

    public void updateNechet(String title, String piketstartnechet, String title_finish, String piketfinishnechet, String speed, int id){
        String selectionNechet = dbHelperNechet.KEY_NECHET_ID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(DBHelperNechet.KEY_NECHET_NAME, title);
        cv.put(DBHelperNechet.KEY_NECHET_NAME_START_PIKET, piketstartnechet);
        cv.put(DBHelperNechet.KEY_NECHET_NAME_FINISH, title_finish);
        cv.put(DBHelperNechet.KEY_NECHET_NAME_FINISH_PIKET, piketfinishnechet);
        cv.put(DBHelperNechet.KEY_NECHET_SPEED, speed);
        dbNechet.update(dbHelperNechet.TABLE_CONTACTS_NECHET, cv, selectionNechet, null);
    }

    public void getFromDbNechet(OnDataReceivedNechet onDataReceivedNechet){
        List<ListItemNechet> tempListNechet = new ArrayList<>();
        Cursor cursor = dbNechet.query(dbHelperNechet.TABLE_CONTACTS_NECHET, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            ListItemNechet item = new ListItemNechet();
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME));
            @SuppressLint("Range") String piketstartnechet = cursor.getString(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME_START_PIKET));
            @SuppressLint("Range") String title_finish = cursor.getString(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME_FINISH));
            @SuppressLint("Range") String piketfinishnechet = cursor.getString(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_NAME_FINISH_PIKET));
            @SuppressLint("Range") String speed = cursor.getString(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_SPEED));
            @SuppressLint("Range") int _id = cursor.getInt(cursor.getColumnIndex(DBHelperNechet.KEY_NECHET_ID));
            item.setTitle_nechet(title);
            item.setPiket_start_nechet(piketstartnechet);
            item.setTitle_finish_nechet(title_finish);
            item.setPiket_finish_nechet(piketfinishnechet);
            item.setSpeed_nechet(speed);
            item.setId_nechet(_id);
            tempListNechet.add(item);
        }
        cursor.close();
        onDataReceivedNechet.onReceived(tempListNechet);
    }

    public void closeDbNechet(){
        dbHelperNechet.close();
    }
}
