package ominext.com.readmestories.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ominext.com.readmestories.R;
import ominext.com.readmestories.adapters.ReadingBookPagerAdapter;
import ominext.com.readmestories.fragments.ReadingBookFragment;
import ominext.com.readmestories.listeners.OnStartedListener;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

public class ReadingBookActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mViewPager;
    private ReadingBookPagerAdapter mPagerAdapter;

    private int mLastPageIndex = 0;
    private int mBookId;

    boolean isFirstTime;
    boolean isSettlingProcess;
    boolean isMediaPlayerStarted;  // for enable or disable play/pause button when media player has not started yet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_book);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        Book book = getIntent().getBundleExtra(Constant.KEY_DATA).getParcelable(Constant.KEY_BOOK);
        mBookId = book.getId();
        mPagerAdapter = new ReadingBookPagerAdapter(getSupportFragmentManager(), book, this, true, getIntent().getBooleanExtra(Constant.IS_FROM_ASSET, true));
        mViewPager.setAdapter(mPagerAdapter);
        findViewById(R.id.ll_root_view).setOnClickListener(this);

        isFirstTime = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMediaPlayerStarted = true;
        mViewPager.addOnPageChangeListener(this);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                ReadingBookFragment fragment = mPagerAdapter.getFragment(mViewPager.getCurrentItem());
                if (isFirstTime) {
                    fragment.startReading(null);
                } else {
                    if (!fragment.isOnStatePausing()) {
                        fragment.resumeReading();
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        isFirstTime = false;
        mViewPager.clearOnPageChangeListeners();
        mPagerAdapter.getFragment(mViewPager.getCurrentItem()).pauseReading();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPagerAdapter.getFragment(mViewPager.getCurrentItem()).release();
        Utils.deleteCacheDir(this, Constant.STORY + "/" + mBookId);
        super.onDestroy();
    }

    public void onCompletionReadingPage(MediaPlayer mediaPlayer, String fileName) {
        mediaPlayer.setOnCompletionListener(null);
        if (mediaPlayer.getDuration() != 0) {

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
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
        ReadingBookFragment lastPage = mPagerAdapter.getFragment(mLastPageIndex);
        boolean isOnStatePausing = false;
        if (lastPage != null) {
            lastPage.stopReading();
            isOnStatePausing = lastPage.isOnStatePausing();
        }
        final ReadingBookFragment currentPage = mPagerAdapter.getFragment(position);
        currentPage.setOnStatePausing(isOnStatePausing);
        currentPage.setHasPageJustSelected(true);
        if (!isOnStatePausing) {
            isMediaPlayerStarted = false;
            mViewPager.post(new Runnable() {
                @Override
                public void run() {
                    currentPage.startReading(new OnStartedListener() {
                        @Override
                        public void onStart() {
                            isMediaPlayerStarted = true;
                        }
                    });
                }
            });
        }
        mLastPageIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            isSettlingProcess = true;
        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            isSettlingProcess = false;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_read) {
            Book book = getIntent().getBundleExtra(Constant.KEY_DATA).getParcelable(Constant.KEY_BOOK);
            if (view.getTag().equals(Constant.COVER)) {
                // read it myself
                mPagerAdapter = new ReadingBookPagerAdapter(getSupportFragmentManager(), book, this, false, getIntent().getBooleanExtra(Constant.IS_FROM_ASSET, true));
                mViewPager.setAdapter(mPagerAdapter);
                mLastPageIndex = 0;
                mViewPager.setCurrentItem(1, true);
            } else {
                // read it again
                mPagerAdapter = new ReadingBookPagerAdapter(getSupportFragmentManager(), book, this, true, getIntent().getBooleanExtra(Constant.IS_FROM_ASSET, true));
                mViewPager.setAdapter(mPagerAdapter);
                mViewPager.setCurrentItem(0, true);
                mViewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        ReadingBookFragment fragment = mPagerAdapter.getFragment(mViewPager.getCurrentItem());
                        if (isFirstTime) {
                            fragment.startReading(null);
                        } else {
                            if (!fragment.isOnStatePausing()) {
                                fragment.resumeReading();
                            }
                        }
                    }
                });
            }
            return;
        }
        if (!isSettlingProcess && isMediaPlayerStarted) {
            ReadingBookFragment fragment = mPagerAdapter.getFragment(mViewPager.getCurrentItem());
            fragment.onPlayClick();
        }
    }
}
