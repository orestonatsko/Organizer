package ua.com.info.data;

import com.google.gson.annotations.SerializedName;

/**
 * Створено v.m 01.08.2017.
 */

public enum Direction {
    @SerializedName("0")
    none (0),
    @SerializedName("1")
    input(1),
    @SerializedName("2")
    output(2),
    @SerializedName("3")
    inputOutput(3);

    private final int value;
    Direction(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }

    public static Direction fromInt(int value) {
        switch (value) {
            case 0:
                return none;
            case 1:
                return input;
            case 2:
                return output;
            case 3:
                return inputOutput;
        }
        return none;
    }
}
