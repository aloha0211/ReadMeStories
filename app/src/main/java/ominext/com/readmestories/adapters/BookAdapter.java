package ominext.com.readmestories.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ominext.com.readmestories.BR;
import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.BaseActivity;
import ominext.com.readmestories.activities.BookDetailActivity;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Fragment mFragment;
    private List<Book> mBooks;

    public BookAdapter(Fragment fragment, List<Book> list) {
        this.mFragment = fragment;
        this.mBooks = list;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.item_book, parent, false);
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
        Intent intent = new Intent(mFragment.getContext(), BookDetailActivity.class);
        Bundle data = new Bundle();
        data.putParcelable(Constant.KEY_BOOK, book);
        intent.putExtra(Constant.KEY_DATA, data);
        mFragment.startActivityForResult(intent, 100);
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding mBinding;

        BookViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
