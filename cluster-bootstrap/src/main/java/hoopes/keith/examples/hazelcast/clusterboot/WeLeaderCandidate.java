package hoopes.keith.examples.hazelcast.clusterboot;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.stereotype.Component;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Component("weLeaderCandidate")
public class WeLeaderCandidate extends DefaultCandidate{

    private final WeAreStartedStatusCache weAreStartedStatusCache;

    private final WeAreStartedProperties weAreStartedProperties;

    private final HazelcastInstance hazelcastInstance;

    private final HazelcastEventDrivenMessageProducer hazelcastEventDrivenMessageProducer;

    @Autowired
    public WeLeaderCandidate(
        final WeAreStartedStatusCache weAreStartedStatusCache,
        final WeAreStartedProperties weAreStartedProperties,
        final HazelcastEventDrivenMessageProducer hazelcastEventDrivenMessageProducer,
        @Qualifier("weHazelcastInstance") final HazelcastInstance hazelcastInstance){

        this.weAreStartedStatusCache = weAreStartedStatusCache;
        this.weAreStartedProperties = weAreStartedProperties;
        this.hazelcastEventDrivenMessageProducer = hazelcastEventDrivenMessageProducer;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void onGranted(final Context ctx){

        super.onGranted(ctx);
        if(!weAreStartedStatusCache.isWeAreStarted() &&
            hazelcastInstance
                .getCluster()
                .getMembers()
                .size() == weAreStartedProperties.getMaxNodes()){

            weAreStartedStatusCache.setWeAreStarted(true);
            System.out.println("We are started!");
        }
        hazelcastEventDrivenMessageProducer.start();
    }

    @Override
    public void onRevoked(final Context ctx){

        super.onRevoked(ctx);
        hazelcastEventDrivenMessageProducer.stop();
    }
}
