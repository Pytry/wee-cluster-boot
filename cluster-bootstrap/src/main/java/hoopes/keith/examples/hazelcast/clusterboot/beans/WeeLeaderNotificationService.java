package hoopes.keith.examples.hazelcast.clusterboot.beans;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

public class WeeLeaderNotificationService{

    private final MessageChannel messageChannel;

    public WeeLeaderNotificationService(final MessageChannel messageChannel){

        if(messageChannel == null){
            throw new IllegalArgumentException("'messageChannel' is required.");
        }

        this.messageChannel = messageChannel;
    }

    public void notifyLeaderOfStartup(final String nodeUuid){

        messageChannel.send(new GenericMessage<>(nodeUuid));
    }
}
