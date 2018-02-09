package hoopes.keith.examples.hazelcast.clusterboot.configuration;

import com.hazelcast.core.IMap;
import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootAutoConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeeLeaderCandidate;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeeLeaderNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

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

    private static final Logger log = LoggerFactory.getLogger(MessagingBeansConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(
        name = "weLeaderEventPublisher",
        value = LeaderEventPublisher.class
    )
    public LeaderEventPublisher weLeaderEventPublisher(){

        log.debug("Configuring LeaderEventPublisher");

        return new DefaultLeaderEventPublisher();
    }

    @Bean("weeLeaderNotificationService")
    public WeeLeaderNotificationService weeLeaderNotificationService(
        @Qualifier("weeLeaderMessageChannel") final MessageChannel weeLeaderMessageChannel){

        log.debug("Configuring WeeLeaderNotificationService; weeLeaderMessageChannel: " + weeLeaderMessageChannel);

        return new WeeLeaderNotificationService(weeLeaderMessageChannel);
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean(
        name = "weeLeaderCandidate",
        value = WeeLeaderCandidate.class)
    public WeeLeaderCandidate weeLeaderCandidate(
        final HazelcastEventDrivenMessageProducer clusterLeaderEventDrivenMessageProducer){

        log.debug("Configuring WeeLeaderCandidate; clusterLeaderEventDrivenMessageProducer:" + clusterLeaderEventDrivenMessageProducer);

        return new WeeLeaderCandidate(clusterLeaderEventDrivenMessageProducer);
    }

    @Bean
    @ConditionalOnMissingBean(
        name = "weeLeaderMessageChannel",
        value = MessageChannel.class
    )
    public MessageChannel weeLeaderMessageChannel(){

        log.debug("Configuring DirectChannel as weeLeaderMessageChannel");

        return new DirectChannel();
    }

    @Bean
    @Autowired
    @ServiceActivator(inputChannel = "weeLeaderMessageChannel")
    @ConditionalOnMissingBean(
        name = "leaderMessageHandler",
        value = MessageHandler.class
    )
    public MessageHandler leaderMessageHandler(
        final LeaderInitiator initiator){

        log.debug("Configuring MessageHandler lambda for leaderMessageHandler");

        return message -> {

            String m = message.toString();
            String bannerEdge = Stream
                .of(m.toCharArray())
                .map(c -> "*")
                .collect(joining());

            System.out.println();
            System.out.println(bannerEdge);
            System.out.println(m);
            System.out.println(bannerEdge);
            System.out.println();

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
        @Qualifier("weeLeaderMessageChannel") final MessageChannel leaderMessageChannel){

        log.debug("Configuring HazelcastEventDrivenMessageProducer; " +
            "leaderStartupEventMap: " + leaderStartupEventMap + ", " +
            "weeLeaderMessageChannel: " + leaderMessageChannel + ", " +
            "cacheEventTypes: {ADDED,REMOVED,UPDATED,CLEAR_ALL}, " +
            "cacheListeningPolicy: CacheListeningPolicyType.SINGLE, " +
            "autoStartup: false");

        HazelcastEventDrivenMessageProducer producer = new HazelcastEventDrivenMessageProducer(leaderStartupEventMap);
        producer.setOutputChannel(leaderMessageChannel);
        producer.setCacheEventTypes("ADDED,REMOVED,UPDATED,CLEAR_ALL");
        producer.setCacheListeningPolicy(CacheListeningPolicyType.SINGLE);
        producer.setAutoStartup(false);//Only want leader to use

        return producer;
    }
}
