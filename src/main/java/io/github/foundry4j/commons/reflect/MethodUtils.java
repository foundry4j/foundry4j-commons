package io.github.foundry4j.commons.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/// 方法级别反射工具：获取方法、调用方法、判断方法特征。
public final class MethodUtils {

    private MethodUtils() {
    }

    // ==================== 方法获取 ====================

    /// 获取声明的方法（含 private，不含父类）
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(methodName, "methodName must not be null");
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such method: " + clazz.getName() + "." + methodName, e);
        }
    }

    /// 获取方法（含父类，向上查找）
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(methodName, "methodName must not be null");
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                Method method = current.getDeclaredMethod(methodName, paramTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
                current = current.getSuperclass();
            }
        }
        throw new RuntimeException("No such method in hierarchy: " + clazz.getName() + "." + methodName);
    }

    /// 获取所有声明方法（含 private，不含父类）
    public static List<Method> getDeclaredMethods(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            m.setAccessible(true);
        }
        return List.of(methods);
    }

    /// 获取所有方法（含父类，不含 Object）
    public static List<Method> getAllMethods(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        List<Method> result = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Method m : current.getDeclaredMethods()) {
                m.setAccessible(true);
                result.add(m);
            }
            current = current.getSuperclass();
        }
        return result;
    }

    /// 按方法名获取所有重载方法
    public static List<Method> getMethodsByName(Class<?> clazz, String methodName) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(methodName, "methodName must not be null");
        List<Method> result = new ArrayList<>();
        for (Method m : getAllMethods(clazz)) {
            if (m.getName().equals(methodName)) {
                result.add(m);
            }
        }
        return result;
    }

    /// 按返回类型过滤方法
    public static List<Method> getMethodsByReturnType(Class<?> clazz, Class<?> returnType) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(returnType, "returnType must not be null");
        List<Method> result = new ArrayList<>();
        for (Method m : getAllMethods(clazz)) {
            if (returnType.isAssignableFrom(m.getReturnType())) {
                result.add(m);
            }
        }
        return result;
    }

    // ==================== 方法调用 ====================

    /// 调用实例方法（自动推断参数类型）
    public static Object invoke(Object target, String methodName, Object... args) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(methodName, "methodName must not be null");

        Class<?>[] paramTypes = args == null ? new Class<?>[0]
                : Arrays.stream(args).map(a -> a == null ? Object.class : a.getClass()).toArray(Class<?>[]::new);

        Method method = findMatchingMethod(target.getClass(), methodName, paramTypes);
        method.setAccessible(true);
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + methodName, e);
        }
    }

    /// 调用实例方法（指定参数类型）
    public static Object invoke(Object target, String methodName, Class<?>[] paramTypes, Object... args) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(methodName, "methodName must not be null");
        Method method = getMethod(target.getClass(), methodName, paramTypes);
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + methodName, e);
        }
    }

    /// 调用静态方法（自动推断参数类型）
    public static Object invokeStatic(Class<?> clazz, String methodName, Object... args) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(methodName, "methodName must not be null");

        Class<?>[] paramTypes = args == null ? new Class<?>[0]
                : Arrays.stream(args).map(a -> a == null ? Object.class : a.getClass()).toArray(Class<?>[]::new);

        Method method = findMatchingMethod(clazz, methodName, paramTypes);
        method.setAccessible(true);
        try {
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke static method: " + methodName, e);
        }
    }

    /// 调用静态方法（指定参数类型）
    public static Object invokeStatic(Class<?> clazz, String methodName, Class<?>[] paramTypes, Object... args) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(methodName, "methodName must not be null");
        Method method = getMethod(clazz, methodName, paramTypes);
        try {
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke static method: " + methodName, e);
        }
    }

    private static Method findMatchingMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        // 先沿类层级搜索（含 private）
        Class<?> current = clazz;
        while (current != null) {
            for (Method m : current.getDeclaredMethods()) {
                if (m.getName().equals(methodName) && isParameterMatch(m.getParameterTypes(), paramTypes)) {
                    m.setAccessible(true);
                    return m;
                }
            }
            current = current.getSuperclass();
        }
        throw new RuntimeException("No matching method found: " + clazz.getName() + "." + methodName);
    }

    private static boolean isParameterMatch(Class<?>[] declared, Class<?>[] actual) {
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

    private static boolean isAssignable(Class<?>[] declared, Class<?>[] actual) {
        if (declared.length != actual.length) return false;
        for (int i = 0; i < declared.length; i++) {
            if (!isAssignable(declared[i], actual[i])) return false;
        }
        return true;
    }

    private static boolean isAssignable(Class<?> declared, Class<?> actual) {
        if (declared.isPrimitive()) {
            return wrap(declared).isAssignableFrom(wrap(actual));
        }
        return declared.isAssignableFrom(actual);
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

    // ==================== 方法特征判断 ====================

    /// 是否为 static 方法
    public static boolean isStatic(Method method) {
        Objects.requireNonNull(method, "method must not be null");
        return Modifier.isStatic(method.getModifiers());
    }

    /// 是否为 abstract 方法
    public static boolean isAbstract(Method method) {
        Objects.requireNonNull(method, "method must not be null");
        return Modifier.isAbstract(method.getModifiers());
    }

    /// 是否为 final 方法
    public static boolean isFinal(Method method) {
        Objects.requireNonNull(method, "method must not be null");
        return Modifier.isFinal(method.getModifiers());
    }

    /// 是否为 synchronized 方法
    public static boolean isSynchronized(Method method) {
        Objects.requireNonNull(method, "method must not be null");
        return Modifier.isSynchronized(method.getModifiers());
    }

    /// 是否为 public 方法
    public static boolean isPublic(Method method) {
        Objects.requireNonNull(method, "method must not be null");
        return Modifier.isPublic(method.getModifiers());
    }

    /// 是否为 private 方法
    public static boolean isPrivate(Method method) {
        Objects.requireNonNull(method, "method must not be null");
        return Modifier.isPrivate(method.getModifiers());
    }

    /// 是否为 getter 方法（以 get/is 开头、无参、有返回值）
    public static boolean isGetter(Method method) {
        Objects.requireNonNull(method, "method must not be null");
        if (method.getParameterCount() != 0) return false;
        if (method.getReturnType() == void.class) return false;
        String name = method.getName();
        return name.startsWith("get") || name.startsWith("is");
    }

    /// 是否为 setter 方法（以 set 开头、单参、无返回值）
    public static boolean isSetter(Method method) {
        Objects.requireNonNull(method, "method must not be null");
        if (method.getParameterCount() != 1) return false;
        if (method.getReturnType() != void.class) return false;
        return method.getName().startsWith("set");
    }
}