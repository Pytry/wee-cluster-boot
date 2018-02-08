package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
class WeClusterMemberStartedListenerTest{

    private static LifecycleEvent lifecycleEventStarted(){

        return new LifecycleEvent(LifecycleEvent.LifecycleState.STARTED);
    }

    @Test
    void stateChanged(){

        HazelcastInstance weHazelcastInstance = new TestHazelcastInstanceFactory().newHazelcastInstance();

        LeaderClusterEventService leaderClusterEventService = mock(LeaderClusterEventService.class);
        ClusterBootProperties clusterBootProperties = new ClusterBootProperties(1);
        WeClusterMemberStartedListener listener = new WeClusterMemberStartedListener(
            clusterBootProperties,
            leaderClusterEventService
        );

        listener.setHazelcastInstance(weHazelcastInstance);
        listener.stateChanged(lifecycleEventStarted());
        verify(
            leaderClusterEventService,
            atMost(1)
        ).notifyLeaderOfStartup(
            any(String.class)
        );
        verify(
            leaderClusterEventService,
            atLeastOnce()
        ).notifyLeaderOfStartup(
            any(String.class)
        );
    }
}