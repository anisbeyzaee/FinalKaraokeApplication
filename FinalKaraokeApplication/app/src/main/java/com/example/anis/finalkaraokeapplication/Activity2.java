package com.example.anis.finalkaraokeapplication;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anis.finalkaraokeapplication.data.ProfilelistDbHelper;
import com.example.anis.finalkaraokeapplication.data.ProfileslistContract;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Anis on 3/2/2018.
 */

public class Activity2 extends YouTubeBaseActivity {

    //initializing variables for Recording
    Button btnRecord, btnStopRecord, btnPlayRcrd, btnStopPlay;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    private SQLiteDatabase mDb;
    private Activity2Adapter mAdapter;

    final int REQUEST_PERMISION_CODE = 1000;


    //initializing variables for youtube player
    private static final String TAG = "Activity2";
    YouTubePlayerView mYouTubePlayerView;
    Button btnPlay;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    RecyclerView.ViewHolder viewHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_layout);

        Intent intent = getIntent();
        String id = intent.getStringExtra("userName/id");
        TextView userWelcome = (TextView) findViewById(R.id.user_welcome);
        userWelcome.setText(" Welcome   " + id);

        RecyclerView personprofileRecyclerView;


        personprofileRecyclerView = (RecyclerView) this.findViewById(R.id.profile_each);

        personprofileRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a profilelistDbHelper instance, pass "this" to the constructor
        // Create a DB helper (this will create the DB if run for the first time)
        ProfilelistDbHelper dbHelper = new ProfilelistDbHelper(this);

        //  Get a writable database reference using getWritableDatabase and store it in mDb
        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();
        //Log.d(TAG, "     Line 57 ...+++");
        //call insertFakeData in TestUtil and pass the database reference mDb
        //Fill the database with fake data
        //TestUtil.insertFakeData(mDb);

        //  Run the getAllGuests function and store the result in a Cursor variable
        //RecyclerView.ViewHolder viewHolder;

        Cursor cursor2 = getProfileInfo(id);
        mAdapter = new Activity2Adapter(this, cursor2);
        personprofileRecyclerView.setAdapter(mAdapter);

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//
//
//            // COMPLETED (4) Override onMove and simply return false inside
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                //do nothing, we only care about swiping
//                return false;
//            }

            // COMPLETED (5) Override onSwiped
            //@Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
//                //get the id of the item being swiped
//                long id = (long) viewHolder.itemView.getTag();
//                // COMPLETED (9) call removeGuest and pass through that id
//                //remove from DB
//                removeSong(id);
//                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
//                //update the list
//                mAdapter.swapCursor(getProfileInfo(id));
//            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
//        }).attachToRecyclerView(personprofileRecyclerView);

        // Request RunTime Permission
        if(!checkPermissionFromDevice())
            requestPermissions();

        btnRecord = (Button) findViewById(R.id.btnRecord);
        btnStopRecord = (Button) findViewById(R.id.btnstprcrd);
        btnPlayRcrd = (Button) findViewById(R.id.btnPlyrcrd);
        btnStopPlay = (Button) findViewById(R.id.btnStpplyrcrd);

        // Requst of RunTime Permission from Android M

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermissionFromDevice()){

                    pathSave = Environment.getExternalStorageDirectory()
                            .getAbsolutePath()+"/"+ UUID.randomUUID().toString()+"_audio_record.3gp";
                    setupMediaRecorder();
                    try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    btnPlayRcrd.setEnabled(false);
                    btnStopPlay.setEnabled(false);

                    Toast.makeText(Activity2.this, "Recording ... ", Toast.LENGTH_SHORT).show();
                }
                else {
                    requestPermissions();
                }
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                btnStopRecord.setEnabled(false);
                // code to add the file name to sqlite Table
                btnPlayRcrd.setEnabled(true);
                btnRecord.setEnabled(true);
                btnStopPlay.setEnabled(false);

            }
        });

        btnPlayRcrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStopPlay.setEnabled(true);
                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(false);

                mediaPlayer = new MediaPlayer();
                try{
                    mediaPlayer.setDataSource(pathSave);
                    mediaPlayer.prepare();
                } catch (IOException e){
                    e.printStackTrace();
                }
                mediaPlayer.start();
                Toast.makeText(Activity2.this, "Playing....", Toast.LENGTH_SHORT).show();

            }
        });
        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStopPlay.setEnabled(false);
                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(true);
                btnPlayRcrd.setEnabled(true);

                if (mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    setupMediaRecorder();
                }
            }
        });

        Log.d(TAG, "onCreate: Starting");
        btnPlay = (Button) findViewById(R.id.ButtonPlay) ;
        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlay);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onInitializationSuccess: Done Initializing");
                //youTubePlayer.loadVideo("cDNDVtoJhik");
                youTubePlayer.loadPlaylist("PLHHOX195x0-K3YBBpfPZAZEkifzH3vXw6");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onInitializationFailure: Failed to initialize");
            }
        };
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Initi youtube player");
                mYouTubePlayerView.initialize(YouTubeConfig.getApiKey(), mOnInitializedListener );
            }
        });
    }

    private boolean removeSong(long id) {
        return mDb.delete(ProfileslistContract.ProfileInfo.COLUMN_RECORDED_AUDIO, ProfileslistContract.ProfileInfo._ID + "=" + id, null) > 0;
    }

    private Cursor getProfileInfo(String idKey) {

        return mDb.query(
                ProfileslistContract.ProfileInfo.TABLE_NAME,
                new String[] {String.valueOf(ProfileslistContract.ProfileInfo.SONG_NAME)},
                ProfileslistContract.ProfileInfo._ID + "= ?",
                new String[] {String.valueOf(idKey)},
                null,
                null,
                ProfileslistContract.ProfileInfo.COLUMN_TIMESTAMP
        );
    }
    //    private Cursor getAllProfiles2(int idKey) {
//        // COMPLETED (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
//        return mDb.query(
//                ProfileInfo.TABLE_NAME,
//                new String[] {String.valueOf(ProfileInfo.SONG_NAME)},
//                ProfileInfo._ID + "= ?",
//                new String[] {String.valueOf(idKey)},
//                null,
//                null,
//                ProfileslistContract.ProfileInfo.COLUMN_TIMESTAMP
//        );
//    }
        //Log.d(TAG, "            Line 226");
    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
        Log.d(TAG, "setupMediaRecorder:    patsave is : "+ pathSave );

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

        }, REQUEST_PERMISION_CODE);
    }
    //

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISION_CODE: {
                if (grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_strage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_strage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
    // to add new records to the porfile
    private Long addToProfile(String userName, String songName, String URI) {
        // COMPLETED (5) Inside, create a ContentValues instance to pass the values onto the insert query
        ContentValues cv = new ContentValues();
        // COMPLETED (6) call put to insert the name value with the key COLUMN_GUEST_NAME
        cv.put(ProfileslistContract.ProfileInfo.COLUMN_PROFILE_NAME, userName);
        // COMPLETED (7) call put to insert the party size value with the key COLUMN_PARTY_SIZE
        cv.put(ProfileslistContract.ProfileInfo.SONG_NAME, songName);
        cv.put(ProfileslistContract.ProfileInfo.COLUMN_PROFILE_NAME, userName);
        //cv.put(ProfileslistContract.ProfileInfo.COLUMN_RECORDED_AUDIO, " Empty");

        // COMPLETED (8) call insert to run an insert query on TABLE_NAME with the ContentValues created
        return mDb.insert(ProfileslistContract.ProfilelistEntry.TABLE_NAME, null, cv);
    }

}
