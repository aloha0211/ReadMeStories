package ominext.com.readmestories.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import ominext.com.readmestories.realm.RealmDouble;

/**
 * Created by LuongHH on 8/3/2017.
 */

public class RealmListDouble extends RealmObject {

    private RealmList<RealmDouble> value;

    public RealmList<RealmDouble> getValue() {
        return value;
    }

    public void setValue(RealmList<RealmDouble> val) {
        this.value = val;
    }
}
