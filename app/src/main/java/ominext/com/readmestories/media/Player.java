package ominext.com.readmestories.media;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by LuongHH on 6/22/2017.
 */

public class Player {

    private static final long DELAY_TIME = 1500;

    private Context mContext;
    private MediaPlayer mMediaPlayer;

    private Handler mHandler = new Handler();
    private Runnable mSpanTextRunnable;
    private Runnable mPlayMediaRunnable;

    private int mDuration;

    public Player(Context context) {
        this.mContext = context;
    }

    public void readBook(@NonNull final TextView textView, @NonNull final String content, @RawRes int audio, final double[] timeFrame) {

//        final String content = contentText.replaceAll("\"", "").replaceAll("&quot;", "").replaceAll(",", "").replaceAll("\\.", "").replaceAll("\\?", "");
        final String[] contents = content.split(" ");

        final int[] index = {0, 0};
        mSpanTextRunnable = new Runnable() {
            @Override
            public void run() {
                if (index[0] < timeFrame.length) {
                    String textToSpan = contents[index[0]];
                    textToSpan = textToSpan.replaceAll("\"", "").replaceAll(",", "").replaceAll("\\.", "").replaceAll("\\?", "");
                    int startIndex = content.indexOf(textToSpan, index[1] /*filter text from Index*/);
                    int endIndex = startIndex + textToSpan.length();
                    spanTextView(textView, content, startIndex, endIndex);

                    int period;
                    if (index[0] + 1 == timeFrame.length) {
                        period = mDuration > timeFrame[index[0]] * 1000 ? mDuration - (int) (timeFrame[index[0]] * 1000) : 0;
                    } else {
                        period = (int) ((timeFrame[index[0] + 1] - timeFrame[index[0]]) * 1000);
                    }
                    Log.e("read", textToSpan + " - " + period);
                    mHandler.postDelayed(this, period);
                    index[0]++;
                    index[1] = endIndex;        // set fromIndex
                }
            }
        };
        mPlayMediaRunnable = new Runnable() {
            @Override
            public void run() {
                mMediaPlayer.start();
            }
        };

        mHandler.postDelayed(mSpanTextRunnable, DELAY_TIME + (long) (timeFrame[0] * 1000));
        mHandler.postDelayed(mPlayMediaRunnable, DELAY_TIME - 100 + (long) (timeFrame[0] * 1000));

        mMediaPlayer = MediaPlayer.create(mContext, audio);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mDuration = mMediaPlayer.getDuration();
    }

    private void spanTextView(@NonNull TextView textView, @NonNull String content, int startIndex, int endIndex) {
        SpannableString textSpan = new SpannableString(content);
        textSpan.setSpan(new BackgroundColorSpan(Color.RED), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(textSpan);
    }

    public void release() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mHandler.removeCallbacks(mSpanTextRunnable);
        mHandler.removeCallbacks(mPlayMediaRunnable);
    }
}
