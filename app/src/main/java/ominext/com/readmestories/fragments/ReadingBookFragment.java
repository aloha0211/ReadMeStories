package ominext.com.readmestories.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ominext.com.readmestories.R;
import ominext.com.readmestories.media.Player;

public class ReadingBookFragment extends BaseFragment implements ReadingBookView {

    private ReadingBookPresenter mPresenter;

    private TextView mTextView;
    private Player mPlayer;
    final double[] timeFrame = {0.08, 0.15, 0.59, 0.98, 1.72, 2.18, 2.39, 2.89, 3.18, 4.50, 4.74, 4.98, 5.24, 5.52, 5.89, 6.16, 7.12, 7.33, 8.68, 8.99, 9.57, 9.87, 10.00, 10.95, 11.30, 12.09, 12.33, 12.45, 13.46, 13.75, 14.62, 14.90, 15.05, 16.48, 16.71, 17.03, 17.36, 17.50, 17.71, 18.79, 19.06, 19.16, 19.49, 19.65};
    private String example = "A little red hen found a grain of wheat. \"Who will help me plant this wheat?\" she said. \"I won\'t,\" said  the dog. \"I won\'t,\" said  the cat. \"I won\'t,\" said the pig. \"Then I will do it alone,\" said  the little red hen.";

    FirebaseAuth mAuth;

    public ReadingBookFragment() {
        // Required empty public constructor
    }

    public static ReadingBookFragment newInstance() {
        ReadingBookFragment fragment = new ReadingBookFragment();
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
        mPresenter = new ReadingBookPresenter(getContext(), this);
        mTextView = (TextView) view.findViewById(R.id.tv_book_content);
        example = example.trim().replaceAll("  ", " ");

        showProgressDialog();
        mAuth = FirebaseAuth.getInstance();
        mPresenter.download("12/audio", "1.mp3");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPlayer = new Player(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }

    @Override
    public void onDownloadSuccessful(String audioPath) {
        mPlayer.readBook(mTextView, example, audioPath, timeFrame);
        dissmissProgressDialog();
    }

    @Override
    public void onDownloadFailed() {
        dissmissProgressDialog();
    }
}
