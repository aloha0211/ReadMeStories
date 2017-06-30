package ominext.com.readmestories.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import ominext.com.readmestories.R;
import ominext.com.readmestories.adapters.ReadingBookPagerAdapter;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;

public class ReadingBookActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ReadingBookPagerAdapter mPagerAdapter;

    private int mLastPageIndex = 0;

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
                mPagerAdapter.getFragment(0).startReading();
            }
        });
        mViewPager.addOnPageChangeListener(this);
    }

    public void onCompletionReadingPage(String fileName) {
        if (fileName.equalsIgnoreCase(Constant.COVER)) {
            mViewPager.setCurrentItem(1, true);
        } else if (!fileName.equalsIgnoreCase(Constant.BACK_COVER)) {
            try {
                int position = Integer.parseInt(fileName) + 1;
                mViewPager.setCurrentItem(position, true);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
        mPagerAdapter.getFragment(mLastPageIndex).stopReading();
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mPagerAdapter.getFragment(position).startReading();
            }
        });
        mLastPageIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
