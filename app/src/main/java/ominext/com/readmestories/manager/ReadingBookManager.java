package ominext.com.readmestories.manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.TextView;

import java.util.List;

import ominext.com.readmestories.activities.ReadingActivity;
import ominext.com.readmestories.media.Player;
import ominext.com.readmestories.utils.Constant;

public class ReadingBookManager {

    private TextView mTvContent;

    private Context mContext;
    private Player mPlayer = new Player();

    private int mBookId;
    private String mContent;
    private String mFileName;
    private List<Double> mTimeFrame;

    public ReadingBookManager(Context context, int bookId, String content, String fileName, List<Double> timeFrame) {
        mContext = context;
        mContent = content;
        mTimeFrame = timeFrame;
        mBookId = bookId;
        mFileName = fileName;
    }

    public ReadingBookManager(Context context, int bookId, String fileName) {
        mContext = context;
        mBookId = bookId;
        mFileName = fileName;
    }

    public void setTextView(TextView tvContent) {
        mTvContent = tvContent;
    }

    public void startReading() {
        String audioPath = mContext.getCacheDir().getPath() + "/" + mBookId + "/" + Constant.AUDIO + "/" + mFileName + Constant.MP3_EXTENSION;
        MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                ((ReadingActivity) mContext).onCompletionReadingPage(mediaPlayer, mFileName);
            }
        };
        if (mContent == null) {
            mPlayer.readBook(audioPath, listener);
        } else {
            mPlayer.readBook(mTvContent, mContent, audioPath, mTimeFrame, listener);
        }
    }

    public void pauseReading() {
        mPlayer.pause();
    }

    public void resumeReading() {
        mPlayer.resume();
    }

    public void stopReading() {
        if (mTvContent != null)
            mTvContent.setText(mContent);
        mPlayer.stop();
    }

    public boolean isReading() {
        return mPlayer.isPlaying();
    }

    public void release() {
        mPlayer.release();
    }
}
