package ominext.com.readmestories.models;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LuongHH on 8/7/2017.
 */

public class Category {

//    @SerializedName("slug")
//    private String slug;

    @SerializedName("collectionId")
    private int collectionId;

    @SerializedName("title")
    private String title;

    @SerializedName("collection")
    private String collection;

    @SerializedName("description")
    private String description;

//    @SerializedName("categorizable_type_restriction")
//    private String categorizableTypeRestriction;
//
//    @SerializedName("us_only")
//    private boolean usOnly;
//
//    @SerializedName("url")
//    private String url;

    @SerializedName("largeTout")
    private String largeTout;

//    @SerializedName("smallTout")
//    private String smallTout;
//
//    @SerializedName("is_active")
//    private boolean isActive;
//
//
//    @SerializedName("is_featured")
//    private boolean isFeatured;
//
//    @SerializedName("standard_image")
//    private String standardImage;
//
//    @SerializedName("featured")
//    private List<Object> featured;

    @SerializedName("books")
    private List<BookResponse> books;

//    public String getSlug() {
//        return slug;
//    }
//
//    public void setSlug(String slug) {
//        this.slug = slug;
//    }

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

//    public String getCategorizableTypeRestriction() {
//        return categorizableTypeRestriction;
//    }
//
//    public void setCategorizableTypeRestriction(String categorizableTypeRestriction) {
//        this.categorizableTypeRestriction = categorizableTypeRestriction;
//    }
//
//    public boolean isUsOnly() {
//        return usOnly;
//    }
//
//    public void setUsOnly(boolean usOnly) {
//        this.usOnly = usOnly;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }

    public String getLargeTout() {
        return largeTout;
    }

    public void setLargeTout(String largeTout) {
        this.largeTout = largeTout;
    }

//    public String getSmallTout() {
//        return smallTout;
//    }
//
//    public void setSmallTout(String smallTout) {
//        this.smallTout = smallTout;
//    }
//
//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }
//
//    @Exclude
//    public boolean isFeatured() {
//        return isFeatured;
//    }
//
//    @Exclude
//    public void setFeatured(boolean featured) {
//        isFeatured = featured;
//    }
//
//    public String getStandardImage() {
//        return standardImage;
//    }
//
//    public void setStandardImage(String standardImage) {
//        this.standardImage = standardImage;
//    }
//
//    public List<Object> getFeatured() {
//        return featured;
//    }
//
//    public void setFeatured(List<Object> featured) {
//        this.featured = featured;
//    }

    public List<BookResponse> getBooks() {
        return books;
    }

    public void setBooks(List<BookResponse> books) {
        this.books = books;
    }
}
