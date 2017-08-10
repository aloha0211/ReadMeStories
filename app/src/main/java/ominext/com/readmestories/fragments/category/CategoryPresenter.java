package ominext.com.readmestories.fragments.category;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.models.Category;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.network.Connectivity;

/**
 * Created by LuongHH on 6/23/2017.
 */

class CategoryPresenter {

    private Context mContext;
    private CategoryView mView;

    CategoryPresenter(Context context, CategoryView view) {
        mContext = context;
        mView = view;
    }

    private boolean isConnected;
    void getCategories() {
        if (!Connectivity.isConnected(mContext)) {
            mView.onFailed(mContext.getString(R.string.no_connection_message));
            return;
        }
        isConnected = false;
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isConnected = true;
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                Iterator<DataSnapshot> item = dataSnapshots.iterator();
                List<Category> categories = new ArrayList<>();
                while (item.hasNext()) {
                    Category category = item.next().getValue(Category.class);
                    categories.add(category);
                }
                mView.onSuccessful(categories);
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
                    mView.onFailed(mContext.getString(R.string.load_data_err_msg));
                }
            }
        }, 30000);
    }

    void getImageUri(String categoryImageName) {
        if (!Connectivity.isConnected(mContext)) {
            mView.onFailed(mContext.getString(R.string.no_connection_message));
            return;
        }
        isConnected = false;
        OnFailureListener onFailureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isConnected = true;
                mView.onFailed(mContext.getString(R.string.load_data_err_msg));
            }
        };
        OnSuccessListener<Uri> onSuccessListener = new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                isConnected = true;
                mView.onLoadImageUriSuccessful(uri);
            }
        };
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child(Constant.CATEGORY + "/" + Constant.IMAGE + "/" + categoryImageName);
        final Task<Uri> task = imageRef.getDownloadUrl();
        task.addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isConnected) {
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    mView.onFailed(mContext.getString(R.string.load_data_err_msg));
                }
            }
        }, 30000);
    }
}
