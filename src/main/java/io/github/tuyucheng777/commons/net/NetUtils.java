package io.github.tuyucheng777.commons.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/// 网络工具类，提供本地端口检测等常用方法。
public final class NetUtils {

    private NetUtils() {
    }

    /// 检测本地端口是否可用。
    ///
    /// @param port 端口号
    /// @return 如果端口可用返回 true
    public static boolean isLocalPortAvailable(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port: " + port);
        }

        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress("127.0.0.1", port));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /// 检测本地端口是否已被占用。
    ///
    /// @param port 端口号
    /// @return 如果端口被占用返回 true
    public static boolean isLocalPortInUse(int port) {
        return !isLocalPortAvailable(port);
    }
}