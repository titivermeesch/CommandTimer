package me.playbosswar.com.tasks.persistors;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.LongStringType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimePersistor extends LongStringType {
    private static final LocalTimePersistor singleTon = new LocalTimePersistor();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private LocalTimePersistor() {
        super(SqlType.LONG_STRING, new Class<?>[]{LocalTime.class});
    }

    public static LocalTimePersistor getSingleton() {
        return singleTon;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) {
            return null;
        }
        return ((LocalTime) javaObject).format(FORMATTER);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        if (sqlArg == null) {
            return null;
        }
        String s = (String) sqlArg;
        if (s.isEmpty()) {
            return null;
        }
        return LocalTime.parse(s, FORMATTER);
    }
}
