package ominext.com.readmestories.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import ominext.com.readmestories.utils.Utils;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"app:bookId"})
    public static void loadImage(final ImageView imageView, String bookId) {
        if (imageView.getTag().equals("local")) {
            Utils.loadLocalImage(imageView, bookId, "cover");
        } else {
            Utils.loadImage(imageView, bookId, "cover");
        }
    }
}
