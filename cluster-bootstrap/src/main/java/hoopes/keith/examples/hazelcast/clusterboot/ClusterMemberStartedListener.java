package hoopes.keith.examples.hazelcast.clusterboot;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;

import static java.util.Optional.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
public class ClusterMemberStartedListener implements HazelcastInstanceAware, LifecycleListener{

    private HazelcastInstance weHazelcastInstance;

    @Override
    public void stateChanged(final LifecycleEvent event){

        if(memberStarted(event) && maxNodesReached()){

            System.out.println("We are started!");
        }
    }

    private static boolean memberStarted(final LifecycleEvent event){

        return event != null && event.getState() == LifecycleEvent.LifecycleState.STARTED;
    }

    private boolean maxNodesReached(){

        Integer maxNodes =
            ofNullable(
                ofNullable(weHazelcastInstance.getConfig())
                    .orElse(new Config())
                    .getMemberAttributeConfig()
                    .getIntAttribute("maxNodes"))
                .orElse(-1);
        int size = weHazelcastInstance
            .getCluster()
            .getMembers()
            .size();

        return size == maxNodes;
    }

    @Override
    public void setHazelcastInstance(final HazelcastInstance weHazelcastInstance){

        this.weHazelcastInstance = weHazelcastInstance;
    }
}
