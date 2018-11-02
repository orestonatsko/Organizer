package ua.com.info.data;

import java.util.ArrayList;

/**
 * Створив VM 26.07.2017.
 */

public class ColumnCollection extends ArrayList<Column> {

    public ColumnCollection() {
    }

    public ColumnCollection(int capacity) {
        super(capacity);
    }

    public Column get(String name){
        for(Column col: this){
            if (col.getName().toLowerCase().equals(name.toLowerCase())){
                return col;
            }
        }
        return null;
    }

    public boolean contains(String colName){
        return get(colName)!=null;
    }
}
