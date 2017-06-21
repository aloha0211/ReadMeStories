package ominext.com.readmestories.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ominext.com.readmestories.R;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"app:url"})
    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.background)
                .into(imageView);
    }
}
