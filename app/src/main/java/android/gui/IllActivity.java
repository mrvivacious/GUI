package android.gui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class IllActivity extends AppCompatActivity {

    private static final String TAG = "IllActivity";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private TextView iniCount;
    private Button INI;
    private Button share;
    private Button signout;

    //Build the notification which appears when counter is incremented
    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(IllActivity.this)
                    .setSmallIcon(R.drawable.illinois_fighting_illini)
                    .setContentTitle("Someone just INI'd!")
                    .setContentText("Show your spirit by INI'ing too!");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ill);

        iniCount = (TextView) findViewById(R.id.tv_total);
        INI = (Button) findViewById(R.id.b_ini);
        share = (Button) findViewById(R.id.b_share);
        signout = (Button) findViewById(R.id.b_signout);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("INICount");


        // Read from the database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long value = dataSnapshot.getValue(long.class);
                String strLong = Long.toString(value);

                iniCount.setText(strLong);

                //Build the notification
                //Notify users when the INI counter increments
                // Sets an ID for the notification
                int mNotificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

                //Makes the notification clickable
                Intent resultIntent = new Intent(IllActivity.this, LoginActivity.class);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity( IllActivity.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentIntent(resultPendingIntent);
            }

            //Debugging purposes
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //When button is clicked, increment the counter by 1
        INI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mDatabaseReference.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(final MutableData currentData) {
                            //Get the current value of the INI counter
                            if (currentData.getValue() == null) {
                                currentData.setValue(0);
                            } else {
                                currentData.setValue((Long) currentData.getValue() + 1);
                            }
                            return Transaction.success(currentData);
                        }

                        //Exists only so that this entire method can run.
                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            //DOES NOTHING//
                        }

                    });
                }
        });

        //Share the current INI count with your friends, family or classmates at lame-ass schools (aka not UIUC)
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareCount = new Intent(Intent.ACTION_SEND);
                //You can now share the message through multiple text-sharing apps
                shareCount.setType("text/plain");
                //Text's subject
                shareCount.putExtra(Intent.EXTRA_SUBJECT,
                        "School spirit was shared for " + iniCount.getText() + " times!");

                //Text's greeting text
                shareCount.putExtra(Intent.EXTRA_TEXT, "Help add more cheers! I-L-L!");
                shareCount.putExtra(Intent.EXTRA_TEXT, "Sent from \"I-L-L,\" the premier school spirit app for" +
                        "the University of Illinois, Urbana - Champaign.");
                //Begin the intent
                startActivity(Intent.createChooser(shareCount, "Share:"));
            }
        });

        //Signout button signs user out
        //Not sure why anyone would want to sign out of my groundbreaking application, though :( .....
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signout = new Intent(IllActivity.this, LoginActivity.class);
                startActivity(signout);
            }
        });

    }
}
