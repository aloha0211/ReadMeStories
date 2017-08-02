package ominext.com.readmestories.fragments.mybooks;

import java.util.List;

import ominext.com.readmestories.models.Book;

/**
 * Created by LuongHH on 6/23/2017.
 */

public interface MyBooksView {

    void onSuccessful(List<Book> bookList);
    void onFailed(String message);
}
