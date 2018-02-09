package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootProperties;
import org.junit.jupiter.api.Test;

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
        WeeLeaderNotificationService weeLeaderNotificationService = mock(WeeLeaderNotificationService.class);
        ClusterBootProperties clusterBootProperties = new ClusterBootProperties(1, "224.0.0.1");
        WeClusterMemberStartedListener listener = new WeClusterMemberStartedListener(
            clusterBootProperties,
            weeLeaderNotificationService
        );

        listener.setHazelcastInstance(weHazelcastInstance);
        listener.stateChanged(lifecycleEventStarted());
        verify(
            weeLeaderNotificationService,
            atMost(1)
        ).notifyLeaderOfStartup(
            any(String.class)
        );
        verify(
            weeLeaderNotificationService,
            atLeastOnce()
        ).notifyLeaderOfStartup(
            any(String.class)
        );
    }
}