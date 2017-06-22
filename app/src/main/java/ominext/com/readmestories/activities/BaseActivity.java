package ominext.com.readmestories.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ominext.com.readmestories.R;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class BaseActivity extends AppCompatActivity {

    protected void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fr_container, fragment);
        ft.commit();
    }
}
