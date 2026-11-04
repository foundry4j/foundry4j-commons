package io.github.foundry4j.commons.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/// IO 工具类，提供流读取、资源读取、流复制等常用方法。
public final class IOUtils {

    private static final int BUFFER_SIZE = 8192;

    private IOUtils() {
    }

    /// 将输入流读取为字节数组。
    ///
    /// @param inputStream 输入流
    /// @return 读取到的字节数组
    /// @throws UncheckedIOException 如果读取过程中发生 IO 异常
    public static byte[] toByteArray(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream must not be null");

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }

            return output.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /// 将输入流读取为字符串。
    ///
    /// @param inputStream 输入流
    /// @param charset     字符集
    /// @return 读取到的字符串
    public static String toString(InputStream inputStream, Charset charset) {
        Objects.requireNonNull(charset, "charset must not be null");
        return new String(toByteArray(inputStream), charset);
    }

    /// 使用 UTF-8 将输入流读取为字符串。
    ///
    /// @param inputStream 输入流
    /// @return 读取到的字符串
    public static String toUtf8String(InputStream inputStream) {
        return toString(inputStream, StandardCharsets.UTF_8);
    }

    /// 读取 classpath 下的资源文件内容为字符串。
    ///
    /// @param resourcePath 资源路径
    /// @return 资源内容
    public static String readResourceAsString(String resourcePath) {
        Objects.requireNonNull(resourcePath, "resourcePath must not be null");

        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }

            return toUtf8String(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /// 复制输入流到目标路径。
    ///
    /// @param inputStream 输入流
    /// @param target      目标文件路径
    /// @throws IOException 如果写入失败
    public static void copy(InputStream inputStream, Path target) throws IOException {
        Objects.requireNonNull(inputStream, "inputStream must not be null");
        Objects.requireNonNull(target, "target must not be null");

        Files.copy(inputStream, target);
    }
}