package me.playbosswar.com.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.Date;

public class GsonConverter {
    protected final Gson gson;

    public GsonConverter() {
        final GsonBuilder builder = new GsonBuilder();
        this.gson = builder
                .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeAdapter(Date.class, new GsonDate())
                .registerTypeAdapter(LocalTime.class, new GsonLocalTime())
                .setPrettyPrinting()
                .create();
    }

    public <T> String toJson(T obj) {
        return gson.toJson(obj);
    }

    public <T> T fromJson(String string, Type valueType) {
        return gson.fromJson(string, valueType);
    }
}
