package ua.com.info.data;

/**
 * Створено v.m 05.12.2017.
 */

public class Message implements IDataObject {
    public String text;

    Message() {
    }

    Message(String text) {
        this.text = text;
    }
}
