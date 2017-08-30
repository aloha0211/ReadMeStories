package ominext.com.readmestories.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class Book implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private List<String> content = null;
    @SerializedName("time_frame")
    @Expose
    private List<List<Double>> timeFrame = null;

    private String author;

    private String illustrator;

    private Integer readingMode;

    public Book() {}

    public Book(Integer id, String title, List<String> content, List<List<Double>> timeFrame) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timeFrame = timeFrame;
    }

    protected Book(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.createStringArrayList();
        timeFrame = new ArrayList<>();
        in.readList(timeFrame, Book.class.getClassLoader());
        author = in.readString();
        illustrator = in.readString();
        readingMode = in.readInt();
    }

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

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public List<List<Double>> gettime_frame() {
        return timeFrame;
    }

    public void settime_frame(List<List<Double>> timeFrame) {
        this.timeFrame = timeFrame;
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

    public Integer getReadingMode() {
        return readingMode;
    }

    public void setReadingMode(int readingMode) {
        this.readingMode = readingMode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeStringList(content);
        parcel.writeList(timeFrame);
        parcel.writeString(author);
        parcel.writeString(illustrator);
        parcel.writeInt(readingMode);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
