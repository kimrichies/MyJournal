package ug.co.must.kimrichies.diary;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kimrichies on 29/06/2018.
 */

public class JournalProvider extends ContentProvider {
    DBHelper dbHelper;
    // fields for content provider
    static final String PROVIDER_NAME = "com.must.provider";
    static final String URL = "content://" + PROVIDER_NAME + "/diary";
    static final Uri CONTENT_URI = Uri.parse(URL);

    // fields for the database
    static final String ID = "id";
    static final String DATE = "date";
    static final String TITLE = "title";
    static final String GRATEFUL = "grateful";
    static final String DONE = "done";
    static final String FEEL = "feel";

    // integer values used in content URI
    static final int DIARY = 1;//for all rows
    static final int DIARY_ID = 2; //for a single row

    // maps content URI "patterns" to the integer values that were set above
    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "diary", DIARY);
        uriMatcher.addURI(PROVIDER_NAME, "diary/#", DIARY_ID);
    }

    // database declarations
    private SQLiteDatabase database;
    static final String DATABASE_NAME = "gratitude";
    static final String TABLE_NAME = "diary";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " date TEXT NOT NULL, " +
                    " title TEXT NOT NULL, " +
                    " grateful TEXT NOT NULL, " +
                    " done TEXT NOT NULL, " +
                    " feel TEXT NOT NULL);";


    // class that creates and manages the provider's database
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ". Old data will be destroyed");
            db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
            onCreate(db);
        }

    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        dbHelper = new DBHelper(context);
        // permissions to be writable
        database = dbHelper.getWritableDatabase();

        if(database == null)
            return false;
        else
            return true;
    }
    // projection map for a query
    private static HashMap<String, String> BirthMap;

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // the TABLE_NAME to query on
        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            // maps all database column names

            case DIARY:
                queryBuilder.setProjectionMap(BirthMap);
                break;
            case DIARY_ID:
                queryBuilder.appendWhere( ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == ""){
            // No sorting-> sort  by default
            sortOrder = DATE;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    //Inserting data in the table
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long row = database.insert(TABLE_NAME, "", values);

        // If record is added successfully
        if(row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Failed to add a new record into " + uri);
    }

    //Updating the data
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case DIARY:
                count = database.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case DIARY_ID:
                count = database.update(TABLE_NAME, values, ID +
                        " = " + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    //DELETE RECORD
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        int count;

        switch (uriMatcher.match(uri)){
            case DIARY:
                // delete all the records of the table
                count = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case DIARY_ID:
                String id = uri.getLastPathSegment();	//gets the id
                count = database.delete( TABLE_NAME, ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;


    }
    //defining the mime type
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){

            case DIARY:
                return "vnd.android.cursor.dir/vnd.example.diary";

            case DIARY_ID:
                return "vnd.android.cursor.item/vnd.example.diary ";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }



}


