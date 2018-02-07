package hoopes.keith.examples.hazelcast.clusterboot;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.hazelcast.CacheListeningPolicyType;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.Context;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
public class MessagingConfiguration{

    @Bean
    public MessageChannel weAreStartedChannel(){

        return new DirectChannel();
    }

    @Bean
    @Autowired
    @ServiceActivator(inputChannel = "weAreStartedChannel")
    public MessageHandler weAreStartedHandler(final LeaderInitiator initiator){

        return message -> {
            System.out.println(message.toString());
            Context context = initiator.getContext();
            if(context.isLeader()){
                context.yield();
            }
        };
    }

    @Bean
    @Autowired
    public HazelcastEventDrivenMessageProducer hazelcastEventDrivenMessageProducer(
        final HazelcastInstance weHazelcastInstance){

        HazelcastEventDrivenMessageProducer producer =
            new HazelcastEventDrivenMessageProducer(
                weHazelcastInstance
                    .getMap("we-are-started"));
        producer.setOutputChannel(weAreStartedChannel());
        producer.setCacheEventTypes("ADDED,REMOVED,UPDATED,CLEAR_ALL");
        producer.setCacheListeningPolicy(CacheListeningPolicyType.SINGLE);
        producer.setAutoStartup(false);

        return producer;
    }
}
