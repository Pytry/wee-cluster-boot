package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.core.IMap;
import com.hazelcast.map.impl.proxy.MapProxyImpl;
import hoopes.keith.examples.hazelcast.clusterboot.beans.LeaderClusterEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;

import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.mockito.Mockito.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@TestInstance(PER_CLASS)
class LeaderClusterEventServiceTest{

    @Test
    void notifyLeaderOfStartup(){

        IMap<String, String> mockMap = (MapProxyImpl<String, String>) mock(MapProxyImpl.class);
        LeaderClusterEventService service = new LeaderClusterEventService(mockMap);
        service.notifyLeaderOfStartup(UUID.randomUUID().toString());

        verify(mockMap, atLeastOnce())
            .put(any(), any());
        verify(mockMap, atMost(1))
            .put(any(), any());
    }
}