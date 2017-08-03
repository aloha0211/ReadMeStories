package ominext.com.readmestories.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;
import ominext.com.readmestories.models.BookRealm;
import ominext.com.readmestories.utils.Constant;

/**
 * Created by LuongHH on 8/3/2017.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm istance
    public void refresh() {
        realm.refresh();
    }

    //clear all objects from BookRealm.class
    public void clearAll() {
        realm.beginTransaction();
        realm.clear(BookRealm.class);
        realm.commitTransaction();
    }

    //find all objects in the BookRealm.class
    public RealmResults<BookRealm> getBooks() {
        return realm.where(BookRealm.class).findAll();
    }

    //query a single item with the given id
    public BookRealm getBook(int id) {
        return realm.where(BookRealm.class).equalTo(Constant.ID, id).findFirst();
    }

    public void addBook(BookRealm BookRealm) {
        realm.beginTransaction();
        realm.copyToRealm(BookRealm);
        realm.commitTransaction();
    }

    public void deleteBook(int id) {
        realm.beginTransaction();
        RealmResults<BookRealm> results = realm.where(BookRealm.class).equalTo(Constant.ID, id).findAll();
        results.clear();
        realm.commitTransaction();
    }

    //check if BookRealm.class is empty
    public boolean hasBooks() {
        return !realm.allObjects(BookRealm.class).isEmpty();
    }

    //query example
    public RealmResults<BookRealm> queryedBooks() {
        return realm.where(BookRealm.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}
