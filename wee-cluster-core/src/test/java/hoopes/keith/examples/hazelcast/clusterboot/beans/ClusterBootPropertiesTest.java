package hoopes.keith.examples.hazelcast.clusterboot.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
class ClusterBootPropertiesTest{

    /**
     * Make sure the expected defaults are present.
     */
    @Test
    void getMaxNodes(){

        ClusterBootProperties p = new ClusterBootProperties();

        assertSame(10, p.getMaxNodes());
        assertSame("All Here", p.getAllHereMessage());
        assertSame("224.0.0.1", p.getMulticastGroup());
    }
}