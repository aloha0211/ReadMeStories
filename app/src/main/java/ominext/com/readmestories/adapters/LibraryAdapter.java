package ominext.com.readmestories.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

//import ominext.com.readmestories.BR;
import ominext.com.readmestories.BR;
import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.BaseActivity;
import ominext.com.readmestories.activities.ReadingBookActivity;
import ominext.com.readmestories.listeners.DownloadFileListener;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.BookViewHolder> {

    private Context mContext;
    private List<Book> mBooks;

    public LibraryAdapter(Context context, List<Book> list) {
        this.mContext = context;
        this.mBooks = list;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_library, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = mBooks.get(position);
        holder.mBinding.setVariable(BR.book, book);
        holder.mBinding.setVariable(BR.handlers, this);
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mBooks == null ? 0 : mBooks.size();
    }

    public void onBookClick(View view, Book book) {
        ((BaseActivity) mContext).showProgressDialog(mContext.getString(R.string.loading_data));
        mSelectedBook = book;
        mFileDownloadedIndex = 0;
        mTotalFile = book.getContent().size() + 2;
        // firstly, download audio file
//        Utils.download(mContext, book.getId() + "/" + Constant.AUDIO, Constant.COVER + Constant.MP3_EXTENSION, mListener);
    }

    private int mFileDownloadedIndex;
    private int mTotalFile;
    private Book mSelectedBook;

    private DownloadFileListener mListener = new DownloadFileListener() {
        @Override
        public void onDownloadSuccessful(String audioPath) {
            mFileDownloadedIndex++;
            if (mFileDownloadedIndex < mTotalFile - 1){
//                Utils.download(mContext, mSelectedBook.getId() + "/" + Constant.AUDIO, mFileDownloadedIndex + Constant.MP3_EXTENSION, mListener);
            } else if (mFileDownloadedIndex == mTotalFile - 1) {
//                Utils.download(mContext, mSelectedBook.getId() + "/" + Constant.AUDIO, Constant.BACK_COVER + Constant.MP3_EXTENSION, mListener);
            } else if (mFileDownloadedIndex == mTotalFile) {
                // download audio finished, start downloading image
//                Utils.download(mContext, mSelectedBook.getId() + "/" + Constant.IMAGE, Constant.COVER, mListener);
            } else if (mFileDownloadedIndex < 2 * mTotalFile - 1) {
//                Utils.download(mContext, mSelectedBook.getId() + "/" + Constant.IMAGE, String.valueOf(mFileDownloadedIndex - mTotalFile), mListener);
            } else if (mFileDownloadedIndex == 2 * mTotalFile - 1) {
//                Utils.download(mContext, mSelectedBook.getId() + "/" + Constant.IMAGE, Constant.BACK_COVER, mListener);
            } else if (mFileDownloadedIndex == 2 * mTotalFile) {
                // download all files finished
                Intent intent = new Intent(mContext, ReadingBookActivity.class);
                Bundle data = new Bundle();
                data.putParcelable(Constant.KEY_BOOK, mSelectedBook);
                intent.putExtra(Constant.IS_FROM_ASSET, false);
                intent.putExtra(Constant.KEY_DATA, data);
                mContext.startActivity(intent);
                ((BaseActivity) mContext).dismissProgressDialog();
            }
        }

        @Override
        public void onDownloadFailed() {
            ((BaseActivity) mContext).dismissProgressDialog();
            ((BaseActivity) mContext).showAlertDialog(mContext.getString(R.string.error), mContext.getString(R.string.load_data_err_msg));
            Utils.deleteCacheDir(mContext, Constant.STORY + "/" + mSelectedBook.getId());
        }
    };

    class BookViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding mBinding;

        BookViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
