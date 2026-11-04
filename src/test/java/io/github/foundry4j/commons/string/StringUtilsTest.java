package io.github.foundry4j.commons.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @Test
    void isEmptyShouldReturnTrueForNull() {
        assertTrue(StringUtils.isEmpty(null));
    }

    @Test
    void isEmptyShouldReturnTrueForEmptyString() {
        assertTrue(StringUtils.isEmpty(""));
    }

    @Test
    void isBlankShouldReturnTrueForWhitespace() {
        assertTrue(StringUtils.isBlank("   "));
    }

    @Test
    void isNotBlankShouldReturnTrueForNormalString() {
        assertTrue(StringUtils.isNotBlank("hello"));
    }

    @Test
    void truncateShouldCutLongString() {
        String result = StringUtils.truncate("hello world", 5, "...");

        assertEquals("hello...", result);
    }

    @Test
    void truncateShouldReturnOriginalWhenLengthIsEnough() {
        String result = StringUtils.truncate("hi", 5, "...");

        assertEquals("hi", result);
    }

    @Test
    void joinShouldCombineValues() {
        String result = StringUtils.join(",", "a", "b", "c");

        assertEquals("a,b,c", result);
    }

    @Test
    void defaultIfNullShouldReturnDefaultWhenNull() {
        assertEquals("default", StringUtils.defaultIfNull(null, "default"));
    }

    @Test
    void defaultIfNullShouldReturnOriginalWhenNotNull() {
        assertEquals("value", StringUtils.defaultIfNull("value", "default"));
    }
}