package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.core.IMap;

import static com.hazelcast.core.LifecycleEvent.LifecycleState.*;

public class WeeLeaderNotificationService{

    private final IMap<String, String> jobs;

    public WeeLeaderNotificationService(IMap<String, String> jobs){

        if(jobs == null){
            throw new IllegalArgumentException("'jobs' is required.");
        }

        this.jobs = jobs;
    }

    public void notifyLeaderOfStartup(final String message){

        jobs.put(STARTED.toString(), message);
    }

}
