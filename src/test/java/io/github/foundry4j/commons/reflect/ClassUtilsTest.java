package io.github.foundry4j.commons.reflect;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassUtilsTest {

    @Test
    void testGetClassName() {
        assertEquals("io.github.foundry4j.commons.reflect.SampleBean",
                ClassUtils.getClassName(SampleBean.class));
    }

    @Test
    void testGetSimpleName() {
        assertEquals("SampleBean", ClassUtils.getSimpleName(SampleBean.class));
    }

    @Test
    void testGetPackageName() {
        assertEquals("io.github.foundry4j.commons.reflect",
                ClassUtils.getPackageName(SampleBean.class));
    }

    @Test
    void testIsPublic() {
        assertTrue(ClassUtils.isPublic(SampleBean.class));
    }

    @Test
    void testIsAbstract() {
        assertFalse(ClassUtils.isAbstract(SampleBean.class));
    }

    @Test
    void testIsInterface() {
        assertFalse(ClassUtils.isInterface(SampleBean.class));
        assertTrue(ClassUtils.isInterface(List.class));
    }

    @Test
    void testIsAssignable() {
        assertTrue(ClassUtils.isAssignable(SampleBean.class, ChildBean.class));
        assertFalse(ClassUtils.isAssignable(ChildBean.class, SampleBean.class));
    }

    @Test
    void testGetAllSuperclasses() {
        List<Class<?>> supers = ClassUtils.getAllSuperclasses(ChildBean.class);
        assertTrue(supers.contains(SampleBean.class));
        assertTrue(supers.contains(Object.class));
    }

    @Test
    void testGetAllInterfaces() {
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(SampleBean.class);
        assertNotNull(interfaces);
    }

    @Test
    void testForName() throws ClassNotFoundException {
        Class<?> clazz = ClassUtils.forName("io.github.foundry4j.commons.reflect.SampleBean");
        assertEquals(SampleBean.class, clazz);
    }

    @Test
    void testGetCanonicalName() {
        assertEquals("io.github.foundry4j.commons.reflect.SampleBean",
                ClassUtils.getCanonicalName(SampleBean.class));
    }

    @Test
    void testIsPrimitiveOrWrapper() {
        assertTrue(ClassUtils.isPrimitiveOrWrapper(int.class));
        assertTrue(ClassUtils.isPrimitiveOrWrapper(Integer.class));
        assertTrue(ClassUtils.isPrimitiveOrWrapper(boolean.class));
        assertTrue(ClassUtils.isPrimitiveOrWrapper(Boolean.class));
        assertFalse(ClassUtils.isPrimitiveOrWrapper(String.class));
    }

    @Test
    void testHasInterface() {
        // SampleBean 没有实现特殊接口，ChildBean 也没有
        assertFalse(ClassUtils.hasInterface(SampleBean.class, Runnable.class));
    }

    @Test
    void testHasSuperClass() {
        assertTrue(ClassUtils.hasSuperClass(ChildBean.class, SampleBean.class));
        assertTrue(ClassUtils.hasSuperClass(ChildBean.class, Object.class));
        assertFalse(ClassUtils.hasSuperClass(SampleBean.class, ChildBean.class));
    }

    @Test
    void testForNameOrNull() {
        Class<?> clazz = ClassUtils.forNameOrNull("io.github.foundry4j.commons.reflect.SampleBean");
        assertEquals(SampleBean.class, clazz);

        Class<?> notFound = ClassUtils.forNameOrNull("com.nonexistent.Foo");
        assertNull(notFound);
    }
}