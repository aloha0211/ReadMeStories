package ominext.com.readmestories.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.ReadingBookActivity;
import ominext.com.readmestories.media.Player;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

public class ReadingBookFragment extends BaseFragment {

    private TextView mTvContent;
    private ImageView mIvContent;

    private Player mPlayer;

    private int mBookId;
    private String mContent;
    private String mFileName;
    private List<Double> mTimeFrame;

    public ReadingBookFragment() {
        // Required empty public constructor
    }

    public static ReadingBookFragment newInstance(int bookId, String content, String fileName, List<Double> timeFrame) {
        ReadingBookFragment fragment = new ReadingBookFragment();
        fragment.mContent = content;
        fragment.mTimeFrame = timeFrame;
        fragment.mBookId = bookId;
        fragment.mFileName = fileName;
        return fragment;
    }

    public static ReadingBookFragment newInstance(int bookId, String fileName) {
        ReadingBookFragment fragment = new ReadingBookFragment();
        fragment.mBookId = bookId;
        fragment.mFileName = fileName;
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

        Utils.loadImage(mIvContent, String.valueOf(mBookId), mFileName);

        if (mContent!= null) {
            mContent = mContent.replaceAll("&quot;", "\"").replaceAll("&#39;", "\'");
            mTvContent.setText(mContent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPlayer = new Player(context);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mPlayer.release();
        super.onDestroy();
    }

    public void startReading() {
        String audioPath = getContext().getCacheDir().getPath() + "/" + mBookId + "/" + Constant.AUDIO + "/" + mFileName + Constant.MP3_EXTENSION;
        MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isAdded()) {
                    ((ReadingBookActivity) getActivity()).onCompletionReadingPage(mFileName);
                }
            }
        };
        if (mContent == null) {
            mPlayer.readBook(audioPath, listener);
        } else {
            mPlayer.readBook(mTvContent, mContent, audioPath, mTimeFrame, listener);
        }
    }

    public void stopReading() {
        mTvContent.setText(mContent);
        mPlayer.stopPlaying();
    }
}
