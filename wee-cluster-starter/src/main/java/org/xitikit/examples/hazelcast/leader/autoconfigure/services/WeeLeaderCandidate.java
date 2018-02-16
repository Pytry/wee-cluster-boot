package org.xitikit.examples.hazelcast.leader.autoconfigure.services;

import com.hazelcast.core.IMap;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.DefaultCandidate;

/**
 * The custom leader candidate for the Wee Cluster.
 * This class is not annotated because I did not
 * want to have to use @{@link org.springframework.beans.factory.annotation.Autowired}
 * when creating the initiator bean. Instead, this
 * is created manually in the configuration with
 * a bean factory method
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
public class WeeLeaderCandidate extends DefaultCandidate{

    private final HazelcastEventDrivenMessageProducer producer;

    private final IMap<String, String> allHereJobMap;

    private final ClusterBootProperties clusterBootProperties;

    public WeeLeaderCandidate(
        final HazelcastEventDrivenMessageProducer producer,
        final IMap<String, String> allHereJobMap,
        final ClusterBootProperties clusterBootProperties){

        this.producer = producer;
        this.allHereJobMap = allHereJobMap;
        this.clusterBootProperties = clusterBootProperties;
    }

    @Override
    public void onGranted(final Context ctx){

        super.onGranted(ctx);
        producer.start();
        allHereJobMap.put(
            "CheckAllHereMessage",
            clusterBootProperties.getAllHereMessage()
        );
    }

    @Override
    public void onRevoked(final Context ctx){

        super.onRevoked(ctx);
        producer.stop();
    }
}
