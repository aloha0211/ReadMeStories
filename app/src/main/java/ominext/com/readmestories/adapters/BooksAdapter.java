package ominext.com.readmestories.adapters;

import android.app.ProgressDialog;
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

import ominext.com.readmestories.BR;
import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.ReadingActivity;
import ominext.com.readmestories.activities.ReadingBookActivity;
import ominext.com.readmestories.listeners.DownloadFileListener;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.DialogUtils;
import ominext.com.readmestories.utils.ProgressDialogUtils;
import ominext.com.readmestories.utils.Utils;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder>{

    private Context mContext;
    private List<Book> mBooks;

    public BooksAdapter(Context context, List<Book> list) {
        this.mContext = context;
        this.mBooks = list;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_book, parent, false);
        final BookViewHolder holder = new BookViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = mBooks.get(holder.getAdapterPosition());
                onBookClick(book);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = mBooks.get(position);
        holder.mBinding.setVariable(BR.book, book);
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mBooks == null ? 0 : mBooks.size();
    }

    private void onBookClick(Book book) {
        mDialog = ProgressDialogUtils.create(mContext, mContext.getString(R.string.loading_data));
        mDialog.show();
        mBook = book;
        mFileDownloadedCount = 0;
        mTotalFile = 2 * (book.getContent().size() + 2);
        for (int i = 1; i <= book.getContent().size(); i++) {
            Utils.download(mContext, book.getId() + "/" + Constant.AUDIO, i + Constant.MP3_EXTENSION, mListener);
            Utils.download(mContext, book.getId() + "/" + Constant.IMAGE, String.valueOf(i), mListener);
        }
        Utils.download(mContext, book.getId() + "/" + Constant.AUDIO, Constant.BACK_COVER + Constant.MP3_EXTENSION, mListener);
        Utils.download(mContext, book.getId() + "/" + Constant.AUDIO, Constant.COVER + Constant.MP3_EXTENSION, mListener);
        Utils.download(mContext, book.getId() + "/" + Constant.IMAGE, Constant.BACK_COVER, mListener);
        Utils.download(mContext, book.getId() + "/" + Constant.IMAGE, Constant.COVER, mListener);
    }

    private int mFileDownloadedCount;
    private int mTotalFile;
    private Book mBook;
    private ProgressDialog mDialog;

    private DownloadFileListener mListener = new DownloadFileListener() {
        @Override
        public void onDownloadSuccessful(String audioPath) {
            mFileDownloadedCount++;
            if (mFileDownloadedCount == mTotalFile) {
                Intent intent = new Intent(mContext, ReadingActivity.class);
                Bundle data = new Bundle();
                data.putParcelable(Constant.KEY_BOOK, mBook);
                intent.putExtra(Constant.KEY_DATA, data);
                mContext.startActivity(intent);
                mDialog.dismiss();
            }
        }

        @Override
        public void onDownloadFailed() {
            mDialog.dismiss();
            DialogUtils.showAlertDialog(mContext, mContext.getString(R.string.error), mContext.getString(R.string.load_data_err_msg));
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
