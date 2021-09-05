package me.playbosswar.com.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.time.LocalTime;

public class GsonConverter {
    protected final Gson gson;

    public GsonConverter() {
        final GsonBuilder builder = new GsonBuilder();
        builder
                .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeAdapter(LocalTime.class, new GsonLocalTime());
        this.gson = builder.create();
    }

    public <T> String toJson(T obj) {
        return gson.toJson(obj);
    }

    public <T> T fromJson(Reader reader, Type valueType) {
        return gson.fromJson(reader, valueType);
    }

    public <T> T fromJson(String string, Type valueType) {
        return gson.fromJson(string, valueType);
    }
}
