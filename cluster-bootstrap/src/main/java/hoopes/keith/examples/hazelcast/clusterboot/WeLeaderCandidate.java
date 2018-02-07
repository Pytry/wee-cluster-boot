package hoopes.keith.examples.hazelcast.clusterboot;

import org.springframework.beans.factory.annotation.Autowired;
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

    private final HazelcastEventDrivenMessageProducer hazelcastEventDrivenMessageProducer;

    @Autowired
    public WeLeaderCandidate(
        final HazelcastEventDrivenMessageProducer hazelcastEventDrivenMessageProducer){

        this.hazelcastEventDrivenMessageProducer = hazelcastEventDrivenMessageProducer;
    }

    @Override
    public void onGranted(final Context ctx){

        super.onGranted(ctx);
        hazelcastEventDrivenMessageProducer.start();
    }

    @Override
    public void onRevoked(final Context ctx){

        super.onRevoked(ctx);
        hazelcastEventDrivenMessageProducer.stop();
    }
}
