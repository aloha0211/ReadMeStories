package ominext.com.readmestories.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LuongHH on 8/7/2017.
 */

public class BookResponse {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("sortTitle")
    @Expose
    private String sortTitle;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("illustrator")
    @Expose
    private String illustrator;

//    @SerializedName("thumbnail_url")
//    @Expose
//    private String thumbnailUrl;
//
//    @SerializedName("cover_url")
//    @Expose
//    private String coverUrl;
//
//    @SerializedName("page_count")
//    @Expose
//    private String pageCount;
//    @SerializedName("page_count_marketing")
//    @Expose
//    private Integer pageCountMarketing;
//
//    @SerializedName("price")
//    @Expose
//    private String price;
//
//    @SerializedName("updated_at")
//    @Expose
//    private Integer updatedAt;
//
//    @SerializedName("type")
//    @Expose
//    private String type;
//
//    @SerializedName("original_book_id")
//    @Expose
//    private String originalBookId;
//
//    @SerializedName("product_id")
//    @Expose
//    private String productId;
//
//    @SerializedName("book")
//    @Expose
//    private String book;
//
//    @SerializedName("book_url")
//    @Expose
//    private String bookUrl;
//
//    @SerializedName("description")
//    @Expose
//    private String description;
//
//    @SerializedName("sample_page_count")
//    @Expose
//    private Integer samplePageCount;
//
//    @SerializedName("categories")
//    @Expose
//    private List<String> categories = null;
//
//    @SerializedName("categories_with_url")
//    @Expose
//    private List<CategoriesWithUrl> categoriesWithUrl = null;
//
//    @SerializedName("version")
//    @Expose
//    private Integer version;
//
//    @SerializedName("old_available")
//    @Expose
//    private Integer oldAvailable;
//    @SerializedName("us_only")
//    @Expose
//    private Boolean usOnly;
//    @SerializedName("publisher_id")
//    @Expose
//    private Object publisherId;
//    @SerializedName("slug")
//    @Expose
//    private String slug;
//    @SerializedName("user_purchased")
//    @Expose
//    private Boolean userPurchased;
//    @SerializedName("user_subscribed")
//    @Expose
//    private Boolean userSubscribed;
//    @SerializedName("subscription_book_type")
//    @Expose
//    private String subscriptionBookType;
//    @SerializedName("genus")
//    @Expose
//    private String genus;
//    @SerializedName("book_infos")
//    @Expose
//    private BookInfos bookInfos;
//    @SerializedName("bookid")
//    @Expose
//    private String bookid;
//    @SerializedName("purchased")
//    @Expose
//    private String purchased;
//    @SerializedName("thumbnail")
//    @Expose
//    private String thumbnail;
//    @SerializedName("modificationTimestamp")
//    @Expose
//    private String modificationTimestamp;
//    @SerializedName("free")
//    @Expose
//    private Boolean free;
//    @SerializedName("url")
//    @Expose
//    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSortTitle() {
        return sortTitle;
    }

    public void setSortTitle(String sortTitle) {
        this.sortTitle = sortTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public void setIllustrator(String illustrator) {
        this.illustrator = illustrator;
    }

//    public String getThumbnailUrl() {
//        return thumbnailUrl;
//    }
//
//    public void setThumbnailUrl(String thumbnailUrl) {
//        this.thumbnailUrl = thumbnailUrl;
//    }
//
//    public String getCoverUrl() {
//        return coverUrl;
//    }
//
//    public void setCoverUrl(String coverUrl) {
//        this.coverUrl = coverUrl;
//    }
//
//    public String getPageCount() {
//        return pageCount;
//    }
//
//    public void setPageCount(String pageCount) {
//        this.pageCount = pageCount;
//    }
//
//    public Integer getPageCountMarketing() {
//        return pageCountMarketing;
//    }
//
//    public void setPageCountMarketing(Integer pageCountMarketing) {
//        this.pageCountMarketing = pageCountMarketing;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public Integer getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(Integer updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getOriginalBookId() {
//        return originalBookId;
//    }
//
//    public void setOriginalBookId(String originalBookId) {
//        this.originalBookId = originalBookId;
//    }
//
//    public String getProductId() {
//        return productId;
//    }
//
//    public void setProductId(String productId) {
//        this.productId = productId;
//    }
//
//    public String getBook() {
//        return book;
//    }
//
//    public void setBook(String book) {
//        this.book = book;
//    }
//
//    public String getBookUrl() {
//        return bookUrl;
//    }
//
//    public void setBookUrl(String bookUrl) {
//        this.bookUrl = bookUrl;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Integer getSamplePageCount() {
//        return samplePageCount;
//    }
//
//    public void setSamplePageCount(Integer samplePageCount) {
//        this.samplePageCount = samplePageCount;
//    }
//
//    public List<String> getCategories() {
//        return categories;
//    }
//
//    public void setCategories(List<String> categories) {
//        this.categories = categories;
//    }
//
//    public List<CategoriesWithUrl> getCategoriesWithUrl() {
//        return categoriesWithUrl;
//    }
//
//    public void setCategoriesWithUrl(List<CategoriesWithUrl> categoriesWithUrl) {
//        this.categoriesWithUrl = categoriesWithUrl;
//    }
//
//    public Integer getVersion() {
//        return version;
//    }
//
//    public void setVersion(Integer version) {
//        this.version = version;
//    }
//
//    public Integer getOldAvailable() {
//        return oldAvailable;
//    }
//
//    public void setOldAvailable(Integer oldAvailable) {
//        this.oldAvailable = oldAvailable;
//    }
//
//    public Boolean getUsOnly() {
//        return usOnly;
//    }
//
//    public void setUsOnly(Boolean usOnly) {
//        this.usOnly = usOnly;
//    }
//
//    public Object getPublisherId() {
//        return publisherId;
//    }
//
//    public void setPublisherId(Object publisherId) {
//        this.publisherId = publisherId;
//    }
//
//    public String getSlug() {
//        return slug;
//    }
//
//    public void setSlug(String slug) {
//        this.slug = slug;
//    }
//
//    public Boolean getUserPurchased() {
//        return userPurchased;
//    }
//
//    public void setUserPurchased(Boolean userPurchased) {
//        this.userPurchased = userPurchased;
//    }
//
//    public Boolean getUserSubscribed() {
//        return userSubscribed;
//    }
//
//    public void setUserSubscribed(Boolean userSubscribed) {
//        this.userSubscribed = userSubscribed;
//    }
//
//    public String getSubscriptionBookType() {
//        return subscriptionBookType;
//    }
//
//    public void setSubscriptionBookType(String subscriptionBookType) {
//        this.subscriptionBookType = subscriptionBookType;
//    }
//
//    public String getGenus() {
//        return genus;
//    }
//
//    public void setGenus(String genus) {
//        this.genus = genus;
//    }
//
//    public BookInfos getBookInfos() {
//        return bookInfos;
//    }
//
//    public void setBookInfos(BookInfos bookInfos) {
//        this.bookInfos = bookInfos;
//    }
//
//    public String getBookid() {
//        return bookid;
//    }
//
//    public void setBookid(String bookid) {
//        this.bookid = bookid;
//    }
//
//    public String getPurchased() {
//        return purchased;
//    }
//
//    public void setPurchased(String purchased) {
//        this.purchased = purchased;
//    }
//
//    public String getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }
//
//    public String getModificationTimestamp() {
//        return modificationTimestamp;
//    }
//
//    public void setModificationTimestamp(String modificationTimestamp) {
//        this.modificationTimestamp = modificationTimestamp;
//    }
//
//    public Boolean getFree() {
//        return free;
//    }
//
//    public void setFree(Boolean free) {
//        this.free = free;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
}
