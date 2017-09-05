package ominext.com.readmestories.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.adapters.BookAdapter;
import ominext.com.readmestories.adapters.SimpleDividerItemDecoration;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class MyBooksFragment extends BaseFragment {

    private BookAdapter mBookAdapter;
    private List<Book> mBookList;

    public MyBooksFragment() {
        // Required empty public constructor
    }

    public static MyBooksFragment newInstance() {
        return new MyBooksFragment();
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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_my_books);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mBookList = new ArrayList<>();
        mBookAdapter = new BookAdapter(this, mBookList);
        recyclerView.setAdapter(mBookAdapter);

        getMyBooks();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getMyBooks();
    }

    private void getMyBooks() {
        mBookList.clear();
        mBookList.addAll(Utils.getBooksFromAssets(getContext()));
        mBookList.addAll(Utils.getBooksFromRealm(getActivity()));
        mBookAdapter.notifyDataSetChanged();
    }
}
