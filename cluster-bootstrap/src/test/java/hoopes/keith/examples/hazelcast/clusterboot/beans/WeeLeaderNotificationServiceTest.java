package hoopes.keith.examples.hazelcast.clusterboot.beans;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

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

        MessageChannel messageChannel = mock(MessageChannel.class);
        WeeLeaderNotificationService service = new WeeLeaderNotificationService(messageChannel);
        service.notifyLeaderOfStartup(UUID.randomUUID().toString());

        verify(messageChannel, atLeastOnce())
            .send(
                any(GenericMessage.class)
            );
    }
}