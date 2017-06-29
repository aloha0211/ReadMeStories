package ominext.com.readmestories.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import ominext.com.readmestories.R;
import ominext.com.readmestories.adapters.ReadingBookPagerAdapter;
import ominext.com.readmestories.fragments.ReadingBookFragment;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;

public class ReadingBookActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ReadingBookPagerAdapter mPagerAdapter;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_book);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        Book book = getIntent().getBundleExtra(Constant.KEY_DATA).getParcelable(Constant.KEY_BOOK);
        mPagerAdapter = new ReadingBookPagerAdapter(getSupportFragmentManager(), book);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mPagerAdapter.getFragment(0).readBook();
            }
        });
    }

    public void onCompletionReadingPage(String fileName) {
        if (fileName.equalsIgnoreCase(Constant.COVER)) {
            mViewPager.setCurrentItem(1, true);
            mPagerAdapter.getFragment(1).readBook();
        } else if (!fileName.equalsIgnoreCase(Constant.BACK_COVER)) {
            try {
                int position = Integer.parseInt(fileName) + 1;
                mViewPager.setCurrentItem(position, true);
                mPagerAdapter.getFragment(position).readBook();
            } catch (Exception e) {

            }
        }
    }
}
