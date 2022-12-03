package com.example.rzdassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "contactDb";

    public static final String EDIT_STATE_CHET = "edit_state_chet";
    public static final String LIST_ITEM_INTENT_CHET = "list_item_intent_chet";
    public static final String TABLE_CONTACTS_CHET = "contacts_chet";
    public static final String KEY_CHET_ID = "_id_chet";
    public static final String KEY_CHET_NAME = "name_chet";
    public static final String KEY_CHET_NAME_START_PIKET = "name_chet_start_piket";
    public static final String KEY_CHET_NAME_FINISH = "name_finish_chet";
    public static final String KEY_CHET_NAME_FINISH_PIKET = "name_finish_chet_piket";
    public static final String KEY_CHET_SPEED = "speed_chet";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS_CHET + "(" + KEY_CHET_ID
                + " integer primary key," + KEY_CHET_NAME_FINISH_PIKET + " text," + KEY_CHET_NAME_START_PIKET + " text," + KEY_CHET_SPEED + " text," + KEY_CHET_NAME + " text," + KEY_CHET_NAME_FINISH + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS_CHET);
        onCreate(db);
    }
}
