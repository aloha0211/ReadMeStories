package ominext.com.readmestories.fragments.library;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.BaseActivity;
import ominext.com.readmestories.adapters.LibraryAdapter;
import ominext.com.readmestories.adapters.SimpleDividerItemDecoration;
import ominext.com.readmestories.fragments.BaseFragment;
import ominext.com.readmestories.models.Book;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class LibraryFragment extends BaseFragment implements LibraryView {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout llNoInternetConnection;

    private LibraryAdapter mBookAdapter;
    private List<Book> mBookList;

    private LibraryPresenter mPresenter;

    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new LibraryPresenter(context, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
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
        mBookAdapter = new LibraryAdapter(getContext(), mBookList);
        recyclerView.setAdapter(mBookAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                llNoInternetConnection.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mPresenter.getListBook();
            }
        });

        llNoInternetConnection = (LinearLayout) view.findViewById(R.id.ll_no_internet_connection);
        view.findViewById(R.id.btn_try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llNoInternetConnection.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                ((BaseActivity) getActivity()).showProgressDialog("");
                mPresenter.getListBook();
            }
        });

        ((BaseActivity) getActivity()).showProgressDialog("");
        mPresenter.getListBook();
    }

    @Override
    public void onSuccessful(List<Book> bookList) {
        if (isAdded()) {
            mSwipeRefreshLayout.setRefreshing(false);
            ((BaseActivity) getActivity()).dissmissProgressDialog();
            mBookList.clear();
            mBookList.addAll(bookList);
            mBookAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailed(String message) {
        if (isAdded()) {
            mSwipeRefreshLayout.setRefreshing(false);
            ((BaseActivity) getActivity()).dissmissProgressDialog();
            if (message.equalsIgnoreCase(getString(R.string.no_connection_message))) {
                llNoInternetConnection.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            } else {
                ((BaseActivity) getActivity()).showAlertDialog("Error", message);
            }
        }
    }
}
