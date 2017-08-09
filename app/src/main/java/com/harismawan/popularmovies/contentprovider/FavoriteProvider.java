package com.harismawan.popularmovies.contentprovider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.harismawan.popularmovies.database.DatabaseHelper;

public class FavoriteProvider extends ContentProvider {

    private SQLiteDatabase db;

    public static String AUTHORITY = "com.harismawan.popularmovies.contentprovider.FavoriteProvider";
    public static String CONTENT_URI = "content://" + AUTHORITY;

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/favorites";
    private static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/favorite";

    private static final int FAVORITES = 1;
    private static final int FAVORITE_ID = 2;
    private static final int DELETE_ID = 3;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "favorite", FAVORITES);
        uriMatcher.addURI(AUTHORITY, "favorite/#", FAVORITE_ID);
        uriMatcher.addURI(AUTHORITY, "favorite/delete/#", DELETE_ID);
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        db = helper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseHelper.TABLE_FAVORITE);

        switch (uriMatcher.match(uri)) {
            case FAVORITES:
                break;
            case FAVORITE_ID:
                builder.appendWhere(DatabaseHelper.COLUMN_ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor c = builder.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case FAVORITES:
                return CONTENT_TYPE;
            case FAVORITE_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (uriMatcher.match(uri)) {
            case FAVORITES:
                long rowID = db.insert(DatabaseHelper.TABLE_FAVORITE, null, contentValues);

                if (rowID > 0) {
                    Uri _uri = ContentUris.withAppendedId(Uri.parse(CONTENT_URI + "/favorite"), rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)){
            case DELETE_ID:
                count = db.delete(DatabaseHelper.TABLE_FAVORITE, DatabaseHelper.COLUMN_ID
                        + " = " +  uri.getLastPathSegment(), null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case FAVORITES:
                count = db.update(DatabaseHelper.TABLE_FAVORITE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
