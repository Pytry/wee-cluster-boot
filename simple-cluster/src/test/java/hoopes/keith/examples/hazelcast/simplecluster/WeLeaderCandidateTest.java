package hoopes.keith.examples.hazelcast.simplecluster;

import com.hazelcast.core.Cluster;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import hoopes.keith.examples.hazelcast.simplecluster.cluster.WeAreStartedStatusCache;
import hoopes.keith.examples.hazelcast.simplecluster.cluster.WeLeaderCandidate;
import org.junit.jupiter.api.Test;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.Context;

import java.util.HashSet;

import static org.mockito.Mockito.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
class WeLeaderCandidateTest{

    @SuppressWarnings("unchecked")
    @Test
    void onGranted(){

        WeAreStartedStatusCache cache = mock(WeAreStartedStatusCache.class);
        WeAreStartedProperties weAreStartedProperties = mock(WeAreStartedProperties.class);
        HazelcastEventDrivenMessageProducer producer = mock(HazelcastEventDrivenMessageProducer.class);
        HazelcastInstance instance = mock(HazelcastInstance.class);
        Context context = mock(Context.class);
        Cluster cluster = mock(Cluster.class);
        HashSet<Member> members = (HashSet<Member>) mock(HashSet.class);

        when(cache.isWeAreStarted()).thenReturn(false);
        when(instance.getCluster()).thenReturn(cluster);
        when(cluster.getMembers()).thenReturn(members);
        when(members.size()).thenReturn(10);

        WeLeaderCandidate can = new WeLeaderCandidate(
            cache, weAreStartedProperties, producer, instance
        );

        can.onGranted(context);

        verify(
            cache,
            times(1)
        ).setWeAreStarted(true);
    }

    @Test
    void onRevoked(){

    }
}