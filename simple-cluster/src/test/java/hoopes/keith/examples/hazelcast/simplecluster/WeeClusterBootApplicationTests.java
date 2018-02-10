package hoopes.keith.examples.hazelcast.simplecluster;

import hoopes.keith.examples.hazelcast.clusterboot.beans.ClusterBootProperties;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeeLeaderCandidate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.event.LeaderEventPublisher;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@Component
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = WeeClusterBootApplication.class
)
class WeeClusterBootApplicationTest{

    @Value("${local.server.port}")
    private int port;

    @Qualifier("clusterBootProperties")
    @Autowired
    private ClusterBootProperties clusterBootProperties;

    @Qualifier("weeLeaderMessageHandler")
    @Autowired
    private MessageHandler weeLeaderMessageHandler;

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

    @BeforeAll
    void setUp(){
        //TODO: Add in some restful calls to check on the health of the service.
    }

    @Test
    void contextLoads(){

        assertNotNull(clusterBootProperties);
        assertNotNull(weeLeaderMessageHandler);
        assertNotNull(weeLeaderCandidate);
        assertNotNull(weeLeaderEventPublisher);
        assertNotNull(weeLeaderMessageChannel);
        assertNotNull(weeClusterEventsMessageProducer);
    }
}
