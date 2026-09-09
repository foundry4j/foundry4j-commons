package io.github.tuyucheng777.commons.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/// 反射工具类，提供字段、方法、注解访问等常用能力。
public final class ReflectUtils {

    private ReflectUtils() {
    }

    /// 获取目标对象指定字段值，支持私有字段。
    ///
    /// @param target    目标对象
    /// @param fieldName 字段名
    /// @return 字段值
    public static Object getFieldValue(Object target, String fieldName) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(fieldName, "fieldName must not be null");

        try {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to get field value: " + fieldName, e);
        }
    }

    /// 设置目标对象指定字段值，支持私有字段。
    ///
    /// @param target    目标对象
    /// @param fieldName 字段名
    /// @param value     字段值
    public static void setFieldValue(Object target, String fieldName, Object value) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(fieldName, "fieldName must not be null");

        try {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to set field value: " + fieldName, e);
        }
    }

    /// 调用目标对象的无参方法。
    ///
    /// @param target     目标对象
    /// @param methodName 方法名
    /// @return 方法返回值
    public static Object invokeMethod(Object target, String methodName) {
        return invokeMethod(target, methodName, new Class[0], new Object[0]);
    }

    /// 调用目标对象的方法。
    ///
    /// @param target         目标对象
    /// @param methodName     方法名
    /// @param parameterTypes 参数类型
    /// @param args           参数值
    /// @return 方法返回值
    public static Object invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(methodName, "methodName must not be null");

        try {
            Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to invoke method: " + methodName, e);
        }
    }

    /// 获取类上指定类型的注解。
    ///
    /// @param clazz          目标类
    /// @param annotationType 注解类型
    /// @param <A>            注解泛型
    /// @return 注解实例，如果不存在则返回 null
    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationType) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(annotationType, "annotationType must not be null");
        return clazz.getAnnotation(annotationType);
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;

        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }

        throw new IllegalStateException("Field not found: " + fieldName);
    }

    /// 获取类中所有声明字段，包括私有字段。
    ///
    /// @param clazz 目标类
    /// @return 字段列表
    public static List<Field> getAllDeclaredFields(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");

        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;

        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }

        return fields;
    }
}