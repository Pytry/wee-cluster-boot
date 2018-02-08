package hoopes.keith.examples.hazelcast.clusterboot.configuration;

import com.hazelcast.core.IMap;
import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootAutoConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeLeaderCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.hazelcast.CacheListeningPolicyType;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.event.DefaultLeaderEventPublisher;
import org.springframework.integration.leader.event.LeaderEventPublisher;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
@AutoConfigurationPackage
@AutoConfigureBefore(HazelcastInstanceConfiguration.class)
@AutoConfigureAfter({
    ClusterBootAutoConfiguration.class,
    PrimaryBeansConfiguration.class})
public class MessagingBeansConfiguration{

    @Bean
    @ConditionalOnMissingBean(
        name = "weLeaderEventPublisher",
        value = LeaderEventPublisher.class
    )
    public LeaderEventPublisher weLeaderEventPublisher(){

        return new DefaultLeaderEventPublisher();
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean(
        name = "weLeaderCandidate",
        value = WeLeaderCandidate.class)
    public WeLeaderCandidate weLeaderCandidate(
        final HazelcastEventDrivenMessageProducer clusterLeaderEventDrivenMessageProducer){

        return new WeLeaderCandidate(clusterLeaderEventDrivenMessageProducer);
    }

    @Bean("leaderMessageChannel")
    @ConditionalOnMissingBean(
        name = "leaderMessageChannel",
        value = MessageChannel.class
    )
    public MessageChannel leaderMessageChannel(){

        return new DirectChannel();
    }

    @Bean
    @Autowired
    @ServiceActivator(inputChannel = "leaderMessageChannel")
    @ConditionalOnMissingBean(
        name = "leaderMessageHandler",
        value = MessageHandler.class
    )
    public MessageHandler leaderMessageHandler(final LeaderInitiator initiator){

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
    @ConditionalOnMissingBean(
        name = "clusterLeaderEventDrivenMessageProducer",
        value = HazelcastEventDrivenMessageProducer.class
    )
    public HazelcastEventDrivenMessageProducer clusterLeaderEventDrivenMessageProducer(
        @Qualifier("leaderStartupEventMap") final IMap<String, String> leaderStartupEventMap,
        @Qualifier("leaderMessageChannel") final MessageChannel leaderMessageChannel){

        HazelcastEventDrivenMessageProducer producer = new HazelcastEventDrivenMessageProducer(leaderStartupEventMap);
        producer.setOutputChannel(leaderMessageChannel);
        producer.setCacheEventTypes("ADDED,REMOVED,UPDATED,CLEAR_ALL");
        producer.setCacheListeningPolicy(CacheListeningPolicyType.SINGLE);
        producer.setAutoStartup(false);//Only want leader to use

        return producer;
    }
}
