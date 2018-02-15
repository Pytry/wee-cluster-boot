package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

import com.hazelcast.core.IMap;
import org.junit.jupiter.api.Test;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.Context;

import static org.mockito.Mockito.*;

/**
 * Unfortunately, we cannot verify that "start" nor "stop" were called
 * since those methods are final. This test only verifies whether or not
 * and event and message was registered, and that no messages are
 * created when leadership is revoked.
 */
@SuppressWarnings("unchecked")
class WeeLeaderCandidateTest{

    @Test
    void onGranted(){

        IMap<String, String> allHereJobMap = (IMap<String, String>) mock(IMap.class);
        HazelcastEventDrivenMessageProducer producer = spy(new HazelcastEventDrivenMessageProducer(allHereJobMap));
        ClusterBootProperties clusterBootProperties = new ClusterBootProperties();
        Context context = mock(Context.class);
        WeeLeaderCandidate candidate = new WeeLeaderCandidate(
            producer,
            allHereJobMap,
            clusterBootProperties
        );
        candidate.onGranted(context);
        // Note: cannot verify "start" nor "stop" since those methods are final
        verify(allHereJobMap, times(1))
            .put("CheckAllHereMessage", "All Here");
    }

    @Test
    void onRevoked(){

        IMap<String, String> allHereJobMap = (IMap<String, String>) mock(IMap.class);
        HazelcastEventDrivenMessageProducer producer = spy(new HazelcastEventDrivenMessageProducer(allHereJobMap));
        ClusterBootProperties clusterBootProperties = new ClusterBootProperties();
        Context context = mock(Context.class);
        WeeLeaderCandidate candidate = new WeeLeaderCandidate(
            producer,
            allHereJobMap,
            clusterBootProperties
        );
        candidate.onRevoked(context);
        verify(allHereJobMap, never()).put(any(), any());
    }
}