package io.github.foundry4j.commons.io;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IOUtilsTest {

    @Test
    void toByteArrayShouldReadAllBytes() {
        byte[] expected = "hello".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(expected);

        byte[] actual = IOUtils.toByteArray(inputStream);

        assertArrayEquals(expected, actual);
    }

    @Test
    void toUtf8StringShouldReturnUtf8Content() {
        InputStream inputStream = new ByteArrayInputStream("你好".getBytes(StandardCharsets.UTF_8));

        String result = IOUtils.toUtf8String(inputStream);

        assertEquals("你好", result);
    }

    @Test
    @Disabled
    void copyShouldWriteStreamToFile() throws IOException {
        byte[] data = "copy-test".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(data);

        Path target = Files.createTempFile("io-utils-test", ".txt");

        try {
            IOUtils.copy(inputStream, target);
            String content = Files.readString(target);
            assertEquals("copy-test", content);
        } finally {
            Files.deleteIfExists(target);
        }
    }

    @Test
    void toByteArrayShouldThrowWhenInputStreamIsNull() {
        assertThrows(NullPointerException.class, () -> IOUtils.toByteArray(null));
    }
}