package me.playbosswar.com.utils;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
    public static <T> T getNextValueInArray(List<T> list, T currentValue) {
        int currentIndex = list.indexOf(currentValue);

        if(currentIndex == list.size() - 1) {
            return list.get(0);
        }

        return list.get(currentIndex + 1);
    }

    public static <T> T getNextValueInArray(T[] arr, T currentValue) {
        List<T> list = Arrays.stream(arr).toList();
        int currentIndex = list.indexOf(currentValue);

        if(currentIndex == list.size() - 1) {
            return list.get(0);
        }

        return list.get(currentIndex + 1);
    }
}
