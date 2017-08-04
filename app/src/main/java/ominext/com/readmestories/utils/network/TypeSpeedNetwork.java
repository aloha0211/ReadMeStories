package ominext.com.readmestories.utils.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dieu on 01/12/2015.
 */
public enum TypeSpeedNetwork {
    @SerializedName("-1")
    LOW(-1),
    @SerializedName("0")
    MEDIUM(0),
    @SerializedName("1")
    HIGH(1);


    TypeSpeedNetwork(int value) {
        this.value = value;
    }

    private int value;

    public static TypeSpeedNetwork fromValue(int value) {
        TypeSpeedNetwork[] values = TypeSpeedNetwork.values();
        for (TypeSpeedNetwork type : values) {
            if (type.value == value)
                return type;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
