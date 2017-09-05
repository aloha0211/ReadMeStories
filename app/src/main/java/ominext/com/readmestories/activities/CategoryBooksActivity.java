package ominext.com.readmestories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.fragments.BooksByCategoryFragment;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.utils.Constant;
import ominext.com.readmestories.utils.Utils;

public class CategoryBooksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent data = getIntent();
        setTitle(data.getStringExtra(Constant.KEY_TITLE));
        List<Book> books = data.getParcelableArrayListExtra(Constant.KEY_BOOKS);
        List<Book> localBooks = new ArrayList<>();
        final List<Integer> bookIds = new ArrayList<>();
        localBooks.addAll(Utils.getBooksFromAssets(this));
        localBooks.addAll(Utils.getBooksFromRealm(this));
        for (int i = 0; i < localBooks.size(); i++) {
            bookIds.add(localBooks.get(i).getId());
        }
//        List<Book> bookList = Stream.of(books)
//                .filter(e -> bookIds.contains(e.getId())).collect(Collectors.toList());
        replaceFragment(BooksByCategoryFragment.newInstance(books));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.deleteCacheDir(this, Constant.STORY + "/" + Constant.TEMP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
