package com.example.anis.finalkaraokeapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;
import com.example.anis.finalkaraokeapplication.data.ProfileslistContract.*;


/**
 * Created by Anis on 3/1/2018.
 */

public class ProfilelistDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "profilelist.db";
    private static final int DATABASE_VERSION = 1;

public ProfilelistDbHelper(Context context){
    super(context, DATABASE_NAME, null, DATABASE_VERSION);

}
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_PROFILELIST_TABLE = "CREATE TABLE " + ProfilelistEntry.TABLE_NAME + " (" +
                ProfilelistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProfilelistEntry.COLUMN_PROFILE_NAME + " TEXT NOT NULL, " +
                ProfilelistEntry.COLUMN_AGE + " INTEGER, " +
                ProfilelistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        final String SQL_CREATE_PROFILEINFO_TABLE = "CREATE TABLE " + ProfileInfo.TABLE_NAME + " (" +
                ProfileslistContract.ProfileInfo._ID + " INTEGER FOREIGN KEY AUTOINCREMENT," +
                ProfileslistContract.ProfileInfo.COLUMN_PROFILE_NAME + " TEXT NOT NULL," +
                ProfileslistContract.ProfileInfo.SONG_NAME + "STRING, " +
                ProfileslistContract.ProfileInfo.COLUMN_RECORDED_AUDIO + " TEXT NOT NULL, " +
                ProfileslistContract.ProfileInfo.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_PROFILELIST_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PROFILEINFO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProfilelistEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProfileInfo.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
