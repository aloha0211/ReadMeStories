package ominext.com.readmestories.fragments;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
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
    private int mPageIndex = -1;
    private String mContent;
    private String mFileName;

    private boolean isClickable = true;
    private boolean isOnStatePausing;
    private boolean hasPageJustSelected;

    private List<Double> mTimeFrame;
    private View.OnClickListener mListener;

    private boolean isAutoReadNextPage = true;
    private int readingMode;

    public ReadingBookFragment() {
        // Required empty public constructor
    }

    public static ReadingBookFragment newInstance(int bookId, String content, String fileName, List<Double> timeFrame, View.OnClickListener listener, int readingMode) {
        ReadingBookFragment fragment = new ReadingBookFragment();
        fragment.mContent = content;
        fragment.mTimeFrame = timeFrame;
        fragment.mBookId = bookId;
        fragment.mFileName = fileName;
        fragment.mListener = listener;
        fragment.readingMode = readingMode;
        return fragment;
    }

    public static ReadingBookFragment newInstance(int pageIndex, int bookId, String fileName, View.OnClickListener listener, int readingMode) {
        ReadingBookFragment fragment = new ReadingBookFragment();
        fragment.mBookId = bookId;
        fragment.mPageIndex = pageIndex;
        fragment.mFileName = fileName;
        fragment.mListener = listener;
        fragment.readingMode = readingMode;
        return fragment;
    }

    public void setAutoReadNextPage(boolean autoReadNextPage) {
        isAutoReadNextPage = autoReadNextPage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int resourceId = mPageIndex > 0 ? R.layout.fragment_reading_book_reverse_page : R.layout.fragment_reading_book;  // mPageIndex > 0 <=> last page
        return inflater.inflate(resourceId, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mIvContent = (ImageView) view.findViewById(R.id.iv_content);
        mPlayButton = (ImageView) view.findViewById(R.id.iv_play);

        mPlayer = new Player();

        if (readingMode == Constant.MODE_FROM_ASSETS) {
            Utils.loadImageFromAssets(mIvContent, String.valueOf(mBookId), mFileName);
        } else if (readingMode == Constant.MODE_FROM_INTERNAL_STORAGE) {
            Utils.loadImageFromInternalStorage(mIvContent, String.valueOf(mBookId), mFileName);
        } else if (readingMode == Constant.MODE_FROM_CACHE) {
            Utils.loadImageFromCache(mIvContent, Constant.STORY + "/" + Constant.TEMP, String.valueOf(mBookId), mFileName);
        }

        if (mContent != null) {
            mContent = mContent.replaceAll("&quot;", "\"").replaceAll("&#39;", "\'");
            mTvContent.setText(mContent);
        }

        view.setOnClickListener(mListener);

        if (mPageIndex >= 0) {
            ImageView ivRead = (ImageView) view.findViewById(R.id.iv_read);
            RelativeLayout rlRead = (RelativeLayout) view.findViewById(R.id.rl_read);
            view.findViewById(R.id.tv_content).setVisibility(View.GONE);
            rlRead.setVisibility(View.VISIBLE);
            if (mPageIndex == 0) {              // first page or cover page
                ivRead.setImageResource(R.drawable.bg_read_it_myselft);
                ivRead.setTag(Constant.COVER);
            } else if (mPageIndex > 0) {        // last page
                ivRead.setImageResource(R.drawable.bg_read_it_again);
                ivRead.setTag(Constant.BACK_COVER);
            }
            ivRead.setOnClickListener(mListener);
        }
    }

    public void onPlayClick() {
        if (isClickable && isAutoReadNextPage) {
            isClickable = false;
            mPlayButton.setVisibility(View.VISIBLE);
            if (!isOnStatePausing) {
                mPlayButton.setImageResource(R.drawable.play_circle);
                pauseReading();
                isOnStatePausing = true;
            } else {
                mPlayButton.setImageResource(R.drawable.pause_circle);
                if (hasPageJustSelected) {
                    startReading(null);
                } else {
                    resumeReading();
                }
                isOnStatePausing = false;
            }
            hasPageJustSelected = false;
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
        if (isAutoReadNextPage) {
            MediaPlayer.OnCompletionListener onCompletionListener = mediaPlayer -> {
                if (isAdded()) {
                    ((ReadingBookActivity) getActivity()).onCompletionReadingPage(mediaPlayer, mFileName);
                }
            };
            String audioPath;
            if (readingMode == Constant.MODE_FROM_ASSETS) {
                try {
                    AssetFileDescriptor descriptor = getContext().getAssets().openFd(mBookId + "/" + Constant.AUDIO + "/" + mFileName + Constant.MP3_EXTENSION);
                    if (mContent == null) {
                        mPlayer.readBook(descriptor, onCompletionListener, onStartedListener);
                    } else {
                        mPlayer.readBook(mTvContent, mContent, descriptor, mTimeFrame, onCompletionListener, onStartedListener);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (readingMode == Constant.MODE_FROM_INTERNAL_STORAGE) {
                audioPath = getContext().getFilesDir().getPath() + "/" + Constant.STORY + "/" + Constant.SAVE + "/" + mBookId + "/" + Constant.AUDIO + "/" + mFileName + Constant.MP3_EXTENSION;
                if (mContent == null) {
                    mPlayer.readBook(audioPath, onCompletionListener, onStartedListener);
                } else {
                    mPlayer.readBook(mTvContent, mContent, audioPath, mTimeFrame, onCompletionListener, onStartedListener);
                }
            } else if (readingMode == Constant.MODE_FROM_CACHE) {
                audioPath = getContext().getCacheDir().getPath() + "/" + Constant.STORY + "/" + Constant.TEMP + "/" + mBookId + "/" + Constant.AUDIO + "/" + mFileName + Constant.MP3_EXTENSION;
                if (mContent == null) {
                    mPlayer.readBook(audioPath, onCompletionListener, onStartedListener);
                } else {
                    mPlayer.readBook(mTvContent, mContent, audioPath, mTimeFrame, onCompletionListener, onStartedListener);
                }
            }
        }
    }

    public void pauseReading() {
        if (isAutoReadNextPage)
            mPlayer.pause();
    }

    public void resumeReading() {
        if (isAutoReadNextPage && !mPlayer.resume()) {
            startReading(null);
        }
    }

    public void stopReading() {
        if (isAutoReadNextPage) {
            mTvContent.setText(mContent);
            mPlayer.stop();
        }
    }

    public boolean isOnStatePausing() {
        return isOnStatePausing;
    }

    public void setOnStatePausing(boolean onStatePausing) {
        isOnStatePausing = onStatePausing;
    }

    public void setHasPageJustSelected(boolean hasPageJustSelected) {
        this.hasPageJustSelected = hasPageJustSelected;
    }

    public void release() {
        if (isAutoReadNextPage)
            mPlayer.release();
    }
}
