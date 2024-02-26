package me.playbosswar.com.utils.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GsonDate implements JsonSerializer<Date>, JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy, H:mm:ss a", Locale.US);
        try {
            return formatter.parse(jsonElement.getAsString());
        } catch(ParseException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy, H:mm:ss a", Locale.US);
        return new JsonPrimitive(sdf.format(date));
    }
}
