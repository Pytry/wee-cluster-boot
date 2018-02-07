package hoopes.keith.examples.hazelcast.simplecluster;

import hoopes.keith.examples.hazelcast.clusterboot.WeAreStartedStatusCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
class WeAreStartedStatusCacheTest{

    @Test
    void isWeAreStarted(){

        WeAreStartedStatusCache cache = new WeAreStartedStatusCache();
        assertFalse(cache.isWeAreStarted());
        cache.setWeAreStarted(true);
        assertTrue(cache.isWeAreStarted());
    }
}