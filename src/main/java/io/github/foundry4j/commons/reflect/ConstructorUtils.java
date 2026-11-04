package io.github.foundry4j.commons.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/// 构造器级别反射工具：获取构造器、创建实例、判断构造器特征。
public final class ConstructorUtils {

    private ConstructorUtils() {
    }

    // ==================== 构造器获取 ====================

    /// 获取声明的构造器（含 private，指定参数类型）
    public static Constructor<?> getDeclaredConstructor(Class<?> clazz, Class<?>... paramTypes) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such constructor in: " + clazz.getName(), e);
        }
    }

    /// 获取无参构造器（含 private）
    public static Constructor<?> getDefaultConstructor(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return getDeclaredConstructor(clazz);
    }

    /// 获取所有声明的构造器（含 private）
    public static List<Constructor<?>> getDeclaredConstructors(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            c.setAccessible(true);
        }
        return Arrays.asList(constructors);
    }

    /// 获取所有 public 构造器
    public static List<Constructor<?>> getPublicConstructors(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return Arrays.asList(clazz.getConstructors());
    }

    /// 按参数数量过滤构造器
    public static List<Constructor<?>> getConstructorsByParamCount(Class<?> clazz, int paramCount) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        List<Constructor<?>> result = new ArrayList<>();
        for (Constructor<?> c : getDeclaredConstructors(clazz)) {
            if (c.getParameterCount() == paramCount) {
                result.add(c);
            }
        }
        return result;
    }

    // ==================== 实例创建 ====================

    /// 通过无参构造器创建实例
    public static <T> T newInstance(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of: " + clazz.getName(), e);
        }
    }

    /// 通过指定参数创建实例（自动匹配构造器）
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz, Object... args) {
        Objects.requireNonNull(clazz, "clazz must not be null");

        if (args == null || args.length == 0) {
            return newInstance(clazz);
        }

        Class<?>[] paramTypes = Arrays.stream(args)
                .map(a -> a == null ? Object.class : a.getClass())
                .toArray(Class<?>[]::new);

        Constructor<?> constructor = findMatchingConstructor(clazz, paramTypes);
        constructor.setAccessible(true);
        try {
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of: " + clazz.getName(), e);
        }
    }

    /// 通过指定参数类型和参数创建实例
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz, Class<?>[] paramTypes, Object... args) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        try {
            Constructor<T> constructor = (Constructor<T>) clazz.getDeclaredConstructor(paramTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of: " + clazz.getName(), e);
        }
    }

    private static Constructor<?> findMatchingConstructor(Class<?> clazz, Class<?>[] paramTypes) {
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            if (isAssignable(c.getParameterTypes(), paramTypes)) {
                return c;
            }
        }
        throw new RuntimeException("No matching constructor found in: " + clazz.getName());
    }

    private static boolean isAssignable(Class<?>[] declared, Class<?>[] actual) {
        if (declared.length != actual.length) return false;
        for (int i = 0; i < declared.length; i++) {
            Class<?> d = declared[i];
            Class<?> a = actual[i];
            if (d.isPrimitive()) {
                d = wrap(d);
                a = wrap(a);
            }
            if (!d.isAssignableFrom(a)) return false;
        }
        return true;
    }

    private static Class<?> wrap(Class<?> type) {
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        if (type == boolean.class) return Boolean.class;
        if (type == byte.class) return Byte.class;
        if (type == short.class) return Short.class;
        if (type == char.class) return Character.class;
        return type;
    }

    // ==================== 构造器特征判断 ====================

    /// 是否为 public 构造器
    public static boolean isPublic(Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "constructor must not be null");
        return Modifier.isPublic(constructor.getModifiers());
    }

    /// 是否为 private 构造器
    public static boolean isPrivate(Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "constructor must not be null");
        return Modifier.isPrivate(constructor.getModifiers());
    }

    /// 是否为 protected 构造器
    public static boolean isProtected(Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "constructor must not be null");
        return Modifier.isProtected(constructor.getModifiers());
    }

    /// 是否为包级私有构造器
    public static boolean isPackagePrivate(Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "constructor must not be null");
        int mod = constructor.getModifiers();
        return !Modifier.isPublic(mod) && !Modifier.isProtected(mod) && !Modifier.isPrivate(mod);
    }

    /// 是否为可变参数构造器
    public static boolean isVarArgs(Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "constructor must not be null");
        return constructor.isVarArgs();
    }

    /// 获取构造器参数类型列表
    public static List<Class<?>> getParameterTypes(Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "constructor must not be null");
        return Arrays.asList(constructor.getParameterTypes());
    }
}