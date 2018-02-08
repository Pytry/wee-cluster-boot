package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootProperties;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
public class WeClusterMemberStartedListener implements HazelcastInstanceAware, LifecycleListener{

    private HazelcastInstance weHazelcastInstance;

    private final LeaderClusterEventService events;

    private final ClusterBootProperties props;

    public WeClusterMemberStartedListener(
        final ClusterBootProperties clusterBootProperties,
        final LeaderClusterEventService leaderClusterEventService){

        this.props = clusterBootProperties;
        this.events = leaderClusterEventService;
    }

    @Override
    public void stateChanged(final LifecycleEvent event){

        if(memberStarted(event) && maxNodesReached()){

            events.notifyLeaderOfStartup(
                weHazelcastInstance
                    .getCluster()
                    .getLocalMember()
                    .getUuid()
            );
        }
    }

    private static boolean memberStarted(final LifecycleEvent event){

        return event != null && event.getState() == LifecycleEvent.LifecycleState.STARTED;
    }

    private boolean maxNodesReached(){

        return weHazelcastInstance != null && props.getMaxNodes() == currentSize();
    }

    private int currentSize(){

        assert weHazelcastInstance != null;

        return weHazelcastInstance
            .getCluster()
            .getMembers()
            .size();
    }

    @Override
    public void setHazelcastInstance(final HazelcastInstance weHazelcastInstance){

        this.weHazelcastInstance = weHazelcastInstance;
    }
}
