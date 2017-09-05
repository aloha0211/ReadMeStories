package ominext.com.readmestories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import io.realm.RealmList;
import ominext.com.readmestories.R;
import ominext.com.readmestories.listeners.DownloadFileListener;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.models.BookRealm;
import ominext.com.readmestories.realm.RealmController;
import ominext.com.readmestories.realm.RealmListDouble;
import ominext.com.readmestories.realm.RealmString;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;
import ominext.com.readmestories.utils.network.Connectivity;

public class BookDetailActivity extends BaseActivity implements View.OnClickListener {

    TextView btnAddBook;

    private Book mBook;

    private boolean isSavingBookToInternalStorage;  // false if download file and save to cache folder

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
        btnAddBook = (TextView) findViewById(R.id.btn_add_to_my_books);
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
        if (RealmController.with(this).getBook(mBook.getId()) != null) {
            btnAddBook.setBackgroundResource(android.R.color.darker_gray);
            btnAddBook.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.deleteCacheDir(BookDetailActivity.this, Constant.STORY + "/" + Constant.TEMP);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_read) {
            final BookRealm localBook = RealmController.with(this).getBook(mBook.getId());
            if (localBook != null) {        // read book from internal storage
                showProgressDialog("");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Book book = new Book(mBook.getId(), mBook.getTitle(), Utils.parseContent(localBook.getContent()), Utils.parseTimeFrame(localBook.getTime_frame()));
                        book.setReadingMode(Constant.MODE_FROM_INTERNAL_STORAGE);
                        dismissProgressDialog();
                        Intent intent = new Intent(BookDetailActivity.this, ReadingBookActivity.class);
                        Bundle data = new Bundle();
                        data.putParcelable(Constant.KEY_BOOK, book);
                        intent.putExtra(Constant.KEY_DATA, data);
                        BookDetailActivity.this.startActivity(intent);
                    }
                }, 500);
            } else {                    // download book into cache folder and read
                showProgressDialog(getString(R.string.loading_data));
                isSavingBookToInternalStorage = false;
                getBookContentFromFirebase();

            }
        } else if (view.getId() == R.id.btn_add_to_my_books) {
            showProgressDialog(getString(R.string.loading_data));
            isSavingBookToInternalStorage = true;
            getBookContentFromFirebase();
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
                    if (isSavingBookToInternalStorage) {
                        BookRealm bookRealm = new BookRealm();
                        bookRealm.setId(mBook.getId());
                        bookRealm.setTitle(mBook.getTitle());
                        RealmList<RealmString> contents = new RealmList<>();
                        for (int i = 0; i < mBook.getContent().size(); i++) {
                            contents.add(new RealmString(mBook.getContent().get(i)));
                        }
                        bookRealm.setContent(contents);
                        RealmList<RealmListDouble> timeFrameList = new RealmList<>();
                        for (int i = 0; i < mBook.gettime_frame().size(); i++) {
                            RealmListDouble timeFrame = new RealmListDouble(mBook.gettime_frame().get(i));
                            timeFrameList.add(timeFrame);
                        }
                        bookRealm.setTime_frame(timeFrameList);
                        RealmController.with(BookDetailActivity.this).addBook(bookRealm);
                    }
                    downloadBookAudioAndImage();
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

    private void downloadBookAudioAndImage() {
        mFileDownloadedIndex = 0;
        mTotalFile = mBook.getContent().size() + 2;
        refPath = mBook.getId().toString() + "/" + Constant.AUDIO;
        storePath = isSavingBookToInternalStorage ? Constant.SAVE + "/" + refPath : Constant.TEMP + "/" + refPath;
        downloadFile(refPath, storePath, Constant.COVER + Constant.MP3_EXTENSION);
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
                downloadFile(refPath, storePath, mFileDownloadedIndex + Constant.MP3_EXTENSION);
            } else if (mFileDownloadedIndex == mTotalFile - 1) {
                downloadFile(refPath, storePath, Constant.BACK_COVER + Constant.MP3_EXTENSION);
            } else if (mFileDownloadedIndex == mTotalFile) {
                // downloadToCacheFolder audio finished, start downloading image
                refPath = mBook.getId().toString() + "/" + Constant.IMAGE;
                storePath = isSavingBookToInternalStorage ? Constant.SAVE + "/" + refPath : Constant.TEMP + "/" + refPath;
                downloadFile(refPath, storePath, Constant.COVER);
            } else if (mFileDownloadedIndex < 2 * mTotalFile - 1) {
                downloadFile(refPath, storePath, String.valueOf(mFileDownloadedIndex - mTotalFile));
            } else if (mFileDownloadedIndex == 2 * mTotalFile - 1) {
                downloadFile(refPath, storePath, Constant.BACK_COVER);
            } else if (mFileDownloadedIndex == 2 * mTotalFile) {
                // download all files finished
                dismissProgressDialog();
                if (isSavingBookToInternalStorage) {
                    showAlertDialog(getString(R.string.book_detail), getString(R.string.added_to_your_books), R.color.green);
                    btnAddBook.setBackgroundResource(android.R.color.darker_gray);
                    btnAddBook.setEnabled(false);
                } else {
                    Intent intent = new Intent(BookDetailActivity.this, ReadingBookActivity.class);
                    Bundle data = new Bundle();
                    mBook.setReadingMode(Constant.MODE_FROM_CACHE);
                    data.putParcelable(Constant.KEY_BOOK, mBook);
                    intent.putExtra(Constant.KEY_DATA, data);
                    BookDetailActivity.this.startActivity(intent);
                }
            }
        }

        @Override
        public void onDownloadFailed() {
            dismissProgressDialog();
            showAlertDialog(BookDetailActivity.this.getString(R.string.error), BookDetailActivity.this.getString(R.string.load_data_err_msg));
            Utils.deleteCacheDir(BookDetailActivity.this, Constant.TEMP);
        }
    };

    private void downloadFile(String refPath, String storePath, String fileName) {
        if (isSavingBookToInternalStorage) {
            Utils.downloadToInternalStorage(BookDetailActivity.this, refPath, storePath, fileName, mListener);
        } else {
            Utils.downloadToCacheFolder(BookDetailActivity.this, refPath, storePath, fileName, mListener);
        }
    }
}