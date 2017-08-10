package ominext.com.readmestories.fragments.category;

import android.net.Uri;

import java.util.List;

import ominext.com.readmestories.models.Category;

/**
 * Created by LuongHH on 6/23/2017.
 */

public interface CategoryView {

    void onSuccessful(List<Category> bookList);

    void onLoadImageUriSuccessful(Uri uri);

    void onFailed(String message);
}
