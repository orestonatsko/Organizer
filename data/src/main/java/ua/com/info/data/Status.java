package ua.com.info.data;

import com.google.gson.annotations.SerializedName;

/**
 * Створив VM 03.12.2017.
 */

public enum Status {
    @SerializedName("0")
    Ok(0),
    @SerializedName("1")
    Error(1),
    @SerializedName("2")
    Timeout(2);

    private final int value;

    Status(int i) {
        value = i;
    }

    public static Status fromInt(int i){
        switch (i){
            case 0:
                return  Ok;
            case 2:
                return Timeout;
        }
        return  Error;
    }

}
