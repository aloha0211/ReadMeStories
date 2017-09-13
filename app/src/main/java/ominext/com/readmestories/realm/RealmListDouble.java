package ominext.com.readmestories.realm;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by LuongHH on 8/3/2017.
 */

public class RealmListDouble extends RealmObject {

    private RealmList<RealmDouble> value;

    public RealmListDouble() {
    }

    public RealmListDouble(List<Double> value) {
        this.value = new RealmList<>();
        for (int i = 0; i < value.size(); i++) {
            RealmDouble item = new RealmDouble(value.get(i));
            this.value.add(item);
        }
    }

    public RealmList<RealmDouble> getValue() {
        return value;
    }

    public void setValue(RealmList<RealmDouble> val) {
        this.value = val;
    }
}
