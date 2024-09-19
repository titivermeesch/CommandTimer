package me.playbosswar.com.tasks.persistors;


import com.google.common.reflect.TypeToken;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.LongStringType;
import me.playbosswar.com.tasks.TaskTime;
import me.playbosswar.com.utils.gson.GsonConverter;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Type;
import java.util.List;

public class TaskTimePersistor extends LongStringType {
    private static final TaskTimePersistor singleTon = new TaskTimePersistor();

    private TaskTimePersistor() {
        super(SqlType.LONG_STRING, new Class<?>[]{TaskTime.class});
    }

    public static TaskTimePersistor getSingleton() {
        return singleTon;
    }

    @Override
    public String javaToSqlArg(FieldType fieldType, Object javaObject) {
        GsonConverter gson = new GsonConverter();
        return gson.toJson(javaObject);
    }

    @Override
    public List<TaskTime> sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        Type type = new TypeToken<List<TaskTime>>(){}.getType();
        String s = (String) sqlArg;
        GsonConverter gson = new GsonConverter();
        JSONParser jsonParser = new JSONParser();
        try {
            return gson.fromJson(jsonParser.parse(s).toString(), type);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
