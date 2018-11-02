package ua.com.info.data;

/**
 * Створено v.m 05.03.2018.
 */

public class Command implements IDataObject {

    public String command;
    public Parameters parameters;

    public Command(String cmd, Parameters parameters) {
        command = cmd;
        this.parameters = parameters;
    }
}
