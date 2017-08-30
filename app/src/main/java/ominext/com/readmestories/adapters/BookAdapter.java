package ominext.com.readmestories.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ominext.com.readmestories.BR;
import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.BaseActivity;
import ominext.com.readmestories.activities.ReadingBookActivity;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context mContext;
    private List<Book> mBooks;

    public BookAdapter(Context context, List<Book> list) {
        this.mContext = context;
        this.mBooks = list;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_book, parent, false);
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

    public void onBookClick(View view, final Book book) {
        ((BaseActivity) mContext).showProgressDialog("");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).dismissProgressDialog();
                Intent intent = new Intent(mContext, ReadingBookActivity.class);
                Bundle data = new Bundle();
                data.putParcelable(Constant.KEY_BOOK, book);
                intent.putExtra(Constant.KEY_DATA, data);
                mContext.startActivity(intent);
            }
        }, 500);
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding mBinding;

        BookViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
