package ominext.com.readmestories.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.listeners.DownloadFileListener;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.network.Connectivity;

import static ominext.com.readmestories.utils.Constant.ASSET_FILE_NAME;

/**
 * Created by LuongHH on 6/27/2017.
 */

public class Utils {

    public static void loadImageFromFirebase(final ImageView imageView, String bookId, String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child(bookId + "/" + Constant.IMAGE + "/" + fileName);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(imageView.getContext().getApplicationContext())
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

    public static void loadImageFromAssets(final ImageView imageView, String bookId, String fileName) {
        try {
            InputStream is = imageView.getContext().getAssets().open(bookId + "/" + Constant.IMAGE + "/" + fileName);
            imageView.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadImageFromCache(final ImageView imageView, String bookId, String fileName) {
        File cDir = imageView.getContext().getCacheDir();
        String filePath = cDir.getPath() + "/" + Constant.STORY + "/" + bookId + "/" + Constant.IMAGE + "/" + fileName;
        imageView.setImageDrawable(Drawable.createFromPath(filePath));
    }

    public static void download(Context context, String filePath, String fileName, final DownloadFileListener listener) {

        File cDir = context.getCacheDir();
        File cacheFolder = new File(cDir.getPath() + "/" + Constant.STORY + "/" + filePath);
        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }

        if (!Connectivity.isConnected(context)) {
            listener.onDownloadFailed();
            return;
        }

        final File tempFile = new File(cacheFolder, fileName);
        if (tempFile.exists()) {
            tempFile.delete();
        }

        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
//            StorageReference storageRef = storage.getReferenceFromUrl("gs://readmestories-2c388.appspot.com");
        StorageReference pathReference = storageRef.child(filePath + "/" + fileName);

        final boolean[] isConnected = {false};

        final FileDownloadTask downloadTask = pathReference.getFile(tempFile);
        final OnSuccessListener onSuccessListener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Successfully downloaded data to local file
                isConnected[0] = true;
                listener.onDownloadSuccessful(tempFile.getPath());
            }
        };
        final OnFailureListener onFailureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                isConnected[0] = true;
                // Handle failed download
                listener.onDownloadFailed();
            }
        };
        downloadTask.addOnSuccessListener(onSuccessListener);
        downloadTask.addOnFailureListener(onFailureListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isConnected[0]) {
                    downloadTask.removeOnFailureListener(onFailureListener);
                    downloadTask.removeOnSuccessListener(onSuccessListener);
                    listener.onDownloadFailed();
                }
            }
        }, 30000);
    }

    public static void deleteCacheDir(Context context, String filePath) {
        File cDir = context.getCacheDir();
        File cacheFile = new File(cDir.getPath() + "/" + filePath);
        deleteDir(cacheFile);
    }

    public static void deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));
            }
        }
        dir.delete();
    }

    public static List<Book> getLocalBooks(Context context) {
        String jsonData = loadAssetText(context, ASSET_FILE_NAME);
        Type type = new TypeToken<ArrayList<Book>>() {
        }.getType();
        return new Gson().<ArrayList<Book>>fromJson(jsonData, type);
    }

    private static String loadAssetText(Context context, String fileName) {
        StringBuilder buf = new StringBuilder();
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static Book parseHtml(String htmlContentInStringFormat) {

        List<String> contentList = new ArrayList<>();
        List<List<Double>> timeFrame = new ArrayList<>();

        Document htmlDocument = Jsoup.parse(htmlContentInStringFormat);
        Element head = htmlDocument.head();
        String data = head.children().get(4).data();

        String keyText1 = "MeeGenius.Settings.bookId =";
        int bookIdBeginIndex = data.indexOf(keyText1) + keyText1.length();
        String bookIdText = data.substring(bookIdBeginIndex, data.indexOf(",", bookIdBeginIndex)).trim();

        String keyText2 = "MeeGenius.Settings.cues = [[],";
        int timeFrameBeginIndex = data.indexOf(keyText2) + keyText2.length();
        String timeFrameText = data.substring(timeFrameBeginIndex, data.indexOf(", []]", timeFrameBeginIndex));
        String[] timeFrameList = timeFrameText.split("],");
        for (String item: timeFrameList) {
            String[] timeFrameItemList = item.substring(1, item.length() - 1).split(",");
            List<Double> doubleList = new ArrayList<>();
            doubleList.add(Double.valueOf(timeFrameItemList[0].substring(1)));
            for (int i = 1 ; i < timeFrameItemList.length; i++) {
                doubleList.add(Double.valueOf(timeFrameItemList[i]));
            }
            timeFrame.add(doubleList);
        }

        Element body = htmlDocument.body();
        Elements contents = body.children();
        for (int i = 1; i < contents.size() - 1; i++) {
            Element content = contents.get(i);
            contentList.add(content.getElementsByClass("valign").get(0).text());
        }
        return new Book(Integer.parseInt(bookIdText), "", contentList, timeFrame);
    }
}
