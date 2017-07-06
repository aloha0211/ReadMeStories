package ominext.com.readmestories.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ominext.com.readmestories.R;
import ominext.com.readmestories.manager.ReadingBookManager;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

/**
 * Created by LuongHH on 7/5/2017.
 */

public class BookPageProvider implements CurlView.PageProvider {

    private Context mContext;
    private Book mBook;
    private SparseArrayCompat<ReadingBookManager> mSparseArray = new SparseArrayCompat<>();

    public BookPageProvider(Context context, Book book) {
        mContext = context;
        mBook = book;
    }

    @Override
    public int getPageCount() {
        return mBook.getContent().size() + 2;
    }

    private Bitmap loadBitmap(int width, int height, int index, boolean isFront) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Bitmap resources.
        int bitmapId;
        View v;
        Log.v("BookPageProvider", " - " + index);
        if (isFront) {
            bitmapId = Utils.setResource(mContext, "image_curl_view", "layout");
            v = inflater.inflate(bitmapId, null, true);
            ImageView ivContent = (ImageView) v.findViewById(R.id.iv_content);
            Utils.loadImage(ivContent, String.valueOf(mBook.getId()), getFileName(index));
        } else {
            bitmapId = Utils.setResource(mContext, "content_curl_view", "layout");
            v = inflater.inflate(bitmapId, null, true);
            TextView tvContent = (TextView) v.findViewById(R.id.tv_content);
            if (index < getPageCount() - 2) {
                String content = mBook.getContent().get(index);
                content = content.replaceAll("&quot;", "\"").replaceAll("&#39;", "\'");
                tvContent.setText(content);
            }
            ReadingBookManager readingBookManager;
            if (index == getPageCount() - 1) {
                readingBookManager = new ReadingBookManager(mContext, mBook.getId(), getFileName(index));
            } else if (index == 0) {
                readingBookManager = new ReadingBookManager(mContext, mBook.getId(), getFileName(index));
            } else {
                readingBookManager = new ReadingBookManager(mContext, mBook.getId(), mBook.getContent().get(index - 1), getFileName(index), mBook.gettime_frame().get(index - 1));
            }
            mSparseArray.put(index, readingBookManager);
        }
        v.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight()
                , Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    @Override
    public void updatePage(CurlPage page, int width, int height, int index) {
        Bitmap front = loadBitmap(width, height, index, true);
        Bitmap kiri = loadBitmap(width, height, index, false);
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        Bitmap flipBack = Bitmap.createBitmap(kiri, 0, 0, kiri.getWidth(), kiri.getHeight(), matrix, false);
        page.setTexture(front, CurlPage.SIDE_FRONT);
        page.setTexture(flipBack, CurlPage.SIDE_BACK);
    }

    public ReadingBookManager getCurrentReading(int position) {
        return mSparseArray.get(position);
    }

    private String getFileName(int position) {
        String fileName;
        if (position == 0) {
            fileName = Constant.COVER;
        } else if (position == getPageCount() - 1) {
            fileName = Constant.BACK_COVER;
        } else {
            fileName = String.valueOf(position);
        }
        return fileName;
    }
}
