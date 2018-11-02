package ua.com.info.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * Створено v.m 04.12.2017.
 */

public class JsonHelper {

    private static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Result.class, new Result.Deserializer());
        builder.registerTypeAdapter(Table.class, new Table.Deserializer());
        builder.setDateFormat("yyyyMMdd HH:mm:ss");
        gson = builder.create();
    }

    JsonHelper() {

    }

    public static String SerializeObject(Object object) {
        return gson.toJson(object);
    }

    public static <T> T DeserializeObject(String json, Type type) {
        return gson.fromJson(json, type);
    }

    static <T> T DeserializeObject(JsonElement json, Type type) {
        return gson.fromJson(json, type);
    }
}
