package ominext.com.readmestories.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ominext.com.readmestories.utils.ProgressDialogUtils;

/**
 * Created by LuongHH on 6/22/2017.
 */

public abstract class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = ProgressDialogUtils.create(getContext());
    }

    public void showProgressDialog() {
        mProgressDialog.show();
    }

    public void dissmissProgressDialog() {
        mProgressDialog.dismiss();
    }
}
