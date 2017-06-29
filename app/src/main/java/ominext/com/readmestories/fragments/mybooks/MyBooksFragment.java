package ominext.com.readmestories.fragments.mybooks;

import android.content.Context;
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
import ominext.com.readmestories.adapters.BooksAdapter;
import ominext.com.readmestories.adapters.SimpleDividerItemDecoration;
import ominext.com.readmestories.fragments.BaseFragment;
import ominext.com.readmestories.models.Book;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class MyBooksFragment extends BaseFragment implements MyBooksView {

    private RecyclerView mRecyclerView;
    private BooksAdapter mBookAdapter;
    private List<Book> mBookList;

    private MyBooksPresenter mPresenter;

    public MyBooksFragment() {
        // Required empty public constructor
    }

    public static MyBooksFragment newInstance() {
        return new MyBooksFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new MyBooksPresenter(context, this);
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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_my_books);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mBookList = new ArrayList<>();
        mBookAdapter = new BooksAdapter(getContext(), mBookList);
        mRecyclerView.setAdapter(mBookAdapter);

        mPresenter.getListBook();
    }

    @Override
    public void onSuccessful(List<Book> bookList) {
        mBookList.addAll(bookList);
        mBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed() {

    }
}
