package hoopes.keith.examples.hazelcast.simplecluster;

import hoopes.keith.examples.hazelcast.clusterboot.beans.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@Component
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = WeeClusterBootApplication.class
)
@TestInstance(PER_CLASS)
class WeeClusterBootApplicationTests{

    @Value("${local.server.port}")
    private int port;

    @Qualifier("weeLeaderMessageHandler")
    @Autowired
    public MessageHandler weeLeaderMessageHandler;

    @Qualifier("weeLeaderNotificationService")
    @Autowired
    private WeeLeaderNotificationService weeLeaderNotificationService;

    @Qualifier("clusterBootProperties")
    @Autowired
    private ClusterBootProperties clusterBootProperties;

    @Qualifier("leaderStartupEventMap")
    @Autowired
    private StartupEventMapProxy leaderStartupEventMap;

    @Qualifier("weClusterMemberStartedListener")
    @Autowired
    private WeClusterMemberStartedListener weClusterMemberStartedListener;

    @Qualifier("weeLeaderCandidate")
    @Autowired
    private WeeLeaderCandidate weeLeaderCandidate;

    @Qualifier("weLeaderEventPublisher")
    @Autowired
    private LeaderEventPublisher weLeaderEventPublisher;

    @Qualifier("weeLeaderMessageChannel")
    @Autowired
    private MessageChannel weeLeaderMessageChannel;

    @Qualifier("weeClusterEventsMessageProducer")
    @Autowired
    private HazelcastEventDrivenMessageProducer weeClusterEventsMessageProducer;

    @BeforeAll
    void setUp(){
        //TODO: Add in some restful calls to check on the health of the service.
        RestAssured.port = port;
    }

    @Test
    void contextLoads(){

        // assertNotNull(weeLeaderNotificationService);
        // assertNotNull(clusterBootProperties);
        // assertNotNull(weeLeaderNotificationService);
        // assertNotNull(clusterBootProperties);
        // assertNotNull(leaderStartupEventMap);
        // assertNotNull(weClusterMemberStartedListener);
        // assertNotNull(weeLeaderMessageHandler);
        // assertNotNull(weeLeaderCandidate);
        // assertNotNull(weeLeaderNotificationService);
        // assertNotNull(weLeaderEventPublisher);
        // assertNotNull(weeLeaderMessageChannel);
        // assertNotNull(weeClusterEventsMessageProducer);
    }
}
