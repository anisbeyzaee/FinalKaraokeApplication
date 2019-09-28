package com.example.anis.finalkaraokeapplication;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anis.finalkaraokeapplication.data.ProfilelistDbHelper;
import com.example.anis.finalkaraokeapplication.data.ProfileslistContract;
import com.example.anis.finalkaraokeapplication.data.ProfileslistContract.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
   // int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
    private ProfileListAdapter mAdapter;

    // Create a local field SQLiteDatabase called mDb
    private SQLiteDatabase mDb;
    // Create local EditText fields for mNewGuestNameEditText and mNewPartySizeEditText
    private EditText mNewPNameEditText;
    private EditText mNewAgeEditText;
    RecyclerView.ViewHolder viewHolder;
    //  Create a constant string LOG_TAG that is equal to the class.getSimpleName()
    //private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.d(TAG, ((" = ") + lineNumber));
        RecyclerView profilelistRecyclerView;

        // Set local attributes to corresponding views
        profilelistRecyclerView = (RecyclerView) this.findViewById(R.id.all_profile_list);
        //  Set the Edit texts to the corresponding views using findViewById
        mNewPNameEditText = (EditText) this.findViewById(R.id.person_name_edit_text);
        mNewAgeEditText = (EditText) this.findViewById(R.id.age_edit_text);
      //Log.d(TAG, "     Line45");
        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        profilelistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        Cursor cursor = getAllProfiles();
        //Log.d(TAG, "     Line64");
        //  Pass the resulting cursor count to the adapter
        // Create an adapter for that cursor to display the data
        mAdapter = new ProfileListAdapter(this, cursor);
        //Log.d(TAG, "     Line69");
        // Link the adapter to the RecyclerView
        profilelistRecyclerView.setAdapter(mAdapter);



        // COMPLETED (3) Create a new ItemTouchHelper with a SimpleCallback that handles both LEFT and RIGHT swipe directions
        // Create an item touch helper to handle swiping items off the list
        //Log.d(TAG, "     Line74");
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            // COMPLETED (5) Override onSwiped
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                long id = (long) viewHolder.itemView.getTag();
                // COMPLETED (9) call removeGuest and pass through that id
                //remove from DB
                removeProfile(id);
                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                //update the list
                mAdapter.swapCursor(getAllProfiles());
            }


            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(profilelistRecyclerView);



    }



    public void addToProfilelist(View view) {
        //First thing, check if any of the EditTexts are empty, return if so
        if (mNewPNameEditText.getText().length() == 0) {
            return;
        }
        // COMPLETED (10) Create an integer to store the party size and initialize to 1
        //default party size to 1
        int age= 25;
        // COMPLETED (11) Use Integer.parseInt to parse mNewPartySizeEditText.getText to an integer
        try {
            //mNewPartyCountEditText inputType="number", so this should always work
            age = Integer.parseInt(mNewAgeEditText.getText().toString());
        } catch (NumberFormatException ex) {
            // COMPLETED (12) Make sure you surround the Integer.parseInt with a try catch and log any exception
            //Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
        }

        // COMPLETED (14) call addNewGuest with the guest name and party size
        // Add guest info to mDb
        addNewProfile(mNewPNameEditText.getText().toString(), age);

        // COMPLETED (15) call mAdapter.swapCursor to update the cursor by passing in getAllGuests()
        // Update the cursor in the adapter to trigger UI to display the new list
        //mAdapter.swapCursor(getAllProfiles());

        // COMPLETED (16) To make the UI look nice, call .getText().clear() on both EditTexts, also call clearFocus() on mNewPartySizeEditText
        //clear UI text fields
        mAdapter.swapCursor(getAllProfiles());
        mNewAgeEditText.clearFocus();
        mNewPNameEditText.getText().clear();
        mNewAgeEditText.getText().clear();
       
        // long id = (long) viewHolder.itemView.getTag(); // it can be used when I have  OnClick for recycler view implemented. but now here
        // for a new profile added I just have to retrieve the id from that insert table or create profile

        
        long id = returnId();
        
        openKaraoke(id);

    }

    private long returnId() {
        String selectQuery = "SELECT *  FROM " + ProfilelistEntry.TABLE_NAME;
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        long str ;
        cursor.moveToLast();
        str = cursor.getColumnIndex(String.valueOf(2));
        return str;

    }

    public void openKaraoke(long id) {
        Intent intent = new Intent(getApplicationContext(), Activity2.class);
       // long userName2 = (long) viewHolder.itemView.getTag();
        intent.putExtra("userName/id", id);
        Log.d(TAG, "Youu      entered     ....." + id  );
        startActivity(intent);
    }


    // COMPLETED (5) Create a private method called getAllGuests that returns a cursor

    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    private Long getId(userName){
//        return " ";
//    }



    private Cursor getAllProfiles() {
        // COMPLETED (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
        return mDb.query(
                ProfilelistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ProfileslistContract.ProfilelistEntry.COLUMN_TIMESTAMP
        );
    }

    private Cursor getAllProfiles2() {
        // COMPLETED (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
        return mDb.query(
                ProfileInfo.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ProfileslistContract.ProfilelistEntry.COLUMN_TIMESTAMP
        );
    }
    private Long addNewProfile(String name, int age) {

        // COMPLETED (5) Inside, create a ContentValues instance to pass the values onto the insert query
        ContentValues cv = new ContentValues();
        // COMPLETED (6) call put to insert the name value with the key COLUMN_GUEST_NAME
        cv.put(ProfileslistContract.ProfilelistEntry.COLUMN_PROFILE_NAME, name);
        // COMPLETED (7) call put to insert the party size value with the key COLUMN_PARTY_SIZE
        cv.put(ProfileslistContract.ProfilelistEntry.COLUMN_AGE, age);
        cv.put(ProfileslistContract.ProfileInfo.COLUMN_PROFILE_NAME, name);
        //cv.put(ProfileslistContract.ProfileInfo.COLUMN_RECORDED_AUDIO, " Empty");

        // COMPLETED (8) call insert to run an insert query on TABLE_NAME with the ContentValues created

        return mDb.insert(ProfileslistContract.ProfilelistEntry.TABLE_NAME, null, cv);
    }


    // COMPLETED (1) Create a new function called removeGuest that takes long id as input and returns a boolean

    /**
     * Removes the record with the specified id
     *
     * @param id the DB id to be removed
     * @return True: if removed successfully, False: if failed
     */
    private boolean removeProfile(long id) {
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(ProfileslistContract.ProfilelistEntry.TABLE_NAME, ProfileslistContract.ProfilelistEntry._ID + "=" + id, null) > 0;
    }

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