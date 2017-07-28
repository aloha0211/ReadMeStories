package ominext.com.readmestories.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.ReadingBookActivity;
import ominext.com.readmestories.listeners.OnStartedListener;
import ominext.com.readmestories.media.Player;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

public class ReadingBookFragment extends BaseFragment {

    private TextView mTvContent;
    private ImageView mIvContent;
    private ImageView mPlayButton;

    private Player mPlayer;

    private int mBookId;
    private String mContent;
    private String mFileName;

    private boolean isClickable = true;
    private boolean isStatePausing;

    private List<Double> mTimeFrame;
    private View.OnClickListener mListener;

    public ReadingBookFragment() {
        // Required empty public constructor
    }

    public static ReadingBookFragment newInstance(int bookId, String content, String fileName, List<Double> timeFrame, View.OnClickListener listener) {
        ReadingBookFragment fragment = new ReadingBookFragment();
        fragment.mContent = content;
        fragment.mTimeFrame = timeFrame;
        fragment.mBookId = bookId;
        fragment.mFileName = fileName;
        fragment.mListener = listener;
        return fragment;
    }

    public static ReadingBookFragment newInstance(int bookId, String fileName, View.OnClickListener listener) {
        ReadingBookFragment fragment = new ReadingBookFragment();
        fragment.mBookId = bookId;
        fragment.mFileName = fileName;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_book, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mIvContent = (ImageView) view.findViewById(R.id.iv_content);
        mPlayButton = (ImageView) view.findViewById(R.id.iv_play);

        mPlayer = new Player();

        Utils.loadImage(mIvContent, String.valueOf(mBookId), mFileName);

        if (mContent != null) {
            mContent = mContent.replaceAll("&quot;", "\"").replaceAll("&#39;", "\'");
            mTvContent.setText(mContent);
        }

        view.setOnClickListener(mListener);
    }

    public void onPlayClick() {
        if (isClickable) {
            isClickable = false;
            mPlayButton.setVisibility(View.VISIBLE);
            if (mPlayer.isPlaying()) {
                mPlayButton.setImageResource(R.drawable.play_circle);
                pauseReading();
                isStatePausing = true;
            } else {
                mPlayButton.setImageResource(R.drawable.pause_circle);
                resumeReading();
                isStatePausing = false;
            }
            final Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            final Animation fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
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
                    isClickable = true;
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

    public void startReading(final OnStartedListener onStartedListener) {
        String audioPath = getContext().getCacheDir().getPath() + "/" + mBookId + "/" + Constant.AUDIO + "/" + mFileName + Constant.MP3_EXTENSION;
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isAdded()) {
                    ((ReadingBookActivity) getActivity()).onCompletionReadingPage(mediaPlayer, mFileName);
                }
            }
        };
        if (mContent == null) {
            mPlayer.readBook(audioPath, onCompletionListener, onStartedListener);
        } else {
            mPlayer.readBook(mTvContent, mContent, audioPath, mTimeFrame, onCompletionListener, onStartedListener);
        }
    }

    public void pauseReading() {
        mPlayer.pause();
    }

    public void resumeReading() {
        if (!mPlayer.resume()) {
            startReading(null);
        }
    }

    public void stopReading() {
        mTvContent.setText(mContent);
        mPlayer.stop();
    }

    public boolean isStatePausing() {
        return isStatePausing;
    }

    public void release() {
        mPlayer.release();
    }
}
