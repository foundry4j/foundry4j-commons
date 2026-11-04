package io.github.foundry4j.commons.reflect;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationUtilsTest {

    @Test
    void testHasAnnotation() {
        assertTrue(AnnotationUtils.hasAnnotation(AnnotatedTarget.class, MetaAnnotatedBean.class));
        assertFalse(AnnotationUtils.hasAnnotation(AnnotatedTarget.class, TestAnnotation.class));
    }

    @Test
    void testHasAnnotationDeep() {
        // AnnotatedTarget 上有 @MetaAnnotatedBean，而 @MetaAnnotatedBean 上有 @TestAnnotation
        assertTrue(AnnotationUtils.hasAnnotationDeep(AnnotatedTarget.class, TestAnnotation.class));
    }

    @Test
    void testGetAnnotation() {
        MetaAnnotatedBean ann = AnnotationUtils.getAnnotation(AnnotatedTarget.class, MetaAnnotatedBean.class);
        assertNotNull(ann);
    }

    @Test
    void testFindAnnotationDeep() {
        TestAnnotation ann = AnnotationUtils.findAnnotationDeep(AnnotatedTarget.class, TestAnnotation.class);
        assertNotNull(ann);
        assertEquals("meta", ann.value());
        assertEquals(5, ann.priority());
    }

    @Test
    void testGetAllAnnotationsDeep() {
        List<Annotation> annotations = AnnotationUtils.getAllAnnotationsDeep(AnnotatedTarget.class);
        assertFalse(annotations.isEmpty());
    }

    @Test
    void testGetAnnotationValue() {
        TestAnnotation ann = AnnotationUtils.findAnnotationDeep(AnnotatedTarget.class, TestAnnotation.class);
        String value = AnnotationUtils.getAnnotationValue(ann, "value");
        assertEquals("meta", value);
    }

    @Test
    void testGetAnnotationValueShortcut() {
        TestAnnotation ann = AnnotationUtils.findAnnotationDeep(AnnotatedTarget.class, TestAnnotation.class);
        String value = AnnotationUtils.getAnnotationValue(ann);
        assertEquals("meta", value);
    }

    @Test
    void testGetAnnotationValueOrDefault() {
        TestAnnotation ann = AnnotationUtils.findAnnotationDeep(AnnotatedTarget.class, TestAnnotation.class);
        String value = AnnotationUtils.getAnnotationValueOrDefault(ann, "nonExistent", "default");
        assertEquals("default", value);
    }

    @Test
    void testGetAnnotationOnField() throws NoSuchFieldException {
        Field field = SampleBean.class.getDeclaredField("name");
        TestAnnotation ann = AnnotationUtils.getAnnotation(field, TestAnnotation.class);
        assertNull(ann);
    }

    @Test
    void testGetAnnotationOnMethod() throws NoSuchMethodException {
        Method method = SampleBean.class.getDeclaredMethod("getName");
        TestAnnotation ann = AnnotationUtils.getAnnotation(method, TestAnnotation.class);
        assertNull(ann);
    }

    @Test
    void testGetAnnotationOnConstructor() throws NoSuchMethodException {
        Constructor<?> ctor = SampleBean.class.getDeclaredConstructor();
        TestAnnotation ann = AnnotationUtils.getAnnotation(ctor, TestAnnotation.class);
        assertNull(ann);
    }
}