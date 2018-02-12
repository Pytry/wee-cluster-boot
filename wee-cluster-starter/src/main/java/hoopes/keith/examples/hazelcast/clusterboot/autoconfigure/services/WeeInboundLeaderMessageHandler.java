package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.ClusterBootAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.leader.Context;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import static hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services.AllHereCacheConstants.*;
import static java.lang.Boolean.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Service
public class WeeInboundLeaderMessageHandler implements MessageHandler{

    private final ClusterBootAutoConfiguration hazelcastInstanceConfiguration;

    @Autowired
    public WeeInboundLeaderMessageHandler(final ClusterBootAutoConfiguration clusterBootAutoConfiguration){

        this.hazelcastInstanceConfiguration = clusterBootAutoConfiguration;
    }

    /**
     * Handle the given message.
     *
     * @param message the message to be handled
     */
    @Override
    @ServiceActivator(inputChannel = "weeInboundLeaderMessageChannel")
    public void handleMessage(final Message<?> message) throws MessagingException{

        final HazelcastInstance hazelcastInstance = hazelcastInstanceConfiguration.weeHazelcastInstance();
        final Context context = hazelcastInstanceConfiguration
            .weeLeaderInitiator()
            .getContext();

        IMap<String, Boolean> allHereCache = hazelcastInstance.getMap(ALL_HERE_DISTRIBUTED_MAP);
        Boolean isAllHereSent = allHereCache.get(IS_ALL_HERE_JOB_SENT);
        if(isAllHereSent == null){
            isAllHereSent = Boolean.FALSE;
        }
        if(!isAllHereSent){
            // I am not sure whether this is the best way to get the size or not.
            // In a small cluster, the performance won't matter, but I am worried
            // that the call to "getMembers()" might cause some unintended polling
            // and other network calls. I suppose the other option would be to
            // have the node ping it's own healthcheck endpoint, but that seems
            // kludgy.
            int size = hazelcastInstance
                .getCluster()
                .getMembers()
                .size();
            int maxNodes = hazelcastInstanceConfiguration
                .clusterBootProperties()
                .getMaxNodes();
            if(size >= maxNodes){
                System.out.println("\n\n**************\n");
                System.out.println(message.toString());
                System.out.println("\n**************\n\n");
                isAllHereSent = TRUE;
            }
        }
        allHereCache.put("IS_ALL_HERE_SENT", isAllHereSent);
        context.yield();
    }
}
