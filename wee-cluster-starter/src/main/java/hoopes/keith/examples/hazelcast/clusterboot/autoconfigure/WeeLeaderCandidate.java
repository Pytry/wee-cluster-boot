package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure;

import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.DefaultCandidate;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
public class WeeLeaderCandidate extends DefaultCandidate{

    private final HazelcastEventDrivenMessageProducer producer;

    public WeeLeaderCandidate(final HazelcastEventDrivenMessageProducer producer){

        this.producer = producer;
    }

    @Override
    public void onGranted(final Context ctx){

        super.onGranted(ctx);
        producer.start();
    }

    @Override
    public void onRevoked(final Context ctx){

        super.onRevoked(ctx);
        producer.stop();
    }
}
