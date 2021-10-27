package me.playbosswar.com.utils.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GsonLocalTime implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String ldtString = jsonElement.getAsString();
        return LocalTime.parse(ldtString, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public JsonElement serialize(LocalTime localTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
    }
}
