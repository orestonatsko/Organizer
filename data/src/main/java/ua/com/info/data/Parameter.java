package ua.com.info.data;

import android.util.Log;

import java.math.BigInteger;
import java.util.Date;

/**
 * Створено v.m 27.07.2017.
 */

public class Parameter {
    private final String name;
    private Object value;
    public Direction direction;
    public final Types type;
    int id;
    int size;

    public Parameter(String name, Object value) {
//        try {
//            name = new String(name.getBytes("windows-1251"), "UTF-8");
//        } catch (Exception ignore) {
//        }
        this.name = name;
        this.value = value;
        this.direction = Direction.input;
        type = SqlType(value);
    }

    private Types SqlType(Object o) {
        if (o instanceof Integer)
            return Types.Int;
        if (o instanceof String)
            return Types.VarChar;
        if (o instanceof Double || o instanceof Float)
            return Types.Decimal;
        if (o instanceof Date)
            return Types.DateTime;
        if(o instanceof BigInteger)
            return Types.BigInt;
        if(o instanceof Long)
            return Types.BigInt;
        return Types.Variant;
    }

    public Parameter(String name, Object value, Direction direction, Types type) {
        this(name, direction, type);
        this.value = value;
    }

    public Parameter(String name, Direction direction, Types type) {
        this.name = name;
        this.direction = direction;
        this.type = type;
        if (type == Types.VarChar) size = 255;
    }

    public void setValue(Object value) {
//        if (this.value instanceof Variable)
//            ((Variable) this.value).value = value;
//        else
        this.value = value;
    }

    public Object getValue() {
//        if (value instanceof Variable)
//            return ((Variable) this.value).value;
//        else
        return value;
    }

//    public int getType() {
//        if (type != 0) return type;
//        if (value instanceof Integer) {
//            return Types.INTEGER;
//        } else if (value instanceof String) {
//            return Types.VARCHAR;
//        } else if (value instanceof Float) {
//            return Types.DOUBLE;
//        } else if (value instanceof Date) {
//            return Types.DATE;
//        }
//        return 0;
//    }

//    public void setVariable(Object value) {
//        if (value instanceof Variable)
//            ((Variable) value).value = value;
//    }

    public String getName() {
        return name;
    }

    public String getParameterName() {
        if (name.startsWith("@")) return name;
        else return "@" + name;
    }
}
