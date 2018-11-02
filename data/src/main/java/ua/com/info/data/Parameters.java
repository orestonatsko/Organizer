package ua.com.info.data;

import java.util.ArrayList;

/**
 * Створено v.m 27.07.2017.
 */

public class Parameters extends ArrayList<Parameter> {
    public Parameters(){
    }

    public Parameters(String name, Object value){
        add(new Parameter(name, value));
    }

    public Parameters(String name, Object value, Direction direction, Types type){
        add(new Parameter(name, value, direction, type));
    }
}
