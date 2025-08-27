package org.vocalsky.extended_tinker.util;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static <T, U> T getReflection(String name, Class<U> obj) {
        Field field;
        try {
            field = obj.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        Object target;
        try {
            target = field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (T) target;
    }
}
