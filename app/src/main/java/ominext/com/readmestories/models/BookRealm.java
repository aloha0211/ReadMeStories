package ominext.com.readmestories.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ominext.com.readmestories.realm.RealmListDouble;
import ominext.com.readmestories.realm.RealmString;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class BookRealm extends RealmObject {

    @PrimaryKey
    private Integer id;

    private String title;

    private RealmList<RealmString> content = null;

    private RealmList<RealmListDouble> time_frame = null;

    public BookRealm() {
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

    public RealmList<RealmString> getContent() {
        return content;
    }

    public void setContent(RealmList<RealmString> content) {
        this.content = content;
    }

    public RealmList<RealmListDouble> getTime_frame() {
        return time_frame;
    }

    public void setTime_frame(RealmList<RealmListDouble> time_frame) {
        this.time_frame = time_frame;
    }
}
