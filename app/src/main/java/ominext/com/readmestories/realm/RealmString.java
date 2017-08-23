package ominext.com.readmestories.realm;

import io.realm.RealmObject;

/**
 * Created by LuongHH on 8/3/2017.
 */

public class RealmString extends RealmObject {

    private String value;

    public RealmString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
