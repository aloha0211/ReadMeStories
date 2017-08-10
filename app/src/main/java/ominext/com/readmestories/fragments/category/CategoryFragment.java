package ominext.com.readmestories.fragments.category;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.BaseActivity;
import ominext.com.readmestories.adapters.CategoryAdapter;
import ominext.com.readmestories.adapters.SimpleDividerItemDecoration;
import ominext.com.readmestories.fragments.BaseFragment;
import ominext.com.readmestories.models.Category;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class CategoryFragment extends BaseFragment implements CategoryView {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout llNoInternetConnection;

    private CategoryAdapter mCategoryAdapter;
    private List<Category> mCategories;

    private CategoryPresenter mPresenter;

    private int mUriPos;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new CategoryPresenter(context, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_my_books);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mCategories = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getContext(), mCategories);
        recyclerView.setAdapter(mCategoryAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                llNoInternetConnection.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mPresenter.getCategories();
            }
        });

        llNoInternetConnection = (LinearLayout) view.findViewById(R.id.ll_no_internet_connection);
        view.findViewById(R.id.btn_try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llNoInternetConnection.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                ((BaseActivity) getActivity()).showProgressDialog("");
                mPresenter.getCategories();
            }
        });

        ((BaseActivity) getActivity()).showProgressDialog("");
        mPresenter.getCategories();
    }

    @Override
    public void onDestroy() {
        Utils.deleteCacheDir(getContext(), Constant.STORY);
        super.onDestroy();
    }

    @Override
    public void onSuccessful(List<Category> categories) {
        if (isAdded()) {
            mCategories.clear();
            mCategories.addAll(categories);
            mUriPos = 0;
            mPresenter.getImageUri(mCategories.get(0).getLargeTout());
        }
    }

    @Override
    public void onLoadImageUriSuccessful(Uri uri) {
        if (isAdded()) {
            mCategories.get(mUriPos).setUri(uri);
            mUriPos++;
            if (mUriPos < mCategories.size())
                mPresenter.getImageUri(mCategories.get(mUriPos).getLargeTout());
            else {
                mSwipeRefreshLayout.setRefreshing(false);
                ((BaseActivity) getActivity()).dismissProgressDialog();
                mCategoryAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onFailed(String message) {
        if (isAdded()) {
            mSwipeRefreshLayout.setRefreshing(false);
            ((BaseActivity) getActivity()).dismissProgressDialog();
            if (message.equalsIgnoreCase(getString(R.string.no_connection_message))) {
                llNoInternetConnection.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            } else {
                ((BaseActivity) getActivity()).showAlertDialog(getString(R.string.error), message);
            }
        }
    }
}
