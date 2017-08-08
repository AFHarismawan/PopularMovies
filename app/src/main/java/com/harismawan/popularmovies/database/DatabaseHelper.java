package com.harismawan.popularmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.harismawan.popularmovies.model.Movie;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movies";
    private static final int DB_VERSION = 1;

    private final String TABLE_FAVORITE = "favorite";
    private final String COLUMN_ID = "id";
    private final String COLUMN_IMG_URL = "name";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITE + " ("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_IMG_URL + " VARCHAR NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
    }

    public void insertFavorite(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, movie.id);
        values.put(COLUMN_IMG_URL, movie.imageUrl);

        getWritableDatabase().replace(TABLE_FAVORITE, null, values);
    }

    public ArrayList<Movie> getFavorite() {
        ArrayList<Movie> list = new ArrayList<>();
        Cursor mCursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_FAVORITE, null);
        mCursor.moveToFirst();

        if (!mCursor.isAfterLast()) {
            do {
                Movie entry = new Movie();
                entry.id = mCursor.getInt(mCursor.getColumnIndex(COLUMN_ID));
                entry.imageUrl = mCursor.getString(mCursor.getColumnIndex(COLUMN_IMG_URL));
                list.add(entry);
            } while (mCursor.moveToNext());
        }

        mCursor.close();
        return list;
    }
}
