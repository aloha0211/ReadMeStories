package ominext.com.readmestories.realm;

import io.realm.RealmObject;

/**
 * Created by LuongHH on 8/3/2017.
 */

public class RealmDouble extends RealmObject {

    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
