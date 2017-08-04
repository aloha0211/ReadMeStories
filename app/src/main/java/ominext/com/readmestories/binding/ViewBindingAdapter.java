package ominext.com.readmestories.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"app:bookId"})
    public static void loadImage(final ImageView imageView, String bookId) {
        if (imageView.getTag().equals("assets")) {
            Utils.loadImageFromAssets(imageView, bookId, Constant.COVER);
        } else {
            Utils.loadImageFromFirebase(imageView, bookId, Constant.COVER);
        }
    }
}
