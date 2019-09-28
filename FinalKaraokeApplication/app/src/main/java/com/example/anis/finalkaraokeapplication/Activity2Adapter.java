package com.example.anis.finalkaraokeapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anis.finalkaraokeapplication.data.ProfileslistContract;

/**
 * Created by Anis on 3/13/2018.
 */

public class Activity2Adapter extends RecyclerView.Adapter<Activity2Adapter.ProfileViewHolder> {




    private Cursor mCursor;
    private Context mContext;

    public Activity2Adapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }
    @Override
    public Activity2Adapter.ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.profile_list_item, parent, false);
        return new Activity2Adapter.ProfileViewHolder(view);



    }


    @Override
    public void onBindViewHolder(final Activity2Adapter.ProfileViewHolder holder, final int position) {
        //final Context list =
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        String name = mCursor.getString(mCursor.getColumnIndex(ProfileslistContract.ProfilelistEntry.COLUMN_PROFILE_NAME));
        int age = mCursor.getInt(mCursor.getColumnIndex(ProfileslistContract.ProfilelistEntry.COLUMN_AGE));
        // COMPLETED (6) Retrieve the id from the cursor and
        final long id = mCursor.getLong(mCursor.getColumnIndex(ProfileslistContract.ProfilelistEntry._ID));
        // Display the  name
        holder.nameTextView.setText(name);
        // Display the age
        holder.ageTextView.setText(String.valueOf(age));

        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);
        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //openNewActivity(id);

                Toast.makeText(mContext, "You clicked  "+ position, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void openNewActivity(long id) {
//        Intent intent = new Intent(mContext.getApplicationContext(), Activity2.class);
//        //Intent intent = new Intent(getApplicationContext(), Activity2.class);
//        intent.putExtra("userName", id);
//        mContext.startActivity(intent);
//    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // COMPLETED (16) Inside, check if the current cursor is not null, and close it if so
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        // COMPLETED (17) Update the local mCursor to be equal to  newCursor
        mCursor = newCursor;
        // COMPLETED (18) Check if the newCursor is not null, and call this.notifyDataSetChanged() if so
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
    class ProfileViewHolder extends RecyclerView.ViewHolder {

        // Will display the  name
        TextView nameTextView;
        // Will display the age
        TextView ageTextView;
        public LinearLayout linearlayout;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            ageTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
            linearlayout = (LinearLayout) itemView.findViewById(R.id.linearlayout);
        }


    }

}

