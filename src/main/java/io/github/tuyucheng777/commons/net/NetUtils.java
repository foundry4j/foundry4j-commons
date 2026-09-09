package io.github.tuyucheng777.commons.net;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Objects;

/// 网络工具类，提供 IP 地址处理、端口检测、URL 操作等常用方法。
public final class NetUtils {

    private NetUtils() {
    }

    /// 判断本地指定端口是否可用。
    ///
    /// @param port 端口号
    /// @return 端口可用返回 true，否则返回 false
    public static boolean isLocalPortAvailable(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port: " + port);
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /// 判断本地指定端口是否被占用。
    ///
    /// @param port 端口号
    /// @return 端口被占用返回 true，否则返回 false
    public static boolean isLocalPortInUse(int port) {
        return !isLocalPortAvailable(port);
    }

    /// 获取本机局域网 IPv4 地址。
    ///
    /// @return 本机 IPv4 地址
    public static String getLocalIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

                    if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            throw new IllegalStateException("Failed to get local IP", e);
        }

        throw new IllegalStateException("No non-loopback IPv4 address found");
    }

    /// 判断是否为内网 IPv4 地址。
    ///
    /// @param ip IPv4 地址
    /// @return 是内网地址返回 true，否则返回 false
    public static boolean isInnerIp(String ip) {
        Objects.requireNonNull(ip, "ip must not be null");

        long ipLong = ipv4ToLong(ip);

        long tenStart = ipv4ToLong("10.0.0.0");
        long tenEnd = ipv4ToLong("10.255.255.255");

        long oneSevenTwoStart = ipv4ToLong("172.16.0.0");
        long oneSevenTwoEnd = ipv4ToLong("172.31.255.255");

        long oneNineTwoStart = ipv4ToLong("192.168.0.0");
        long oneNineTwoEnd = ipv4ToLong("192.168.255.255");

        return (ipLong >= tenStart && ipLong <= tenEnd)

                || (ipLong >= oneSevenTwoStart && ipLong <= oneSevenTwoEnd)
                || (ipLong >= oneNineTwoStart && ipLong <= oneNineTwoEnd);
    }

    /// 校验 IPv4 地址格式是否合法。
    ///
    /// @param ip IPv4 地址
    /// @return 合法返回 true，否则返回 false
    public static boolean isValidIpv4(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        String[] parts = ip.split("\\.", -1);

        if (parts.length != 4) {
            return false;
        }

        for (String part : parts) {
            if (part.isEmpty()) {
                return false;
            }

            try {
                int num = Integer.parseInt(part);

                if (num < 0 || num > 255) {
                    return false;
                }

                if (part.length() > 1 && part.startsWith("0")) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    /// 将 IPv4 地址转换为 long 数值。
    ///
    /// @param ip IPv4 地址
    /// @return long 数值
    public static long ipv4ToLong(String ip) {
        Objects.requireNonNull(ip, "ip must not be null");

        String[] parts = ip.split("\\.");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid IPv4: " + ip);
        }

        long result = 0;

        for (String part : parts) {
            int num = Integer.parseInt(part);

            if (num < 0 || num > 255) {
                throw new IllegalArgumentException("Invalid IPv4: " + ip);
            }

            result = (result << 8) | num;
        }

        return result;
    }

    /// 将 long 数值转换为 IPv4 地址。
    ///
    /// @param value long 数值
    /// @return IPv4 地址
    public static String longToIpv4(long value) {
        return ((value >> 24) & 0xFF) + "."
                + ((value >> 16) & 0xFF) + "."
                + ((value >> 8) & 0xFF) + "."
                + (value & 0xFF);
    }

    /// 对 IPv4 地址进行脱敏，将最后一段替换为星号。
    ///
    /// @param ip IPv4 地址
    /// @return 脱敏后的 IP 地址
    public static String hideIpPart(String ip) {
        Objects.requireNonNull(ip, "ip must not be null");

        int lastDot = ip.lastIndexOf('.');

        if (lastDot < 0) {
            throw new IllegalArgumentException("Invalid IPv4: " + ip);
        }

        return ip.substring(0, lastDot + 1) + "*";
    }

    /// 判断主机是否可达。
    ///
    /// @param host    主机名或 IP 地址
    /// @param timeout 超时时间，单位毫秒
    /// @return 可达返回 true，否则返回 false
    public static boolean isReachable(String host, int timeout) {
        Objects.requireNonNull(host, "host must not be null");

        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must not be negative");
        }

        try {
            InetAddress address = InetAddress.getByName(host);
            return address.isReachable(timeout);
        } catch (IOException e) {
            return false;
        }
    }

    /// 解析域名对应的 IP 地址。
    ///
    /// @param host 域名或主机名
    /// @return IP 地址
    public static String resolveHost(String host) {
        Objects.requireNonNull(host, "host must not be null");

        try {
            return InetAddress.getByName(host).getHostAddress();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to resolve host: " + host, e);
        }
    }

    /// 自动查找一个可用端口。
    ///
    /// @return 可用端口号
    public static int findAvailablePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            serverSocket.setReuseAddress(true);
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            throw new IllegalStateException("Could not find available port", e);
        }
    }

    /// 在指定范围内查找一个可用端口。
    ///
    /// @param from 起始端口，包含
    /// @param to   结束端口，包含
    /// @return 可用端口号
    public static int findAvailablePort(int from, int to) {
        if (from < 0 || to > 65535 || from > to) {
            throw new IllegalArgumentException("Invalid port range: " + from + "-" + to);
        }

        for (int port = from; port <= to; port++) {
            if (isLocalPortAvailable(port)) {
                return port;
            }
        }

        throw new IllegalStateException("No available port in range: " + from + "-" + to);
    }

    /// 将相对 URL 转换为绝对 URL。
    ///
    /// @param baseUrl     基础 URL
    /// @param relativeUrl 相对 URL
    /// @return 绝对 URL
    public static String toAbsoluteUrl(String baseUrl, String relativeUrl) {
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        Objects.requireNonNull(relativeUrl, "relativeUrl must not be null");

        try {
            URL base = new URL(baseUrl);
            URL resolved = new URL(base, relativeUrl);
            return resolved.toURI().toString();
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: baseUrl=" + baseUrl + ", relativeUrl=" + relativeUrl, e);
        }
    }

    /// 校验 URL 格式是否合法。
    ///
    /// @param url URL 字符串
    /// @return 合法返回 true，否则返回 false
    public static boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        try {
            new URL(url).toURI();
            return true;
        } catch (IOException | URISyntaxException e) {
            return false;
        }
    }

    /// 对字符串进行 URL 编码。
    ///
    /// @param value 原始字符串
    /// @return 编码后的字符串
    public static String urlEncode(String value) {
        Objects.requireNonNull(value, "value must not be null");

        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /// 对字符串进行 URL 解码。
    ///
    /// @param value 编码后的字符串
    /// @return 解码后的字符串
    public static String urlDecode(String value) {
        Objects.requireNonNull(value, "value must not be null");

        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}