package ominext.com.readmestories;

import java.util.ArrayList;
import java.util.List;

import ominext.com.readmestories.models.Book;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class Data {

    public static List<Book> getBookList() {
        List<Book> bookList = new ArrayList<>();
        Book book = new Book();
        book.id = 1;
        book.title = "Foxy Joxy Plays a Trick";
        book.imageUrl = "https://avatars1.githubusercontent.com/u/14102732?v=3";
        bookList.add(book);

        book = new Book();
        book.id = 2;
        book.title = "Foxy Joxy Plays a Trick";
        book.imageUrl = "http://bookdash.org/wp-content/uploads/2017/03/foxy-joxy-plays-a-trick_english_20170320_cover.jpg";
        bookList.add(book);

        book = new Book();
        book.id = 3;
        book.title = "Foxy Joxy Plays a Trick Foxy Joxy Plays a Trick";
        book.imageUrl = "http://bookdash.org/wp-content/uploads/2017/03/foxy-joxy-plays-a-trick_english_20170320_cover.jpg";
        bookList.add(book);

        book = new Book();
        book.id = 4;
        book.title = "Foxy Joxy";
        book.imageUrl = "http://bookdash.org/wp-content/uploads/2017/03/foxy-joxy-plays-a-trick_english_20170320_cover.jpg";
        bookList.add(book);

        book = new Book();
        book.id = 5;
        book.title = "Foxy Joxy Plays a Trick";
        book.imageUrl = "http://bookdash.org/wp-content/uploads/2017/03/foxy-joxy-plays-a-trick_english_20170320_cover.jpg";
        bookList.add(book);

        book = new Book();
        book.id = 6;
        book.title = "Foxy Joxy Plays a Trick";
        book.imageUrl = "http://bookdash.org/wp-content/uploads/2017/03/foxy-joxy-plays-a-trick_english_20170320_cover.jpg";
        bookList.add(book);

        book = new Book();
        book.id = 7;
        book.title = "Foxy Joxy";
        book.imageUrl = "http://bookdash.org/wp-content/uploads/2017/03/foxy-joxy-plays-a-trick_english_20170320_cover.jpg";
        bookList.add(book);

        book = new Book();
        book.id = 8;
        book.title = "Foxy Joxy";
        book.imageUrl = "http://bookdash.org/wp-content/uploads/2017/03/foxy-joxy-plays-a-trick_english_20170320_cover.jpg";
        bookList.add(book);

        return bookList;
    }
}
