package ominext.com.readmestories.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.BaseActivity;
import ominext.com.readmestories.activities.BookDetailActivity;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class BookByCategoryAdapter extends RecyclerView.Adapter<BookByCategoryAdapter.BookViewHolder> {

    private Context mContext;
    private List<Book> mBooks;

    public BookByCategoryAdapter(Context context, List<Book> list) {
        this.mContext = context;
        this.mBooks = list;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_book_by_category, parent, false);
        final BookViewHolder holder = new BookViewHolder(itemView);
        itemView.setOnClickListener(view -> onBookClick(mBooks.get(holder.getAdapterPosition())));
        return holder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = mBooks.get(position);
        holder.tvBookName.setText(book.getTitle());
        Utils.loadImageFromCache(holder.ivBook, Constant.STORY + "/" + Constant.CATEGORY, book.getId().toString(), Constant.COVER);
    }

    @Override
    public int getItemCount() {
        return mBooks == null ? 0 : mBooks.size();
    }

    private void onBookClick(final Book book) {
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        Bundle data = new Bundle();
        data.putParcelable(Constant.KEY_BOOK, book);
        intent.putExtra(Constant.KEY_DATA, data);
        ((BaseActivity)mContext).startActivityForResult(intent, 100);
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBook;
        TextView tvBookName;

        BookViewHolder(View itemView) {
            super(itemView);
            ivBook = (ImageView) itemView.findViewById(R.id.iv_book);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
        }
    }
}
