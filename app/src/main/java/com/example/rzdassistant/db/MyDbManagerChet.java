package com.example.rzdassistant.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rzdassistant.DBHelper;
import com.example.rzdassistant.adapter.ListItemChet;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class MyDbManagerChet {
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase dbChet;

    public MyDbManagerChet(Context context){
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public void openDbChet(){
        dbChet = dbHelper.getWritableDatabase();
    }

    public void insertToDbChet(String title, String piketstart, String title_finish, String piketfinish, String speed){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_CHET_NAME, title);
        cv.put(DBHelper.KEY_CHET_NAME_START_PIKET, piketstart);
        cv.put(DBHelper.KEY_CHET_NAME_FINISH, title_finish);
        cv.put(DBHelper.KEY_CHET_NAME_FINISH_PIKET, piketfinish);
        cv.put(DBHelper.KEY_CHET_SPEED, speed);
        dbChet.insert(DBHelper.TABLE_CONTACTS_CHET, null, cv);
    }

    public void deleteChet(int id){
        String selectionChet = dbHelper.KEY_CHET_ID + "=" + id;
        dbChet.delete(dbHelper.TABLE_CONTACTS_CHET, selectionChet, null);
    }

    public void updateChet(String title, String piketstart, String title_finish, String piketfinish, String speed, int id){
        String selectionChet = dbHelper.KEY_CHET_ID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_CHET_NAME, title);
        cv.put(DBHelper.KEY_CHET_NAME_START_PIKET, piketstart);
        cv.put(DBHelper.KEY_CHET_NAME_FINISH, title_finish);
        cv.put(DBHelper.KEY_CHET_NAME_FINISH_PIKET, piketfinish);
        cv.put(DBHelper.KEY_CHET_SPEED, speed);
        dbChet.update(dbHelper.TABLE_CONTACTS_CHET, cv, selectionChet, null);
    }

    public void getFromDbChet(OnDataReceived onDataReceived){
        List<ListItemChet> tempListChet = new ArrayList<>();
        Cursor cursor = dbChet.query(dbHelper.TABLE_CONTACTS_CHET, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            ListItemChet item = new ListItemChet();
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CHET_NAME));
            @SuppressLint("Range") String piketstart = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CHET_NAME_START_PIKET));
            @SuppressLint("Range") String title_finish = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CHET_NAME_FINISH));
            @SuppressLint("Range") String piketfinish = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CHET_NAME_FINISH_PIKET));
            @SuppressLint("Range") String speed = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CHET_SPEED));
            @SuppressLint("Range") int _id = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_CHET_ID));
            item.setTitle_chet(title);
            item.setPiket_start_chet(piketstart);
            item.setTitle_finish_chet(title_finish);
            item.setPiket_finish_chet(piketfinish);
            item.setSpeed_chet(speed);
            item.setId_chet(_id);
            tempListChet.add(item);
        }
        cursor.close();
        onDataReceived.onReceived(tempListChet);
    }

    public void closeDbChet(){
        dbHelper.close();
    }
}
