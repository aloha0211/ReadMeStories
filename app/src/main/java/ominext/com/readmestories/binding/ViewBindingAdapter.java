package ominext.com.readmestories.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ominext.com.readmestories.R;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"app:url"})
    public static void loadImage(final ImageView imageView, String url) {
        if (imageView.getTag().equals("assets")) {
            Utils.loadImageFromAssets(imageView, url, Constant.COVER);
        } else if (imageView.getTag().equals("firebase")){
            Utils.loadImageByBookId(imageView, url, Constant.COVER);
        }
    }
}
