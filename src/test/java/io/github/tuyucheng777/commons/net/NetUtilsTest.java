package io.github.tuyucheng777.commons.net;

import org.junit.jupiter.api.Test;

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
}