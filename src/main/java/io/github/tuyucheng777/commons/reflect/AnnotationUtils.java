package io.github.tuyucheng777.commons.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/// 注解级别反射工具：获取注解、判断注解存在、读取注解属性值。
public final class AnnotationUtils {

    private AnnotationUtils() {
    }

    // ==================== 注解存在判断 ====================

    /// 元素上是否直接声明了指定注解（不含继承）
    public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        Objects.requireNonNull(element, "element must not be null");
        Objects.requireNonNull(annotationType, "annotationType must not be null");
        return element.isAnnotationPresent(annotationType);
    }

    /// 元素上是否存在指定注解（含元注解递归查找）
    public static boolean hasAnnotationDeep(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        Objects.requireNonNull(element, "element must not be null");
        Objects.requireNonNull(annotationType, "annotationType must not be null");
        return findAnnotationDeep(element, annotationType) != null;
    }

    // ==================== 注解获取 ====================

    /// 获取元素上直接声明的注解
    public static <A extends Annotation> A getAnnotation(AnnotatedElement element, Class<A> annotationType) {
        Objects.requireNonNull(element, "element must not be null");
        Objects.requireNonNull(annotationType, "annotationType must not be null");
        return element.getAnnotation(annotationType);
    }

    /// 获取元素上所有直接声明的注解
    public static List<Annotation> getAnnotations(AnnotatedElement element) {
        Objects.requireNonNull(element, "element must not be null");
        return Arrays.asList(element.getAnnotations());
    }

    /// 递归查找注解（含元注解）
    public static <A extends Annotation> A findAnnotationDeep(AnnotatedElement element, Class<A> annotationType) {
        Objects.requireNonNull(element, "element must not be null");
        Objects.requireNonNull(annotationType, "annotationType must not be null");

        A direct = element.getAnnotation(annotationType);
        if (direct != null) {
            return direct;
        }

        for (Annotation ann : element.getAnnotations()) {
            if (ann.annotationType().getPackageName().startsWith("java.lang")) {
                continue;
            }
            A found = findAnnotationDeep(ann.annotationType(), annotationType);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /// 获取元素上所有注解（含元注解，去重）
    public static List<Annotation> getAllAnnotationsDeep(AnnotatedElement element) {
        Objects.requireNonNull(element, "element must not be null");
        List<Annotation> result = new ArrayList<>();
        collectAnnotationsDeep(element, result);
        return result;
    }

    private static void collectAnnotationsDeep(AnnotatedElement element, List<Annotation> result) {
        for (Annotation ann : element.getAnnotations()) {
            if (!result.contains(ann)) {
                result.add(ann);
            }
            if (!ann.annotationType().getPackageName().startsWith("java.lang")) {
                collectAnnotationsDeep(ann.annotationType(), result);
            }
        }
    }

    // ==================== 注解属性读取 ====================

    /// 读取注解的指定属性值
    @SuppressWarnings("unchecked")
    public static <T> T getAnnotationValue(Annotation annotation, String attribute) {
        Objects.requireNonNull(annotation, "annotation must not be null");
        Objects.requireNonNull(attribute, "attribute must not be null");
        try {
            Method method = annotation.annotationType().getDeclaredMethod(attribute);
            method.setAccessible(true);
            return (T) method.invoke(annotation);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such attribute: " + attribute
                    + " in annotation " + annotation.annotationType().getName(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read annotation attribute: " + attribute, e);
        }
    }

    /// 读取注解的 value 属性（快捷方法）
    @SuppressWarnings("unchecked")
    public static <T> T getAnnotationValue(Annotation annotation) {
        return (T) getAnnotationValue(annotation, "value");
    }

    /// 读取注解属性值，不存在时返回默认值
    @SuppressWarnings("unchecked")
    public static <T> T getAnnotationValueOrDefault(Annotation annotation, String attribute, T defaultValue) {
        Objects.requireNonNull(annotation, "annotation must not be null");
        try {
            Method method = annotation.annotationType().getDeclaredMethod(attribute);
            return (T) method.invoke(annotation);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    // ==================== 类/字段/方法上的注解快捷方法 ====================

    /// 获取类上的注解
    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationType) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return getAnnotation((AnnotatedElement) clazz, annotationType);
    }

    /// 获取字段上的注解
    public static <A extends Annotation> A getAnnotation(Field field, Class<A> annotationType) {
        Objects.requireNonNull(field, "field must not be null");
        return getAnnotation((AnnotatedElement) field, annotationType);
    }

    /// 获取方法上的注解
    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
        Objects.requireNonNull(method, "method must not be null");
        return getAnnotation((AnnotatedElement) method, annotationType);
    }

    /// 获取构造器上的注解
    public static <A extends Annotation> A getAnnotation(Constructor<?> constructor, Class<A> annotationType) {
        Objects.requireNonNull(constructor, "constructor must not be null");
        return getAnnotation((AnnotatedElement) constructor, annotationType);
    }
}