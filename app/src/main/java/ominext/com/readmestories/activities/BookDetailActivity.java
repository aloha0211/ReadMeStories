package ominext.com.readmestories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.UnsupportedEncodingException;

import ominext.com.readmestories.R;
import ominext.com.readmestories.listeners.DownloadFileListener;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;
import ominext.com.readmestories.utils.network.Connectivity;

public class BookDetailActivity extends BaseActivity implements View.OnClickListener {

    private Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getString(R.string.book_detail));

        TextView tvTitle = (TextView) findViewById(R.id.tv_book_title);
        TextView tvAuthor = (TextView) findViewById(R.id.tv_book_author);
        TextView tvIllustrator = (TextView) findViewById(R.id.tv_book_illustrator);
        TextView btnRead = (TextView) findViewById(R.id.btn_read);
        TextView btnAddBook = (TextView) findViewById(R.id.btn_add_to_my_books);
        ImageView ivBook = (ImageView) findViewById(R.id.iv_book);

        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(Constant.KEY_DATA);
        mBook = data.getParcelable(Constant.KEY_BOOK);
        tvTitle.setText(mBook.getTitle());
        tvAuthor.setText(mBook.getAuthor());
        tvIllustrator.setText(mBook.getIllustrator());
        Utils.loadImageFromCache(ivBook, Constant.STORY + "/" + Constant.CATEGORY, mBook.getId().toString(), Constant.COVER);

        btnRead.setOnClickListener(this);
        btnAddBook.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.deleteCacheDir(BookDetailActivity.this, Constant.STORY + "/" + Constant.TEMP);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_read) {
            showProgressDialog(getString(R.string.loading_data));
            getBookContentFromFirebase();
        } else if (view.getId() == R.id.btn_add_to_my_books) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBookContentFromFirebase() {
        if (!Connectivity.isConnected(this)) {
            dismissProgressDialog();
            showAlertDialog(getString(R.string.error), getString(R.string.no_connection_message));
            return;
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference htmlRef = storageRef.child(Constant.STORY + "/" + mBook.getId().toString() + "/" + Constant.HTML_FILE_NAME);
        final long ONE_MEGABYTE = 1024 * 1024;
        htmlRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String htmlContentStr = new String(bytes, "UTF-8");
                    Book result = Utils.parseHtml(htmlContentStr);
                    mBook.setContent(result.getContent());
                    mBook.settime_frame(result.gettime_frame());
                    downloadBookAudio();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    dismissProgressDialog();
                    showAlertDialog(getString(R.string.error), getString(R.string.load_data_err_msg));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                dismissProgressDialog();
                showAlertDialog(getString(R.string.error), getString(R.string.load_data_err_msg));
            }
        });
    }

    private void downloadBookAudio() {
        mFileDownloadedIndex = 0;
        mTotalFile = mBook.getContent().size() + 2;
        refPath = mBook.getId().toString() + "/" + Constant.AUDIO;
        storePath = Constant.TEMP + "/" + refPath;
        Utils.download(this, refPath, storePath, Constant.COVER + Constant.MP3_EXTENSION, mListener);
    }

    private int mFileDownloadedIndex;
    private int mTotalFile;
    private String refPath;
    private String storePath;

    private DownloadFileListener mListener = new DownloadFileListener() {
        @Override
        public void onDownloadSuccessful(String audioPath) {
            mFileDownloadedIndex++;
            if (mFileDownloadedIndex < mTotalFile - 1) {
                Utils.download(BookDetailActivity.this, refPath, storePath, mFileDownloadedIndex + Constant.MP3_EXTENSION, mListener);
            } else if (mFileDownloadedIndex == mTotalFile - 1) {
                Utils.download(BookDetailActivity.this, refPath, storePath, Constant.BACK_COVER + Constant.MP3_EXTENSION, mListener);
            } else if (mFileDownloadedIndex == mTotalFile) {
                // download audio finished, start downloading image
                refPath = mBook.getId().toString() + "/" + Constant.IMAGE;
                storePath = Constant.TEMP + "/" + refPath;
                Utils.download(BookDetailActivity.this, refPath, storePath, Constant.COVER, mListener);
            } else if (mFileDownloadedIndex < 2 * mTotalFile - 1) {
                Utils.download(BookDetailActivity.this, refPath, storePath, String.valueOf(mFileDownloadedIndex - mTotalFile), mListener);
            } else if (mFileDownloadedIndex == 2 * mTotalFile - 1) {
                Utils.download(BookDetailActivity.this, refPath, storePath, Constant.BACK_COVER, mListener);
            } else if (mFileDownloadedIndex == 2 * mTotalFile) {
                // download all files finished
                Intent intent = new Intent(BookDetailActivity.this, ReadingBookActivity.class);
                Bundle data = new Bundle();
                data.putParcelable(Constant.KEY_BOOK, mBook);
                intent.putExtra(Constant.KEY_READING_MODE, Constant.MODE_FROM_CACHE_NOT_ADDED_YET);
                intent.putExtra(Constant.KEY_DATA, data);
                BookDetailActivity.this.startActivity(intent);
                dismissProgressDialog();
            }
        }

        @Override
        public void onDownloadFailed() {
            dismissProgressDialog();
            showAlertDialog(BookDetailActivity.this.getString(R.string.error), BookDetailActivity.this.getString(R.string.load_data_err_msg));
            Utils.deleteCacheDir(BookDetailActivity.this, Constant.TEMP);
        }
    };
}
