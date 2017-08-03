package ominext.com.readmestories.fragments.library;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.models.Book;

/**
 * Created by LuongHH on 6/23/2017.
 */

class LibraryPresenter {

    private Context mContext;
    private LibraryView mView;

    LibraryPresenter(Context context, LibraryView view) {
        mContext = context;
        mView = view;
    }

    private boolean isConnected;
    void getListBook() {
        isConnected = false;
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isConnected = true;
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                Iterator<DataSnapshot> item = dataSnapshots.iterator();
                List<Book> bookList = new ArrayList<>();
                while (item.hasNext()) {
                    Book book = item.next().getValue(Book.class);
                    bookList.add(book);
                }
                mView.onSuccessful(bookList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isConnected = true;
                Log.e("Get List Book", databaseError.getMessage());
                mView.onFailed(databaseError.getMessage());
            }
        };
        database.addValueEventListener(eventListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isConnected) {
                    database.removeEventListener(eventListener);
                    mView.onFailed(mContext.getString(R.string.no_connection_message));
                }
            }
        }, 30000);
    }
}
