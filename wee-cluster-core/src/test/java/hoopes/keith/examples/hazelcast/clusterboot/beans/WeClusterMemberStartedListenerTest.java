package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.test.TestHazelcastInstanceFactory;
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
        ClusterBootProperties clusterBootProperties = new ClusterBootProperties(1, "224.0.0.1", "" +
            "A pirate walks into a bar with a huge mast wheel hanging from his belt buckle, and orders some rum.\n" +
            "The bartender asks him, \"It must be pretty difficult to get around with that wheel on your belt.\n" +
            "\"Yar,\" says the pirate. \"It be driving me nuts!");
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