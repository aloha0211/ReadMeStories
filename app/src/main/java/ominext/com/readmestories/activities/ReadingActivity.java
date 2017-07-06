package ominext.com.readmestories.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ominext.com.readmestories.R;
import ominext.com.readmestories.listeners.OnPageChangeListener;
import ominext.com.readmestories.media.Player;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.view.BookPageProvider;
import ominext.com.readmestories.view.CurlView;

/**
 * Created by LuongHH on 7/5/2017.
 */

public class ReadingActivity extends AppCompatActivity implements OnPageChangeListener {

    private CurlView mCurlView;
    private Player mPlayer;
    private BookPageProvider mPageProvider;
    private Book mBook;

    private int mLastPageIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        mBook = getIntent().getBundleExtra(Constant.KEY_DATA).getParcelable(Constant.KEY_BOOK);
        mCurlView = (CurlView) findViewById(R.id.curl_view);
        mPageProvider = new BookPageProvider(this, mBook);

        int index = 0;
        if (getLastNonConfigurationInstance() != null) {
            index = (Integer) getLastNonConfigurationInstance();
        }

        mCurlView.setOnPageChangeListener(this);
        mCurlView.setPageProvider(mPageProvider);
        mCurlView.setViewMode(CurlView.SHOW_TWO_PAGES);
        mCurlView.setCurrentIndex(index);
        mCurlView.setBackgroundColor(0xFF202830);

        // This is something somewhat experimental. Before uncommenting next
        // line, please see method comments in CurlView.
        // mCurlView.setEnableTouchPressure(true);

        mPlayer = new Player();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCurlView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurlView.onResume();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mCurlView.getCurrentIndex();
    }

    @Override
    public void onPageSelected(int position) {
        mPageProvider.getCurrentReading(mLastPageIndex).stopReading();
        mPageProvider.getCurrentReading(position).startReading();
        mLastPageIndex = position;
    }

    public void onCompletionReadingPage(MediaPlayer mediaPlayer, String fileName) {
        mediaPlayer.setOnCompletionListener(null);
        if (mediaPlayer.getDuration() != 0) {
            if (fileName.equalsIgnoreCase(Constant.COVER)) {
//                mViewPager.setCurrentItem(1, true);
            } else if (!fileName.equalsIgnoreCase(Constant.BACK_COVER)) {
                try {
                    int position = Integer.parseInt(fileName) + 1;
//                    mViewPager.setCurrentItem(position, true);
                } catch (Exception e) {

                }
            }
        }
    }
}
