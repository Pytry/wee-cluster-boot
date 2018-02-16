package org.xitikit.examples.hazelcast.simplecluster;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xitikit.examples.hazelcast.leader.autoconfigure.services.AllHereSentCache;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@Disabled("" +
    "The multi threaded startup is causing some race " +
    "conditions on setting the value of the cache. A " +
    "different approach for integration testing the " +
    "cache is needed.")
@ExtendWith(SpringExtension.class)
@Configuration
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = WeeClusterBootApplication.class)
@TestInstance(PER_CLASS)
class AllHereSentCacheTest{

    @Autowired
    private AllHereSentCache allHereSentCache;

    @Test
    @Disabled("This test needs to be able to handle race conditions.")
    void defaultValuesFalse(){

        assertTrue(allHereSentCache.get("isAllHereSent"));//values save

        allHereSentCache.put("isAllHereSent", Boolean.FALSE);
        assertFalse(allHereSentCache.get("isAllHereSent"));//can change to FALSE

        allHereSentCache.put("isAllHereSent", Boolean.TRUE);
        assertTrue(allHereSentCache.get("isAllHereSent"));//can change to FALSE

        allHereSentCache.put("isAllHereSent", null);
        assertFalse(allHereSentCache.get("isAllHereSent"));//default to false if value is null
    }
}