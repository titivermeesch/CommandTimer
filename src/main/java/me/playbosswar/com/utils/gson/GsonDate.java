package me.playbosswar.com.utils.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GsonDate implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss. SSSXXX";

    @Override
    public Date deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
        try {
            return formatter.parse(jsonElement.getAsString());
        } catch(ParseException ex1) {
            // Check if maybe it's in the old format
            try {
                SimpleDateFormat oldFormatter = new SimpleDateFormat("MMM d, yyyy, H:mm:ss a");
                return oldFormatter.parse(jsonElement.getAsString());
            } catch(ParseException ex2) {
                try {
                    SimpleDateFormat oldFormatter = new SimpleDateFormat("MMM d, yyyy, HH:mm:ss a");
                    return oldFormatter.parse(jsonElement.getAsString());
                } catch(ParseException ex3) {
                    try {
                        SimpleDateFormat oldFormatter = new SimpleDateFormat("MMM d, yyyy, HH:mm:ss");
                        return oldFormatter.parse(jsonElement.getAsString());
                    } catch(ParseException ex4) {
                        throw new JsonParseException(ex4);
                    }
                }
            }
        }
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return new JsonPrimitive(sdf.format(date));
    }
}
