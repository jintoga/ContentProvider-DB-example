package com.example.dat.contentprovider1.Database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by DAT on 10/28/2015.
 */
public class AnimalTable {

    public static final String TABLE_NAME = "animal_table";
    public static final String KEY_ID = "_id";
    public static final String NAME = "name";
    public static final String TYPE = "type";


    static final String CREATE_DB_TABLE = "CREATE TABLE " + TABLE_NAME
            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " TEXT NOT NULL, "
            + TYPE + " TEXT NOT NULL"
            + " );";

    public static void onCreate(SQLiteDatabase database) {
        // Execute Table creation SQL script
        database.execSQL(CREATE_DB_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // Drop Table if exist and recreate it
        Log.w(AnimalTable.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
