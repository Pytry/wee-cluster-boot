package hoopes.keith.examples.hazelcast.simplecluster;

import hoopes.keith.examples.hazelcast.clusterboot.beans.WeeLeaderNotificationService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

@Component
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = WeeClusterBootApplication.class
)
@TestInstance(PER_CLASS)
class WeeClusterBootApplicationTests{

    @Value("${local.server.port}")
    private int port;

    @Autowired
    @Qualifier("leaderClusterEventService")
    private WeeLeaderNotificationService weeLeaderNotificationService;

    @BeforeAll
    void setUp(){

        RestAssured.port = port;
    }

    @Test
    void contextLoads(){

        assertNotNull(weeLeaderNotificationService);
    }
}
