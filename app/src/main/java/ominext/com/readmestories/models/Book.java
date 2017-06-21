package ominext.com.readmestories.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class Book implements Parcelable {

    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("image_url")
    public String imageUrl;

    public Book() {}

    protected Book(Parcel in) {
        id = in.readInt();
        title = in.readString();
        imageUrl = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(imageUrl);
    }
}
