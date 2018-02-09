package hoopes.keith.examples.hazelcast.clusterboot.beans;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;

import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.mockito.Mockito.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@TestInstance(PER_CLASS)
class WeeLeaderNotificationServiceTest{

    @Test
    void notifyLeaderOfStartup(){

        StartupEventMapProxy jobs = spy(new StartupEventMapProxy(""));
        WeeLeaderNotificationService service = spy(new WeeLeaderNotificationService(jobs));
        service.notifyLeaderOfStartup(UUID.randomUUID().toString());

        verify(jobs, atLeastOnce())
            .put(
                any(String.class),
                any(String.class));
    }
}