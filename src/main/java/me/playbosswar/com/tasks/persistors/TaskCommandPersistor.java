package me.playbosswar.com.tasks.persistors;


import com.google.common.reflect.TypeToken;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.LongStringType;
import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.utils.gson.GsonConverter;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Type;
import java.util.List;

public class TaskCommandPersistor extends LongStringType {
    private static final TaskCommandPersistor singleTon = new TaskCommandPersistor();

    private TaskCommandPersistor() {
        super(SqlType.LONG_STRING, new Class<?>[]{TaskCommand.class});
    }

    public static TaskCommandPersistor getSingleton() {
        return singleTon;
    }

    @Override
    public String javaToSqlArg(FieldType fieldType, Object javaObject) {
        GsonConverter gson = new GsonConverter();
        return gson.toJson(javaObject);
    }

    @Override
    public List<TaskCommand> sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        Type type = new TypeToken<List<TaskCommand>>(){}.getType();
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
