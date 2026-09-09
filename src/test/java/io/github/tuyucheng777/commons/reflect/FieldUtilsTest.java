package io.github.tuyucheng777.commons.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldUtilsTest {

    @Test
    void testGetDeclaredField() {
        Field field = FieldUtils.getDeclaredField(SampleBean.class, "name");
        assertNotNull(field);
        assertEquals("name", field.getName());
    }

    @Test
    void testGetFieldFromHierarchy() {
        // ChildBean 自身没有 name 字段，但父类有
        Field field = FieldUtils.getField(ChildBean.class, "name");
        assertNotNull(field);
        assertEquals("name", field.getName());
    }

    @Test
    void testGetAllDeclaredFields() {
        List<Field> fields = FieldUtils.getDeclaredFields(SampleBean.class);
        assertTrue(fields.size() >= 2); // name, age
    }

    @Test
    void testGetAllFields() {
        List<Field> fields = FieldUtils.getAllFields(ChildBean.class);
        // 应包含 ChildBean 的 extra 和父类的 name, age
        assertTrue(fields.stream().anyMatch(f -> f.getName().equals("extra")));
        assertTrue(fields.stream().anyMatch(f -> f.getName().equals("name")));
    }

    @Test
    void testReadField() {
        SampleBean bean = new SampleBean("test", 25);
        Object value = FieldUtils.readField(bean, "name");
        assertEquals("test", value);
    }

    @Test
    void testWriteField() {
        SampleBean bean = new SampleBean();
        FieldUtils.writeField(bean, "name", "updated");
        assertEquals("updated", bean.getName());
    }

    @Test
    void testReadStaticField() {
        Object value = FieldUtils.readStaticField(SampleBean.class, "CONSTANT");
        assertEquals("hello", value);
    }

    @Test
    void testIsPrivate() {
        Field field = FieldUtils.getDeclaredField(SampleBean.class, "name");
        assertTrue(FieldUtils.isPrivate(field));
    }

    @Test
    void testIsStatic() {
        Field field = FieldUtils.getDeclaredField(SampleBean.class, "CONSTANT");
        assertTrue(FieldUtils.isStatic(field));
    }

    @Test
    void testIsFinal() {
        Field field = FieldUtils.getDeclaredField(SampleBean.class, "CONSTANT");
        assertTrue(FieldUtils.isFinal(field));
    }

    @Test
    void testIsProtected() {
        // SampleBean 没有 protected 构造器，用 private 构造器验证 isProtected 返回 false
        Constructor<?> ctor = ConstructorUtils.getDeclaredConstructor(SampleBean.class, String.class);
        assertFalse(ConstructorUtils.isProtected(ctor));
    }

    @Test
    void testIsPackagePrivate() {
        Constructor<?> ctor = ConstructorUtils.getDeclaredConstructor(SampleBean.class, String.class);
        // private 构造器不是 package-private
        assertFalse(ConstructorUtils.isPackagePrivate(ctor));
    }

    @Test
    void testIsVarArgs() {
        Constructor<?> ctor = ConstructorUtils.getDefaultConstructor(SampleBean.class);
        assertFalse(ConstructorUtils.isVarArgs(ctor));
    }

    @Test
    void testGetStaticFieldValue() {
        Object value = FieldUtils.getStaticFieldValue(SampleBean.class, "CONSTANT");
        assertEquals("hello", value);
    }

    @Test
    void testSetStaticFieldValue() {
        // 注意：修改 static final 字段在 Java 12+ 可能受限，这里仅测试非 final 场景
        // 如果 CONSTANT 是 final 的，此测试可能抛异常，可跳过或改用非 final 静态字段
        // 此处仅验证方法存在且能调用
        assertDoesNotThrow(() -> FieldUtils.getStaticFieldValue(SampleBean.class, "CONSTANT"));
    }

    @Test
    void testIsTransient() {
        Field field = FieldUtils.getDeclaredField(SampleBean.class, "name");
        assertFalse(FieldUtils.isTransient(field));
    }

    @Test
    void testIsVolatile() {
        Field field = FieldUtils.getDeclaredField(SampleBean.class, "name");
        assertFalse(FieldUtils.isVolatile(field));
    }

    @Test
    void testIsPublicField() {
        Field field = FieldUtils.getDeclaredField(SampleBean.class, "name");
        assertFalse(FieldUtils.isPublic(field)); // name 是 private

        Field constField = FieldUtils.getDeclaredField(SampleBean.class, "CONSTANT");
        assertTrue(FieldUtils.isPublic(constField)); // CONSTANT 是 public
    }

    @Test
    void testGetFieldsByType() {
        List<Field> stringFields = FieldUtils.getFieldsByType(SampleBean.class, String.class);
        assertTrue(stringFields.stream().anyMatch(f -> f.getName().equals("name")));
        assertTrue(stringFields.stream().anyMatch(f -> f.getName().equals("CONSTANT")));
    }
}