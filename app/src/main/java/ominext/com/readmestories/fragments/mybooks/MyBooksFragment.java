package ominext.com.readmestories.fragments.mybooks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.adapters.BookAdapter;
import ominext.com.readmestories.adapters.SimpleDividerItemDecoration;
import ominext.com.readmestories.fragments.BaseFragment;
import ominext.com.readmestories.models.Book;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class MyBooksFragment extends BaseFragment implements MyBooksView {

    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;

    private MyBooksPresenter mPresenter;

    public MyBooksFragment() {
        // Required empty public constructor
    }

    public static MyBooksFragment newInstance() {
        MyBooksFragment fragment = new MyBooksFragment();
        return fragment;
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

        mPresenter.getListBook();
    }

    @Override
    public void onSuccessful(List<Book> bookList) {
        mBookAdapter = new BookAdapter(getContext(), bookList);
        mRecyclerView.setAdapter(mBookAdapter);
    }

    @Override
    public void onFailed() {

    }
}
