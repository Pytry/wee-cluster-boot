package org.xitikit.examples.hazelcast.simplecluster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.xitikit.examples.hazelcast.leader.autoconfigure.services.AllHereSentCache;
import org.xitikit.examples.hazelcast.leader.autoconfigure.services.AllHereSentService;

import static java.lang.Boolean.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.mockito.Mockito.*;

@TestInstance(PER_METHOD)
class AllHereSentServiceTest{

    private AllHereSentCache allHereSentCache;

    private AllHereSentService allHereSentService;

    @BeforeEach
    void init(){

        allHereSentCache = mock(AllHereSentCache.class);
        allHereSentService = new AllHereSentService(allHereSentCache);
    }

    @Test
    void isAllHereSentTrue(){

        doReturn(TRUE).when(allHereSentCache).get("isAllHereSent");
        assertTrue(allHereSentService.isAllHereSent());
        verify(allHereSentCache, times(1)).get(any(String.class));
    }

    @Test
    void isAllHereSentFalse(){

        doReturn(FALSE).when(allHereSentCache).get("isAllHereSent");
        assertFalse(allHereSentService.isAllHereSent());
        verify(allHereSentCache, atLeastOnce()).get("isAllHereSent");
        verify(allHereSentCache, atMost(2)).get("isAllHereSent");
    }

    @Test
    void setAllHereSentTrue(){

        allHereSentService.setAllHereSent(true);
        verify(allHereSentCache, atLeastOnce()).put("isAllHereSent", Boolean.TRUE);
        verify(allHereSentCache, atMost(1)).put("isAllHereSent", Boolean.TRUE);
    }

    @Test
    void setAllHereSentFalse(){

        allHereSentService.setAllHereSent(false);
        verify(allHereSentCache, atLeastOnce()).put("isAllHereSent", Boolean.FALSE);
        verify(allHereSentCache, atMost(1)).put("isAllHereSent", Boolean.FALSE);
    }
}