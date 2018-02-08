package hoopes.keith.examples.hazelcast.clusterboot.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
class StartupEventMapProxyTest{

    @Test
    void putAndGet(){
        // Only the basic "put" and "get" methods are proxied
        StartupEventMapProxy proxy = new StartupEventMapProxy("StartupEventMapProxyTest");
        proxy.put("test-key", "test-message");
        assertEquals("test-message", proxy.get("test-key"));
        assertThrows(
            IllegalStateException.class,
            proxy::clear
        );
    }
}