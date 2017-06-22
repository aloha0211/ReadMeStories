package ominext.com.readmestories.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ominext.com.readmestories.R;
import ominext.com.readmestories.media.Player;

public class ReadingBookFragment extends Fragment {

    private TextView mTextView;
    private Player mPlayer;
    final double[] timeFrame = {0.08, 0.15, 0.59, 0.98, 1.72, 2.18, 2.39, 2.89, 3.18, 4.50, 4.74, 4.98, 5.24, 5.52, 5.89, 6.16, 7.12, 7.33, 8.68, 8.99, 9.57, 9.87, 10.00, 10.95, 11.30, 12.09, 12.33, 12.45, 13.46, 13.75, 14.62, 14.90, 15.05, 16.48, 16.71, 17.03, 17.36, 17.50, 17.71, 18.79, 19.06, 19.16, 19.49, 19.65};

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
        mTextView = (TextView) view.findViewById(R.id.tv_book_content);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPlayer = new Player(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPlayer.readBook(mTextView, getString(R.string.content_example), R.raw._1, timeFrame);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }
}
