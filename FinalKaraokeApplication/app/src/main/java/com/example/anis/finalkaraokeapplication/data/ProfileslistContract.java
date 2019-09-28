package com.example.anis.finalkaraokeapplication.data;

import android.provider.BaseColumns;

/**
 * Created by Anis on 3/12/2018.
 */

public class ProfileslistContract {

    public static final class ProfilelistEntry implements BaseColumns {
        public static final String TABLE_NAME = "profileslist";
        public static final String COLUMN_PROFILE_NAME = "profileName";
        public static final String COLUMN_AGE= "age";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public static final class ProfileInfo implements BaseColumns {
        public static final String TABLE_NAME = "profileeach";
        public static final String COLUMN_PROFILE_NAME = "profileName";
        public static final String SONG_NAME = "songname";
        public static final String COLUMN_RECORDED_AUDIO = "audio";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
