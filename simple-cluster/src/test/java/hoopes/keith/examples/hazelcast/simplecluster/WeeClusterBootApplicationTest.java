package hoopes.keith.examples.hazelcast.simplecluster;

import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services.ClusterBootProperties;
import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services.WeeInboundLeaderMessageHandler;
import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services.WeeLeaderCandidate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.event.LeaderEventPublisher;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@SuppressWarnings("unused")
@Component
@ExtendWith(SpringExtension.class)
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
