package com.example.rzdassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperNechet extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION_NECHET = 2;
    public static final String DATABASE_NAME_NECHET = "contactDbNechet";

    public static final String EDIT_STATE_NECHET = "edit_state_nechet";
    public static final String LIST_ITEM_INTENT_NECHET = "list_item_intent_nechet";
    public static final String TABLE_CONTACTS_NECHET = "contacts_nechet";
    public static final String KEY_NECHET_ID = "_id_nechet";
    public static final String KEY_NECHET_NAME = "name_nechet";
    public static final String KEY_NECHET_NAME_START_PIKET = "name_nechet_start_piket";
    public static final String KEY_NECHET_NAME_FINISH = "name_finish_nechet";
    public static final String KEY_NECHET_NAME_FINISH_PIKET = "name_finish_nechet_piket";
    public static final String KEY_NECHET_SPEED = "speed_nechet";

    public DBHelperNechet(Context context) {
        super(context, DATABASE_NAME_NECHET, null, DATABASE_VERSION_NECHET);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS_NECHET + "(" + KEY_NECHET_ID
                + " integer primary key," + KEY_NECHET_NAME_FINISH_PIKET + " text," + KEY_NECHET_NAME_START_PIKET + " text," + KEY_NECHET_SPEED + " text," + KEY_NECHET_NAME + " text," + KEY_NECHET_NAME_FINISH + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS_NECHET);
        onCreate(db);

    }
}
