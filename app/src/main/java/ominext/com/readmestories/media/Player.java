package ominext.com.readmestories.media;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by LuongHH on 6/22/2017.
 */

public class Player {

    private static final long DELAY_TIME = 1000;

    private Context mContext;
    private MediaPlayer mMediaPlayer;

    private Handler mHandler = new Handler();
    private Runnable mSpanTextRunnable;
    private Runnable mPlayMediaRunnable;

    private int mDuration;
    private int mTimeIndex;
    private int mTextSpanFromIndex;
    private boolean mIsFirstRun;

    public Player(Context context) {
        this.mContext = context;
    }

    public void readBook(@NonNull final TextView textView, @NonNull final String content, @NonNull String audioPath, final List<Double> timeFrame, MediaPlayer.OnCompletionListener listener) {

        final String[] contents = content.trim().replaceAll("-", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" \" ", " ").split(" ");

        mTimeIndex = 0;
        mTextSpanFromIndex = 0;
        mIsFirstRun = true;
        mSpanTextRunnable = new Runnable() {
            @Override
            public void run() {
                if (mTimeIndex < timeFrame.size() && mTimeIndex < contents.length) {
                    String textToSpan = contents[mTimeIndex];
                    textToSpan = textToSpan.replaceAll("\"", "").replaceAll(",", "").replaceAll("\\.", "").replaceAll("\\?", "").replace("!", "");
                    int startIndex = content.indexOf(textToSpan, mTextSpanFromIndex /*filter text from Index*/);
                    int endIndex = startIndex + textToSpan.length();
                    spanTextView(textView, content, startIndex, endIndex);

                    if (mIsFirstRun) {
                        mMediaPlayer.start();
                        mIsFirstRun = false;
                    }

                    int period;
                    if (mTimeIndex + 1 == timeFrame.size()) {
                        period = mDuration > timeFrame.get(mTimeIndex) * 1000 ? mDuration - (int) (timeFrame.get(mTimeIndex) * 1000) : 0;
                    } else {
                        period = (int) ((timeFrame.get(mTimeIndex + 1) - timeFrame.get(mTimeIndex)) * 1000);
                    }
                    Log.e("read", textToSpan + " - " + period);
                    mHandler.postDelayed(this, period);
                    mTimeIndex++;
                    mTextSpanFromIndex = endIndex;        // set fromIndex
                }
            }
        };

        mHandler.postDelayed(mSpanTextRunnable, DELAY_TIME + (long) (timeFrame.get(0) * 1000));

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(audioPath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnCompletionListener(listener);
            mDuration = mMediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readBook(@NonNull String audioPath, MediaPlayer.OnCompletionListener listener) {

        mPlayMediaRunnable = new Runnable() {
            @Override
            public void run() {
                mMediaPlayer.start();
            }
        };
        mHandler.postDelayed(mPlayMediaRunnable, DELAY_TIME);

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(audioPath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnCompletionListener(listener);
            mDuration = mMediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void spanTextView(@NonNull TextView textView, @NonNull String content, int startIndex, int endIndex) {
        SpannableString textSpan = new SpannableString(content);
        textSpan.setSpan(new BackgroundColorSpan(Color.RED), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(textSpan);
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mHandler.removeCallbacks(mSpanTextRunnable);
        mHandler.removeCallbacks(mPlayMediaRunnable);
    }

    public void pausePlaying() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
        mHandler.removeCallbacks(mSpanTextRunnable);
        mHandler.removeCallbacks(mPlayMediaRunnable);
    }

    public void stopPlaying() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.stop();
        }
        mHandler.removeCallbacks(mSpanTextRunnable);
        mHandler.removeCallbacks(mPlayMediaRunnable);
    }
}
