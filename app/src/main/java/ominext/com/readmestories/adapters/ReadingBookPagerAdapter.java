package ominext.com.readmestories.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
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
    public ReadingBookPagerAdapter(FragmentManager fm, Book book) {
        super(fm);
        this.mBook = book;
    }

    @Override
    public Fragment getItem(int position) {
        ReadingBookFragment fragment;
        if (position == getCount() - 1) {
            fragment = ReadingBookFragment.newInstance(mBook.getId(), getFileName(position));
        } else if (position == 0) {
            fragment = ReadingBookFragment.newInstance(mBook.getId(), getFileName(position));
        } else {
            fragment = ReadingBookFragment.newInstance(mBook.getId(), mBook.getContent().get(position - 1), getFileName(position), mBook.gettime_frame().get(position - 1));
        }
        return fragment;
    }

    @Override
    public int getCount() {

        return mBook.getContent().size() + 2;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        ReadingBookFragment fragment = (ReadingBookFragment) object;
        mSparseArray.put(position, fragment);
        super.setPrimaryItem(container, position, object);
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
