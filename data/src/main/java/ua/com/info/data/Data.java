package ua.com.info.data;

import java.util.ArrayList;

/**
 * Створив VM 07.08.2017.
 */

public class Data extends ArrayList<IDataObject> {

    public Table getTable() {
        for (IDataObject o : this)
            if (o instanceof Table) return (Table) o;
        return null;
    }

    public Variables getVariables(){
        for (IDataObject o : this)
            if (o instanceof Variables) return (Variables) o;
        return null;
    }

    public Data() {
    }

    public Data(int size) {
        super(size);
    }

    public void addCommand(String cmd, Parameters parameters)
    {
        add(new Command(cmd, parameters));
    }


//    public String prefix;
//
//    @Override
//    public void accept() {
//
//    }
//
//    @Override
//    public void reject() {
//
//    }
//
//    private Field getField(String name) {
//        for (Field f : this)
//            if (f.Name().equals(name)) return f;
//        return null;
//    }
//
//    public boolean containsField(String fieldName) {
//        return getField(fieldName) != null;
//    }
//
//    public int getInt(String fieldName) {
//        Field f=getField(fieldName);
//        if (f==null) throw new NullPointerException();
//        return (int) f.value().getValue();
//    }
//
//    public void save() {
//        if (db == null || prefix == null) return; // todo
//        Parameters parameters = new Parameters();
//        for (Field f : this) {
//            if (f.SaveDirection() != Direction.none) {
//                String s = f.Name();
//                Parameter p = new Parameter(f.Name(), f.value(), f.SaveDirection(), f.Type());
//                parameters.add(p);
//            }
//        }
//        db.execute(prefix + "_Записати", parameters, new DataBase.OnResultListener() {
//            @Override
//            public void onResult(Result result) {
//
//            }
//        });
////        if (res.get_status()== Result.Status.Ok){
////            for (Field f : this) {
////                Direction d = f.SaveDirection();
////                if (d==Direction.input||d==Direction.inputOutput) {
////                    f.value().setValue();
////                    Parameter p = new Parameter(f.Name(), f.value(), f.SaveDirection(), f.Type());
////                    parameters.add(p);
////                }
////            }
////        }
//
//    }
//
//    public void delete() {
//    }

}
