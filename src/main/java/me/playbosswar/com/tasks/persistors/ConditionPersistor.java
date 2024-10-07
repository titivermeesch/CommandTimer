package me.playbosswar.com.tasks.persistors;


import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.LongStringType;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.utils.gson.GsonConverter;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConditionPersistor extends LongStringType {
    private static final ConditionPersistor singleTon = new ConditionPersistor();

    private ConditionPersistor() {
        super(SqlType.LONG_STRING, new Class<?>[]{Condition.class});
    }

    public static ConditionPersistor getSingleton() {
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
            return gson.fromJson(jsonParser.parse(s).toString(), Condition.class);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
