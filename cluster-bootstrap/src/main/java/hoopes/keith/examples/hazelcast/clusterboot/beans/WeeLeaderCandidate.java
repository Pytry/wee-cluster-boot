package hoopes.keith.examples.hazelcast.clusterboot.beans;

import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.DefaultCandidate;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
public class WeeLeaderCandidate extends DefaultCandidate{

    private final HazelcastEventDrivenMessageProducer hazelcastEventDrivenMessageProducer;

    public WeeLeaderCandidate(final HazelcastEventDrivenMessageProducer hazelcastEventDrivenMessageProducer){

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
