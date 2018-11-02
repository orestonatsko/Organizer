package ua.com.info.data;

/**
 * Створив VM 06.12.2017.
 */

public class Convert {
    public static double toDouble(Object value){
        if (value == null)
            return 0;
        else if (value instanceof Double)
            return (Double) value;
        else if ((value instanceof String) && (String.valueOf(value).equals("")))
            return 0;
        else
            return Double.valueOf(String.valueOf(value).replace(",", "."));
    }
}
