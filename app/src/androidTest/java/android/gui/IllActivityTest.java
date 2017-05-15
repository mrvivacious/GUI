package android.gui;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.junit.Test;

/**
 * Created by cqdeveloper on 5/2/17.
 */
public class IllActivityTest {

    public Long before;
    public Long after;
    public int diff = 1;

    /**
     * Test if the counter is properly incremented
     */
    @Test
    public void incrementTest() {

        FirebaseDatabase.getInstance().getReference("INICount")
                .runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                before = (Long) currentData.getValue();

                if (currentData.getValue() == null) {
                    currentData.setValue(0);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                    after = (Long) currentData.getValue();
                }


                assert(after - before == diff);
                return Transaction.success(currentData);

            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                //DOES NOTHING
                //Exists only so that this entire method can run.
            }

        });
    }


}