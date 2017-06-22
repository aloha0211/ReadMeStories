package ominext.com.readmestories.activities;

import android.os.Bundle;

import ominext.com.readmestories.R;
import ominext.com.readmestories.fragments.ReadingBookFragment;

public class ReadingBookActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_book);
        replaceFragment(ReadingBookFragment.newInstance());
    }
}
