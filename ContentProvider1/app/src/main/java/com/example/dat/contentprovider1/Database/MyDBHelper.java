package com.example.dat.contentprovider1.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DAT on 10/28/2015.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydb1";
    private static final int DATABASE_VERSION = 1;

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AnimalTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AnimalTable.onUpgrade(db, oldVersion, newVersion);
    }
}
