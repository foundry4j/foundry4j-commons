package io.github.tuyucheng777.commons.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReflectUtilsTest {

    static class Sample {
        private String name = "java";

        private String secret() {
            return "secret-value";
        }
    }

    @Test
    void getFieldValueShouldReturnPrivateFieldValue() {
        Sample sample = new Sample();

        Object value = ReflectUtils.getFieldValue(sample, "name");

        assertEquals("java", value);
    }

    @Test
    void setFieldValueShouldUpdatePrivateField() {
        Sample sample = new Sample();

        ReflectUtils.setFieldValue(sample, "name", "updated");

        assertEquals("updated", ReflectUtils.getFieldValue(sample, "name"));
    }

    @Test
    void invokeMethodShouldCallPrivateMethod() {
        Sample sample = new Sample();

        Object result = ReflectUtils.invokeMethod(sample, "secret");

        assertEquals("secret-value", result);
    }

    @Test
    void getAllDeclaredFieldsShouldReturnFields() {
        List<Field> fields = ReflectUtils.getAllDeclaredFields(Sample.class);

        assertFalse(fields.isEmpty());
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("name")));
    }

    @Test
    void getFieldValueShouldThrowWhenFieldNotFound() {
        Sample sample = new Sample();

        assertThrows(IllegalStateException.class, () -> ReflectUtils.getFieldValue(sample, "notExist"));
    }
}