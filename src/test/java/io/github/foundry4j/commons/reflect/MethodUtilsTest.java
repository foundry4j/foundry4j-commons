package io.github.foundry4j.commons.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MethodUtilsTest {

    @Test
    void testGetDeclaredMethod() {
        Method method = MethodUtils.getDeclaredMethod(SampleBean.class, "secretMethod");
        assertNotNull(method);
        assertEquals("secretMethod", method.getName());
    }

    @Test
    void testGetMethodFromHierarchy() {
        Method method = MethodUtils.getMethod(ChildBean.class, "secretMethod");
        assertNotNull(method);
    }

    @Test
    void testGetAllMethods() {
        List<Method> methods = MethodUtils.getAllMethods(ChildBean.class);
        assertTrue(methods.stream().anyMatch(m -> m.getName().equals("childOnly")));
        assertTrue(methods.stream().anyMatch(m -> m.getName().equals("secretMethod")));
    }

    @Test
    void testGetMethodsByName() {
        List<Method> methods = MethodUtils.getMethodsByName(SampleBean.class, "getName");
        assertEquals(1, methods.size());
    }

    @Test
    void testInvokeInstanceMethod() {
        SampleBean bean = new SampleBean("hello", 10);
        Object result = MethodUtils.invoke(bean, "secretMethod");
        assertEquals("secret", result);
    }

    @Test
    void testInvokeMethodWithArgs() {
        SampleBean bean = new SampleBean();
        Object result = MethodUtils.invoke(bean, "concat", "foo", "bar");
        assertEquals("foobar", result);
    }

    @Test
    void testInvokeStaticMethod() {
        Object result = MethodUtils.invokeStatic(SampleBean.class, "staticMethod", "world");
        assertEquals("static:world", result);
    }

    @Test
    void testIsGetter() {
        Method getter = MethodUtils.getDeclaredMethod(SampleBean.class, "getName");
        assertTrue(MethodUtils.isGetter(getter));
    }

    @Test
    void testIsSetter() {
        Method setter = MethodUtils.getDeclaredMethod(SampleBean.class, "setName", String.class);
        assertTrue(MethodUtils.isSetter(setter));
    }

    @Test
    void testIsStatic() {
        Method method = MethodUtils.getDeclaredMethod(SampleBean.class, "staticMethod", String.class);
        assertTrue(MethodUtils.isStatic(method));
    }

    @Test
    void testIsPrivate() {
        Method method = MethodUtils.getDeclaredMethod(SampleBean.class, "secretMethod");
        assertTrue(MethodUtils.isPrivate(method));
    }

    @Test
    void testIsStaticMethod() {
        Method method = MethodUtils.getDeclaredMethod(SampleBean.class, "staticMethod", String.class);
        assertTrue(MethodUtils.isStatic(method));
    }

    @Test
    void testIsAbstractMethod() {
        Method method = MethodUtils.getDeclaredMethod(SampleBean.class, "getName");
        assertFalse(MethodUtils.isAbstract(method));
    }

    @Test
    void testIsFinalMethod() {
        Method method = MethodUtils.getDeclaredMethod(SampleBean.class, "getName");
        assertFalse(MethodUtils.isFinal(method));
    }

    @Test
    void testIsSynchronizedMethod() {
        Method method = MethodUtils.getDeclaredMethod(SampleBean.class, "getName");
        assertFalse(MethodUtils.isSynchronized(method));
    }

    @Test
    void testIsPublicMethod() {
        Method method = MethodUtils.getDeclaredMethod(SampleBean.class, "getName");
        assertTrue(MethodUtils.isPublic(method));

        Method privateMethod = MethodUtils.getDeclaredMethod(SampleBean.class, "secretMethod");
        assertFalse(MethodUtils.isPublic(privateMethod));
    }
}