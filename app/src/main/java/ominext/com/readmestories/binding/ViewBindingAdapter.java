package ominext.com.readmestories.binding;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ominext.com.readmestories.R;
import ominext.com.readmestories.glidemodule.GlideApp;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"app:imagePath"})
    public static void loadImage(final ImageView imageView, String imagePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child(imagePath + "/image/cover");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                GlideApp.with(imageView.getContext())
                        .load(uri)
                        .placeholder(R.drawable.background)
                        .into(imageView);
            }
        });
    }
}
