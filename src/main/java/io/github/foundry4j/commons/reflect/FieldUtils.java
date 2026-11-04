package io.github.foundry4j.commons.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// 字段级别反射工具：获取字段、读写字段值、判断字段特征。
public final class FieldUtils {

    private FieldUtils() {
    }

    // ==================== 字段获取 ====================

    /// 获取声明的字段（含 private，不含父类）
    public static Field getDeclaredField(Class<?> clazz, String fieldName) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(fieldName, "fieldName must not be null");
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("No such field: " + clazz.getName() + "." + fieldName, e);
        }
    }

    /// 获取字段（含父类，向上查找）
    public static Field getField(Class<?> clazz, String fieldName) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(fieldName, "fieldName must not be null");
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new RuntimeException("No such field in hierarchy: " + clazz.getName() + "." + fieldName);
    }

    /// 获取所有声明字段（含 private，不含父类）
    public static List<Field> getDeclaredFields(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
        }
        return List.of(fields);
    }

    /// 获取所有字段（含父类，不含 Object）
    public static List<Field> getAllFields(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        List<Field> result = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Field f : current.getDeclaredFields()) {
                f.setAccessible(true);
                result.add(f);
            }
            current = current.getSuperclass();
        }
        return result;
    }

    // ==================== 字段读写 ====================

    /// 读取字段值
    public static Object readField(Object target, String fieldName) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(fieldName, "fieldName must not be null");
        Field field = getField(target.getClass(), fieldName);
        field.setAccessible(true);
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to read field: " + fieldName, e);
        }
    }

    /// 读取字段值（指定类型）
    public static void writeField(Object target, String fieldName, Object value) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(fieldName, "fieldName must not be null");
        Field field = getField(target.getClass(), fieldName);
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to write field: " + fieldName, e);
        }
    }

    /// 读取静态字段值
    public static Object getStaticFieldValue(Class<?> clazz, String fieldName) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Field field = getField(clazz, fieldName);
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot read static field: " + fieldName, e);
        }
    }

    /// 写入静态字段值
    public static void setStaticFieldValue(Class<?> clazz, String fieldName, Object value) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Field field = getField(clazz, fieldName);
        try {
            field.set(null, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot write static field: " + fieldName, e);
        }
    }

    public static Object readStaticField(Class<?> clazz, String fieldName) {
        Field field = getDeclaredField(clazz, fieldName);
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to read static field: " + fieldName, e);
        }
    }

    // ==================== 字段特征判断 ====================

    /// 是否为 static 字段
    public static boolean isStatic(Field field) {
        Objects.requireNonNull(field, "field must not be null");
        return Modifier.isStatic(field.getModifiers());
    }

    /// 是否为 final 字段
    public static boolean isFinal(Field field) {
        Objects.requireNonNull(field, "field must not be null");
        return Modifier.isFinal(field.getModifiers());
    }

    /// 是否为 transient 字段
    public static boolean isTransient(Field field) {
        Objects.requireNonNull(field, "field must not be null");
        return Modifier.isTransient(field.getModifiers());
    }

    /// 是否为 volatile 字段
    public static boolean isVolatile(Field field) {
        Objects.requireNonNull(field, "field must not be null");
        return Modifier.isVolatile(field.getModifiers());
    }

    /// 是否为 public 字段
    public static boolean isPublic(Field field) {
        Objects.requireNonNull(field, "field must not be null");
        return Modifier.isPublic(field.getModifiers());
    }

    /// 是否为 private 字段
    public static boolean isPrivate(Field field) {
        Objects.requireNonNull(field, "field must not be null");
        return Modifier.isPrivate(field.getModifiers());
    }

    /// 按类型过滤字段
    public static List<Field> getFieldsByType(Class<?> clazz, Class<?> fieldType) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(fieldType, "fieldType must not be null");
        List<Field> result = new ArrayList<>();
        for (Field f : getAllFields(clazz)) {
            if (fieldType.isAssignableFrom(f.getType())) {
                result.add(f);
            }
        }
        return result;
    }
}