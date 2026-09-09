package io.github.tuyucheng777.commons.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConstructorUtilsTest {

    @Test
    void testGetDefaultConstructor() {
        Constructor<?> ctor = ConstructorUtils.getDefaultConstructor(SampleBean.class);
        assertNotNull(ctor);
        assertEquals(0, ctor.getParameterCount());
    }

    @Test
    void testGetDeclaredConstructor() {
        Constructor<?> ctor = ConstructorUtils.getDeclaredConstructor(SampleBean.class, String.class);
        assertNotNull(ctor);
        assertEquals(1, ctor.getParameterCount());
    }

    @Test
    void testGetDeclaredConstructors() {
        List<Constructor<?>> ctors = ConstructorUtils.getDeclaredConstructors(SampleBean.class);
        assertEquals(3, ctors.size()); // 无参、单参private、双参public
    }

    @Test
    void testGetPublicConstructors() {
        List<Constructor<?>> ctors = ConstructorUtils.getPublicConstructors(SampleBean.class);
        // 只有无参和双参是 public
        assertEquals(2, ctors.size());
    }

    @Test
    void testGetConstructorsByParamCount() {
        List<Constructor<?>> ctors = ConstructorUtils.getConstructorsByParamCount(SampleBean.class, 2);
        assertEquals(1, ctors.size());
    }

    @Test
    void testNewInstanceNoArgs() {
        SampleBean bean = ConstructorUtils.newInstance(SampleBean.class);
        assertNotNull(bean);
    }

    @Test
    void testNewInstanceWithArgs() {
        SampleBean bean = ConstructorUtils.newInstance(SampleBean.class, "test", 30);
        assertNotNull(bean);
        assertEquals("test", bean.getName());
        assertEquals(30, bean.getAge());
    }

    @Test
    void testNewInstancePrivateConstructor() {
        // 通过指定参数类型调用 private 构造器
        SampleBean bean = ConstructorUtils.newInstance(SampleBean.class,
                new Class<?>[]{String.class}, "private");
        assertNotNull(bean);
        assertEquals("private", bean.getName());
    }

    @Test
    void testIsPublic() {
        Constructor<?> ctor = ConstructorUtils.getDeclaredConstructor(SampleBean.class);
        assertTrue(ConstructorUtils.isPublic(ctor));
    }

    @Test
    void testIsPrivate() {
        Constructor<?> ctor = ConstructorUtils.getDeclaredConstructor(SampleBean.class, String.class);
        assertTrue(ConstructorUtils.isPrivate(ctor));
    }

    @Test
    void testGetParameterTypes() {
        Constructor<?> ctor = ConstructorUtils.getDeclaredConstructor(SampleBean.class, String.class, int.class);
        List<Class<?>> types = ConstructorUtils.getParameterTypes(ctor);
        assertEquals(2, types.size());
        assertEquals(String.class, types.get(0));
        assertEquals(int.class, types.get(1));
    }
}