package ominext.com.readmestories.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LuongHH on 6/23/2017.
 */

public class BookResponse {

    @SerializedName("")
    private List<Book> mBooks;

    public List<Book> getBooks() {
        return mBooks;
    }
}
