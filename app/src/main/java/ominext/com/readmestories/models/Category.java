package ominext.com.readmestories.models;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LuongHH on 8/7/2017.
 */

public class Category {

    @SerializedName("collectionId")
    private int collectionId;

    @SerializedName("title")
    private String title;

    @SerializedName("collection")
    private String collection;

    @SerializedName("description")
    private String description;

    @SerializedName("largeTout")
    private String largeTout;

    @SerializedName("books")
    private List<BookResponse> books;

    private Uri mUri;

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLargeTout() {
        return largeTout;
    }

    public void setLargeTout(String largeTout) {
        this.largeTout = largeTout;
    }

    public List<BookResponse> getBooks() {
        return books;
    }

    public void setBooks(List<BookResponse> books) {
        this.books = books;
    }
}
