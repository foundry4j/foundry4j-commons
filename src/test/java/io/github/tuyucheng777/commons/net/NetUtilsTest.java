package io.github.tuyucheng777.commons.net;

import org.junit.jupiter.api.Test;

import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

class NetUtilsTest {

    @Test
    void isLocalPortAvailableShouldDetectInvalidPort() {
        assertThrows(IllegalArgumentException.class, () -> NetUtils.isLocalPortAvailable(-1));
        assertThrows(IllegalArgumentException.class, () -> NetUtils.isLocalPortAvailable(70000));
    }

    @Test
    void isLocalPortInUseShouldBeOppositeOfAvailable() {
        int port = 0;

        boolean available = NetUtils.isLocalPortAvailable(port);
        boolean inUse = NetUtils.isLocalPortInUse(port);

        assertNotEquals(available, inUse);
    }

    @Test
    void isLocalPortAvailableShouldReturnFalseForBusyPort() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            assertFalse(NetUtils.isLocalPortAvailable(port));
            assertTrue(NetUtils.isLocalPortInUse(port));
        }
    }

    @Test
    void getLocalIpShouldReturnIpv4Address() {
        String ip = NetUtils.getLocalIp();

        assertNotNull(ip);
        assertTrue(NetUtils.isValidIpv4(ip));
    }

    @Test
    void isInnerIpShouldDetectPrivateAddresses() {
        assertTrue(NetUtils.isInnerIp("10.0.0.1"));
        assertTrue(NetUtils.isInnerIp("172.16.0.1"));
        assertTrue(NetUtils.isInnerIp("192.168.1.1"));

        assertFalse(NetUtils.isInnerIp("8.8.8.8"));
    }

    @Test
    void isValidIpv4ShouldValidateFormat() {
        assertTrue(NetUtils.isValidIpv4("127.0.0.1"));
        assertTrue(NetUtils.isValidIpv4("192.168.1.1"));

        assertFalse(NetUtils.isValidIpv4(null));
        assertFalse(NetUtils.isValidIpv4(""));
        assertFalse(NetUtils.isValidIpv4("256.0.0.1"));
        assertFalse(NetUtils.isValidIpv4("1.2.3"));
        assertFalse(NetUtils.isValidIpv4("01.2.3.4"));
    }

    @Test
    void ipv4ToLongAndLongToIpv4ShouldConvertCorrectly() {
        String ip = "192.168.1.100";

        long value = NetUtils.ipv4ToLong(ip);
        String restored = NetUtils.longToIpv4(value);

        assertEquals(ip, restored);
    }

    @Test
    void ipv4ToLongShouldThrowForInvalidIp() {
        assertThrows(IllegalArgumentException.class, () -> NetUtils.ipv4ToLong("1.2.3"));
    }

    @Test
    void hideIpPartShouldMaskLastSegment() {
        assertEquals("192.168.1.*", NetUtils.hideIpPart("192.168.1.100"));
    }

    @Test
    void hideIpPartShouldThrowForInvalidIp() {
        assertThrows(IllegalArgumentException.class, () -> NetUtils.hideIpPart("invalid"));
    }

    @Test
    void isReachableShouldReturnFalseForInvalidHost() {
        assertFalse(NetUtils.isReachable("invalid-host-should-not-exist-12345", 500));
    }

    @Test
    void isReachableShouldThrowWhenTimeoutIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> NetUtils.isReachable("127.0.0.1", -1));
    }

    @Test
    void resolveHostShouldResolveLocalhost() {
        String ip = NetUtils.resolveHost("127.0.0.1");

        assertEquals("127.0.0.1", ip);
    }

    @Test
    void findAvailablePortShouldReturnValidPort() {
        int port = NetUtils.findAvailablePort();

        assertTrue(port >= 1 && port <= 65535);
        assertTrue(NetUtils.isLocalPortAvailable(port));
    }

    @Test
    void findAvailablePortInRangeShouldReturnValidPort() {
        int port = NetUtils.findAvailablePort(10000, 60000);

        assertTrue(port >= 10000 && port <= 60000);
        assertTrue(NetUtils.isLocalPortAvailable(port));
    }

    @Test
    void findAvailablePortInRangeShouldThrowForInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> NetUtils.findAvailablePort(60000, 10000));
    }

    @Test
    void toAbsoluteUrlShouldResolveRelativeUrl() {
        String result = NetUtils.toAbsoluteUrl("https://example.com/a/", "b.html");

        assertEquals("https://example.com/a/b.html", result);
    }

    @Test
    void toAbsoluteUrlShouldThrowForInvalidUrl() {
        assertThrows(IllegalArgumentException.class, () -> NetUtils.toAbsoluteUrl("not-a-url", "path"));
    }

    @Test
    void isValidUrlShouldValidateUrlFormat() {
        assertTrue(NetUtils.isValidUrl("https://example.com"));
        assertTrue(NetUtils.isValidUrl("http://localhost:8080/path"));

        assertFalse(NetUtils.isValidUrl(null));
        assertFalse(NetUtils.isValidUrl(""));
        assertFalse(NetUtils.isValidUrl("not a url"));
    }

    @Test
    void urlEncodeAndUrlDecodeShouldBeReversible() {
        String value = "hello world&name=测试";

        String encoded = NetUtils.urlEncode(value);
        String decoded = NetUtils.urlDecode(encoded);

        assertEquals(value, decoded);
        assertNotEquals(value, encoded);
    }
}