package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastInstanceFactory;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.hazelcast.CacheListeningPolicyType;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.event.LeaderEventPublisher;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.IOException;

/**
 * I prefer to keep my autoconfigure classes as concise
 * and logically grouped as possible.
 *
 * This autoconfigure group contains everything that depends
 * on an instantiated {@link HazelcastInstance}
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Configuration
@IntegrationComponentScan
public class HazelcastInstanceConfiguration{

    private static final Logger log = LoggerFactory.getLogger(ClusterBootAutoConfiguration.class);

    @Autowired
    private ClusterBootProperties clusterBootProperties;

    @Autowired
    private HazelcastProperties hazelcastProperties;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("allHereCache")
    private ClusterBootAutoConfiguration.AllHereCache allHereCache;

    @Autowired
    @Qualifier("weeInboundLeaderMessageChannel")
    private MessageChannel weeInboundLeaderMessageChannel;

    @Autowired
    private LeaderEventPublisher weeLeaderEventPublisher;

    @ConditionalOnMissingBean(name = "weeLeaderMessageHandler", value = MessageHandler.class)
    @ServiceActivator(inputChannel = "weeInboundLeaderMessageChannel ")
    @Bean
    public MessageHandler weeLeaderMessageHandler(){

        log.debug("Configuring MessageHandler lambda for weeLeaderMessageHandler");

        return message -> {

            Context context = weeLeaderInitiator().getContext();
            context.yield();

            if(!allHereCache.isAllHere()){
                // I am not sure whether this is the best way to get the size or not.
                // In a small cluster, the performance won't matter, but I am worried
                // that the call to "getMembers()" might cause some unintended polling
                // and other network calls. I suppose the other option would be to
                // have the node ping it's own healthcheck endpoint, but that seems
                // kludgy.
                int size = weeHazelcastInstance().getCluster().getMembers().size();
                int maxNodes = clusterBootProperties.getMaxNodes();
                if(size >= maxNodes){
                    System.out.println("\n\n**************\n");
                    System.out.println(message.toString());
                    System.out.println("\n**************\n\n");
                    allHereCache.setAllHere(true);
                }
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(
        name = "weeLeaderInitiator",
        value = LeaderInitiator.class)
    public LeaderInitiator weeLeaderInitiator(){

        log.info("Configuring weeLeaderInitiator");

        LeaderInitiator leaderInitiator = new LeaderInitiator(weeHazelcastInstance(), weeLeaderCandidate());
        leaderInitiator.setLeaderEventPublisher(weeLeaderEventPublisher);

        return leaderInitiator;
    }

    /**
     * This is basically a copy-paste from {@link org.springframework.boot.autoconfigure.hazelcast.HazelcastServerConfiguration}
     */
    @Bean
    public HazelcastInstance weeHazelcastInstance(){

        try{
            final HazelcastInstance answer;
            final Resource config = hazelcastProperties.resolveConfigLocation();

            if(config != null){
                answer = new HazelcastInstanceFactory(config).getHazelcastInstance();
            }else{
                answer = Hazelcast.newHazelcastInstance();
            }
            answer
                .getConfig()
                .setProperty("hazelcast.http.healthcheck.enabled", "true");

            return answer;
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    @Bean
    @ConditionalOnMissingBean(
        name = "weeLeaderCandidate",
        value = WeeLeaderCandidate.class)
    public WeeLeaderCandidate weeLeaderCandidate(){

        log.debug("Configuring WeeLeaderCandidate");

        return new WeeLeaderCandidate(weeClusterEventsMessageProducer());
    }

    @Bean
    @ConditionalOnMissingBean(
        name = "weeClusterEventsMessageProducer",
        value = HazelcastEventDrivenMessageProducer.class)
    public HazelcastEventDrivenMessageProducer weeClusterEventsMessageProducer(){

        log.info("Configuring weeClusterEventsMessageProducer");

        HazelcastEventDrivenMessageProducer producer = new HazelcastEventDrivenMessageProducer(
            weeHazelcastInstance().getMap("wee-cluster-boot-leader-event-map")
        );
        producer.setOutputChannel(weeInboundLeaderMessageChannel);
        producer.setCacheEventTypes("ADDED,REMOVED,UPDATED,CLEAR_ALL");
        producer.setCacheListeningPolicy(CacheListeningPolicyType.SINGLE);
        producer.setAutoStartup(false);//Only want leader to use

        return producer;
    }

}
