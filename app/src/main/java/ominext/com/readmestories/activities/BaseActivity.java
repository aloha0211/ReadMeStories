package ominext.com.readmestories.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ominext.com.readmestories.R;
import ominext.com.readmestories.utils.DialogUtils;
import ominext.com.readmestories.utils.ProgressDialogUtils;

/**
 * Created by LuongHH on 6/21/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fr_container, fragment);
        ft.commit();
    }

    public void showProgressDialog(String message) {
        mDialog = ProgressDialogUtils.create(this, message);
        mDialog.show();
    }

    public void dismissProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void showAlertDialog(String title, String content) {
        DialogUtils.showAlertDialog(this, title, content);
    }

    public void showAlertDialog(String title, String content, @ColorRes int resColor) {
        DialogUtils.showAlertDialog(this, title, content, resColor);
    }

    public void showConfirmationDialog(String title, String content,
                                       final View.OnClickListener onClickListener) {
        DialogUtils.showConfirmationDialog(this, title, content, getString(R.string.ok), getString(R.string.cancel), onClickListener);
    }
}
