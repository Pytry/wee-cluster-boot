package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.core.IMap;

public class LeaderClusterEventService{

    private final IMap<String, String> events;

    public LeaderClusterEventService(final IMap<String, String> startupEventMap){

        if(startupEventMap == null){
            throw new IllegalArgumentException("'startupEventMap' is required.");
        }
        this.events = startupEventMap;
    }

    public void notifyLeaderOfStartup(final String nodeUuid){

        events.put("nodeStartupNotificationForLeader", nodeUuid);
    }
}
