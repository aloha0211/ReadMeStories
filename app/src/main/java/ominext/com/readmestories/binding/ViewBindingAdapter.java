package ominext.com.readmestories.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"bind:url"})
    public static void loadImage(final ImageView imageView, String url) {
        if (imageView.getTag() == null) {
            Utils.loadImage(imageView, url);
        } else if (imageView.getTag().equals(Constant.MODE_FROM_ASSETS)) {
            Utils.loadImageFromAssets(imageView, url, Constant.COVER);
        } else if (imageView.getTag().equals(Constant.MODE_FROM_INTERNAL_STORAGE)) {
            Utils.loadImageFromInternalStorage(imageView, url, Constant.COVER);
        } else if (imageView.getTag().equals(Constant.FIREBASE)) {
            Utils.loadImageFromFirebase(imageView, url, Constant.COVER);
        }
    }
}
