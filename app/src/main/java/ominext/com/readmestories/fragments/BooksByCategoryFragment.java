package ominext.com.readmestories.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.adapters.BookByCategoryAdapter;
import ominext.com.readmestories.adapters.SimpleDividerItemDecoration;
import ominext.com.readmestories.models.Book;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class BooksByCategoryFragment extends BaseFragment {

    private BookByCategoryAdapter mBookAdapter;
    private List<Book> mBookList;
    RecyclerView rvMyBooks;

    public BooksByCategoryFragment() {
        // Required empty public constructor
    }

    public static BooksByCategoryFragment newInstance(List<Book> books) {
        BooksByCategoryFragment fragment = new BooksByCategoryFragment();
        fragment.mBookList = books;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_books, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMyBooks = (RecyclerView) view.findViewById(R.id.rv_my_books);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, VERTICAL, false);
        rvMyBooks.setLayoutManager(gridLayoutManager);
        rvMyBooks.setHasFixedSize(true);
        rvMyBooks.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mBookAdapter = new BookByCategoryAdapter(getContext(), mBookList);
        rvMyBooks.setAdapter(mBookAdapter);
    }
}
