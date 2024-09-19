package me.playbosswar.com.tasks.persistors;


import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.LongStringType;
import me.playbosswar.com.tasks.TaskInterval;
import me.playbosswar.com.utils.gson.GsonConverter;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TaskIntervalPersistor extends LongStringType {
    private static final TaskIntervalPersistor singleTon = new TaskIntervalPersistor();

    private TaskIntervalPersistor() {
        super(SqlType.LONG_STRING, new Class<?>[]{TaskInterval.class});
    }

    public static TaskIntervalPersistor getSingleton() {
        return singleTon;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        GsonConverter gson = new GsonConverter();
        return gson.toJson(javaObject);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        String s = (String) sqlArg;
        GsonConverter gson = new GsonConverter();
        JSONParser jsonParser = new JSONParser();
        try {
            return gson.fromJson(jsonParser.parse(s).toString(), TaskInterval.class);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
