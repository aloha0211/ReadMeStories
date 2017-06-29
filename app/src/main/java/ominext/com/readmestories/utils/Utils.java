package ominext.com.readmestories.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import ominext.com.readmestories.R;
import ominext.com.readmestories.glidemodule.GlideApp;
import ominext.com.readmestories.listeners.DownloadFileListener;

/**
 * Created by LuongHH on 6/27/2017.
 */

public class Utils {

    public static void loadImage(final ImageView imageView, String bookId, String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child(bookId + "/image/" + fileName);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                GlideApp.with(imageView.getContext().getApplicationContext())
                        .load(uri)
                        .placeholder(R.drawable.background)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "Load Image failed: " + e.getMessage());
            }
        });
    }

    public static void download(Context context, String filePath, String fileName, final DownloadFileListener listener) {
        File cDir = context.getCacheDir();
        File cachingFolder = new File(cDir.getPath() + "/" + filePath);
        cachingFolder.mkdirs();
        final File tempFile = new File(cachingFolder, fileName);
        if (tempFile.exists()) {
            listener.onDownloadSuccessful(tempFile.getPath());
        } else {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
//            StorageReference storageRef = storage.getReferenceFromUrl("gs://readmestories-2c388.appspot.com");
            StorageReference pathReference = storageRef.child(filePath + "/" + fileName);
            pathReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            listener.onDownloadSuccessful(tempFile.getPath());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    listener.onDownloadFailed();
                }
            });
        }
    }
}
