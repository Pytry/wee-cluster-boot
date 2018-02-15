package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.ClusterBootAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.hazelcast.message.EntryEventMessagePayload;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.*;

/**
 * Prints out the configured message if all expected members are present.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Service("weeInboundLeaderMessageHandler")
public class WeeInboundLeaderMessageHandler implements MessageHandler{

    /**
     * The pattern of injecting the configuration was done in
     * the example from tomask79, and before that I had never considered
     * doing such a thing.
     *
     * I don't like the idea of injecting a configuration, but I can't really
     * think of anything specifically wrong with it either.
     *
     * Pro: It does lead to less "@Autowired" injections, so the code is more readable.
     * Con: There is a feeling of pulling dependencies instead of injecting them, which
     * makes my IoC sense tingle.
     *
     * Spring core is actually intercepting the method calls for injection, so it
     * ends up have the same affect as if we had Autowired, but with less annotations.
     * I'll have to make up my mind later when I come back to this and measure my
     * cringe output.
     */
    private final ClusterBootAutoConfiguration hazelcastInstanceConfiguration;

    private final AllHereSentService allHereSentService;

    @Autowired
    public WeeInboundLeaderMessageHandler(
        final ClusterBootAutoConfiguration clusterBootAutoConfiguration,
        final AllHereSentService allHereSentService){

        this.hazelcastInstanceConfiguration = clusterBootAutoConfiguration;
        this.allHereSentService = allHereSentService;
    }

    /**
     * Handle the given message.
     *
     * @param message the message to be handled
     */
    @SuppressWarnings("unchecked")
    @Override
    @ServiceActivator(inputChannel = "weeInboundLeaderMessageChannel")
    public void handleMessage(final Message<?> message) throws MessagingException{
        // Would you assert or validate the input here or not? I'm choosing "not"
        // because this is a public method that could have a test added for it.
        handleMessage((GenericMessage<EntryEventMessagePayload>) message);
    }

    /**
     * Here's a fun little game to play when you're coding.
     *
     * See if you can write your code to never create a local
     * variable reference.
     *
     * @param genericMessage is the original message, but cast
     *     to the proper class to make things readable.
     */
    private void handleMessage(final GenericMessage<EntryEventMessagePayload> genericMessage){

        assert genericMessage != null;

        if(doPrintMessage()){
            if(memberCount() >= expectedCount()){
                printMessage(genericMessage.getPayload().value.toString());
                confirmSent();
            }else{
                // only yield context if not all expected nodes are registered/joined, because otherwise
                // you're disenfranchising the cluster vote and that's wrong
                giveUpLeadership();
            }
        }
        // If you are not supposed to print the message, then it's because it's already been printed.
        // There's no longer any need to keep switching leadership. nor to check the member count.
        // I would want to determine whether the membership dropped below the expected count and
        // send notifications appropriately, but this is better done in a membership listener than here.
    }

    private boolean doPrintMessage(){

        return !allHereSentService.isAllHereSent();
    }

    private int memberCount(){
        // I am not sure whether this is the best way to get the size or not.
        // In a small cluster, the performance won't matter, but I am worried
        // that the call to "getMembers()" might cause some unintended polling
        // and other network calls?
        return hazelcastInstanceConfiguration
            .weeHazelcastInstance()
            .getCluster()
            .getMembers()
            .size();
    }

    private int expectedCount(){

        return hazelcastInstanceConfiguration
            .clusterBootProperties()
            .getExpectedMemberCount();
    }

    private void printMessage(final String message){
        //TODO: maybe substitute this for a different service call, or route to another input channel
        System.out.println("\n\n**************\n");
        System.out.println(message);
        System.out.println("\n**************\n\n");
    }

    private void confirmSent(){

        allHereSentService.setAllHereSent(TRUE);
    }

    /**
     * You have failed your people. Smelt thine crown and furrow though the brow in shame.
     */
    private void giveUpLeadership(){

        hazelcastInstanceConfiguration
            .weeLeaderInitiator()
            .getContext()
            .yield();
    }
}

