package org.xitikit.examples.hazelcast.simplecluster;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.event.LeaderEventPublisher;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xitikit.examples.hazelcast.leader.autoconfigure.services.ClusterBootProperties;
import org.xitikit.examples.hazelcast.leader.autoconfigure.services.WeeInboundLeaderMessageHandler;
import org.xitikit.examples.hazelcast.leader.autoconfigure.services.WeeLeaderCandidate;

import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@SuppressWarnings("unused")
@Component
@ExtendWith(SpringExtension.class)
@TestInstance(PER_CLASS)
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = WeeClusterBootApplication.class)
class WeeClusterBootApplicationTest{

    @Qualifier("clusterBootProperties")
    @Autowired
    private ClusterBootProperties clusterBootProperties;

    @Qualifier("weeInboundLeaderMessageHandler")
    @Autowired
    private WeeInboundLeaderMessageHandler weeInboundLeaderMessageHandler;

    @Qualifier("weeLeaderCandidate")
    @Autowired
    private WeeLeaderCandidate weeLeaderCandidate;

    @Qualifier("weeLeaderEventPublisher")
    @Autowired
    private LeaderEventPublisher weeLeaderEventPublisher;

    @Qualifier("weeInboundLeaderMessageChannel")
    @Autowired
    private MessageChannel weeLeaderMessageChannel;

    @Qualifier("weeClusterEventsMessageProducer")
    @Autowired
    private HazelcastEventDrivenMessageProducer weeClusterEventsMessageProducer;

    @Test
    void contextLoads(){

    }
}
