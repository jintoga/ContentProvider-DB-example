package com.example.dat.contentprovider1.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.dat.contentprovider1.Database.AnimalTable;
import com.example.dat.contentprovider1.Database.MyDBHelper;

import java.util.HashMap;

/**
 * Created by DAT on 10/28/2015.
 */
public class MyContentProvider extends ContentProvider {

    private MyDBHelper myDBHelper;
    private SQLiteDatabase database;

    private static final String PROVIDER_NAME = "com.example.dat.contentprovider.Animals";
    private static final String URI = "content://" + PROVIDER_NAME + "/animals";
    public static final Uri CONTENT_URI = Uri.parse(URI);

    private static HashMap<String, String> ANIMALS_PROJECTION_MAP;

    private static final int ANIMALS = 1;
    private static final int ANIMALS_ID = 2;
    private static final String ANIMALS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + PROVIDER_NAME;
    private static final String ANIMALS_ID_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + PROVIDER_NAME;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(PROVIDER_NAME, "animals", ANIMALS);
        uriMatcher.addURI(PROVIDER_NAME, "animals/#", ANIMALS_ID);
    }

    @Override
    public boolean onCreate() {
        myDBHelper = new MyDBHelper(getContext());
        database = myDBHelper.getWritableDatabase();
        return (database == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(AnimalTable.TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case ANIMALS:
                queryBuilder.setProjectionMap(ANIMALS_PROJECTION_MAP);
                break;
            case ANIMALS_ID:
                queryBuilder.appendWhere(AnimalTable.KEY_ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        //sort by
        if (sortOrder == null || sortOrder == "") {
            sortOrder = AnimalTable.NAME; // or AnimalTable.TYPE;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ANIMALS:
                return ANIMALS_TYPE;
            case ANIMALS_ID:
                return ANIMALS_ID_TYPE;
            default:
                throw new IllegalArgumentException("Unsupport URIL " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != ANIMALS) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long rowInserted = database.insert(AnimalTable.TABLE_NAME, null, values);
        if (rowInserted > 0) {
            Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowInserted);
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsAffected = 0;
        switch (uriMatcher.match(uri)) {
            case ANIMALS:
                rowsAffected = database.delete(AnimalTable.TABLE_NAME, selection, selectionArgs);
                break;
            case ANIMALS_ID:
                rowsAffected = database.delete(AnimalTable.TABLE_NAME, AnimalTable.KEY_ID + "=" + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;
        switch (uriMatcher.match(uri)) {
            case ANIMALS:
                rowsUpdated = database.update(AnimalTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ANIMALS_ID:
                rowsUpdated = database.update(AnimalTable.TABLE_NAME, values, AnimalTable.KEY_ID + "=" + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
