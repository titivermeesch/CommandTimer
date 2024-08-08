package me.playbosswar.com.utils.gson;

import com.google.gson.*;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.parser.ParserException;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;


public class GsonDate implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss. SSSXXX";

    @Override
    public Date deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<SimpleDateFormat> formats = List.of(
                new SimpleDateFormat(FORMAT),
                new SimpleDateFormat("MMM d, yyyy, H:mm:ss a"),
                new SimpleDateFormat("MMM d, yyyy, HH:mm:ss a"),
                new SimpleDateFormat("MMM d, yyyy, HH:mm:ss"),
                new SimpleDateFormat("MMM d, yyyy, hh:mm:ss a"));
        for (SimpleDateFormat dataFormat : formats) {
            try {
                return dataFormat.parse(jsonElement.getAsString());
            } catch (ParseException ex) {
                // ignore
            }
        }
        throw new JsonParseException("Can not parse data: " + jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return new JsonPrimitive(sdf.format(date));
    }
}
