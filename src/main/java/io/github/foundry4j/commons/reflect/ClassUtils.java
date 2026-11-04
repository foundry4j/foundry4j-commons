package io.github.foundry4j.commons.reflect;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// 类级别反射工具：获取类名、判断类特征、查找类层级关系等。
public final class ClassUtils {

    private ClassUtils() {
    }

    // ==================== 类名获取 ====================

    /// 获取简单类名（不含包名）
    public static String getSimpleName(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return clazz.getSimpleName();
    }

    /// 获取类的全限定名
    public static String getClassName(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return clazz.getName();
    }

    /// 获取全限定类名
    public static String getCanonicalName(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return clazz.getCanonicalName();
    }

    /// 获取包名
    public static String getPackageName(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Package pkg = clazz.getPackage();
        return pkg != null ? pkg.getName() : "";
    }

    // ==================== 类特征判断 ====================

    /// 是否为 public 类
    public static boolean isPublic(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return Modifier.isPublic(clazz.getModifiers());
    }

    /// 是否为接口
    public static boolean isInterface(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return clazz.isInterface();
    }

    /// 是否为抽象类
    public static boolean isAbstract(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return Modifier.isAbstract(clazz.getModifiers());
    }

    /// 是否为枚举类
    public static boolean isEnum(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return clazz.isEnum();
    }

    /// 是否为注解类型
    public static boolean isAnnotation(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return clazz.isAnnotation();
    }

    /// 是否为数组类型
    public static boolean isArray(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return clazz.isArray();
    }

    /// 是否为基本类型（含 void）
    public static boolean isPrimitive(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return clazz.isPrimitive();
    }

    /// 是否为基本类型的包装类（Integer、Long 等）
    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return clazz == Boolean.class || clazz == Byte.class

                || clazz == Character.class || clazz == Short.class
                || clazz == Integer.class || clazz == Long.class
                || clazz == Float.class || clazz == Double.class;
    }

    /// 是否为基本类型或其包装类
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return isPrimitive(clazz) || isPrimitiveWrapper(clazz);
    }

    // ==================== 类层级关系 ====================

    /// 判断 target 是否为 source 的父类或父接口
    public static boolean isAssignable(Class<?> target, Class<?> source) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(source, "source must not be null");
        return target.isAssignableFrom(source);
    }

    /// 获取所有父类（不含自身，包含 Object，按直接父类到 Object 的顺序）
    public static List<Class<?>> getAllSuperclasses(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        List<Class<?>> result = new ArrayList<>();
        Class<?> current = clazz.getSuperclass();
        while (current != null) {
            result.add(current);
            current = current.getSuperclass();
        }
        return result;
    }

    /// 获取所有接口（含父类实现的接口，去重）
    public static List<Class<?>> getAllInterfaces(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        List<Class<?>> result = new ArrayList<>();
        collectInterfaces(clazz, result);
        return result;
    }

    private static void collectInterfaces(Class<?> clazz, List<Class<?>> result) {
        if (clazz == null || clazz == Object.class) {
            return;
        }
        for (Class<?> iface : clazz.getInterfaces()) {
            if (!result.contains(iface)) {
                result.add(iface);
            }
            collectInterfaces(iface, result);
        }
        collectInterfaces(clazz.getSuperclass(), result);
    }

    /// 判断类是否实现了指定接口
    public static boolean hasInterface(Class<?> clazz, Class<?> interfaceType) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(interfaceType, "interfaceType must not be null");
        return getAllInterfaces(clazz).contains(interfaceType);
    }

    /// 判断类是否继承自指定父类
    public static boolean hasSuperClass(Class<?> clazz, Class<?> superClass) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(superClass, "superClass must not be null");
        return getAllSuperclasses(clazz).contains(superClass);
    }

    // ==================== 类加载 ====================

    /// 根据全限定名加载类
    public static Class<?> forName(String className) throws ClassNotFoundException {
        Objects.requireNonNull(className, "className must not be null");
        return Class.forName(className);
    }

    /// 根据全限定名加载类，加载失败返回 null
    public static Class<?> forNameOrNull(String className) {
        try {
            return forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}