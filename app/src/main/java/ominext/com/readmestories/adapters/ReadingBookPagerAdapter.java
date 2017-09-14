package ominext.com.readmestories.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;

import ominext.com.readmestories.fragments.ReadingBookFragment;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;

/**
 * Created by LuongHH on 6/27/2017.
 */

public class ReadingBookPagerAdapter extends FragmentStatePagerAdapter {

    private Book mBook;
    private SparseArrayCompat<ReadingBookFragment> mSparseArray = new SparseArrayCompat<>();
    private View.OnClickListener mListener;

    private boolean isAutoReadNextPage;
    private int readingMode;

    public ReadingBookPagerAdapter(FragmentManager fm, Book book, View.OnClickListener listener, boolean isAutoReadNextPage) {
        super(fm);
        this.mBook = book;
        this.mListener = listener;
        this.isAutoReadNextPage = isAutoReadNextPage;
        this.readingMode = book.getReadingMode();
    }

    @Override
    public Fragment getItem(int position) {
        ReadingBookFragment fragment;
        if (position == 0 || position == getCount() - 1) {
            fragment = ReadingBookFragment.newInstance(position, mBook.getId(), getFileName(position), mListener, readingMode);
        } else {
            fragment = ReadingBookFragment.newInstance(mBook.getId(), mBook.getContent().get(position - 1), getFileName(position), mBook.gettime_frame().get(position - 1), mListener, readingMode);
        }
        fragment.setAutoReadNextPage(isAutoReadNextPage);
        return fragment;
    }

    @Override
    public int getCount() {
        return mBook.getContent().size() + 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ReadingBookFragment fragment = (ReadingBookFragment) super.instantiateItem(container, position);
        mSparseArray.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mSparseArray.remove(position);
    }

    public ReadingBookFragment getFragment(int position) {
        return mSparseArray.get(position);
    }

    private String getFileName(int position) {
        String fileName;
        if (position == 0) {
            fileName = Constant.COVER;
        } else if (position == getCount() - 1) {
            fileName = Constant.BACK_COVER;
        } else {
            fileName = String.valueOf(position);
        }
        return fileName;
    }
}
