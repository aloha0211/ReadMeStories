package ominext.com.readmestories.fragments.mybooks;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ominext.com.readmestories.models.Book;

/**
 * Created by LuongHH on 6/23/2017.
 */

class MyBooksPresenter {

    private Context mContext;
    private MyBooksView mView;

    MyBooksPresenter(Context context, MyBooksView view) {
        mContext = context;
        mView = view;
    }

    void getListBook() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                Log.e("Get List Book", databaseError.getMessage());
            }
        });
    }
}
