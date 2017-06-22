package ominext.com.readmestories.fragments;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * Created by LuongHH on 6/22/2017.
 */

class ReadingBookPresenter {

    private ReadingBookView mView;
    private Context mContext;

    ReadingBookPresenter(Context context, ReadingBookFragment view) {
        mView = view;
        mContext = context;
    }

    void download(String filePath, String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
//        StorageReference storageRef = storage.getReferenceFromUrl("gs://readmestories-2c388.appspot.com");
        StorageReference pathReference = storageRef.child(filePath + "/" + fileName);
        File cDir = mContext.getCacheDir();
        File cachingFolder = new File(cDir.getPath() + "/" + filePath);
        cachingFolder.mkdirs();
        final File tempFile = new File(cachingFolder, fileName);
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pathReference.getFile(tempFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        mView.onDownloadSuccessful(tempFile.getPath());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                mView.onDownloadFailed();
            }
        });
    }
}
