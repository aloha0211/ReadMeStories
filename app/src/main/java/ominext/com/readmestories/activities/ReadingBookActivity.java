package ominext.com.readmestories.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import ominext.com.readmestories.R;
import ominext.com.readmestories.adapters.ReadingBookPagerAdapter;
import ominext.com.readmestories.fragments.ReadingBookFragment;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;

public class ReadingBookActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mViewPager;
    private ReadingBookPagerAdapter mPagerAdapter;

    private ImageView mPlayButton;

    private int mLastPageIndex = 0;
    boolean isEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_book);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPlayButton = (ImageView) findViewById(R.id.iv_play);

        Book book = getIntent().getBundleExtra(Constant.KEY_DATA).getParcelable(Constant.KEY_BOOK);
        mPagerAdapter = new ReadingBookPagerAdapter(getSupportFragmentManager(), book, this);
        mViewPager.setAdapter(mPagerAdapter);
        findViewById(R.id.ll_root_view).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.addOnPageChangeListener(this);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                ReadingBookFragment fragment = mPagerAdapter.getFragment(mViewPager.getCurrentItem());
                if (fragment.isReading()) {
                    fragment.resumeReading();
                } else {
                    fragment.startReading();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewPager.clearOnPageChangeListeners();
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mPagerAdapter.getFragment(mViewPager.getCurrentItem()).pauseReading();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPagerAdapter.getFragment(mViewPager.getCurrentItem()).release();
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

    @Override
    public void onClick(View view) {
        if (isEnabled) {
            isEnabled = false;
            ReadingBookFragment fragment = mPagerAdapter.getFragment(mViewPager.getCurrentItem());
            mPlayButton.setVisibility(View.VISIBLE);
            if (fragment.isReading()) {
                mPlayButton.setImageResource(R.drawable.play_circle);
                fragment.pauseReading();
            } else {
                mPlayButton.setImageResource(R.drawable.pause_circle);
                fragment.resumeReading();
            }
            final Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            final Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            Animation.AnimationListener fadeInAnimationListener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPlayButton.startAnimation(fadeOutAnimation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            };
            Animation.AnimationListener fadeOutAnimationListener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPlayButton.setVisibility(View.GONE);
                    isEnabled = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            };
            fadeInAnimation.setAnimationListener(fadeInAnimationListener);
            fadeOutAnimation.setAnimationListener(fadeOutAnimationListener);
            mPlayButton.startAnimation(fadeInAnimation);
        }
    }
}
