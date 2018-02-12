package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services.ClusterBootProperties;
import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services.WeeLeaderCandidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastInstanceFactory;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.hazelcast.CacheListeningPolicyType;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.event.DefaultLeaderEventPublisher;
import org.springframework.integration.leader.event.LeaderEventPublisher;
import org.springframework.messaging.MessageChannel;

import java.io.IOException;

/**
 * This is the root autoconfigure class
 * for the cluster-boot AutoConfiguration.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Configuration
@EnableIntegration
@AutoConfigurationPackage
@EnableConfigurationProperties
@AutoConfigureBefore(HazelcastAutoConfiguration.class)
@ConditionalOnClass({HazelcastInstance.class, HazelcastProperties.class})
@IntegrationComponentScan({
    "hoopes.keith.examples.hazelcast.clusterboot.autoconfigure",
    "hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services"
})
public class ClusterBootAutoConfiguration{

    private static final Logger log = LoggerFactory.getLogger(ClusterBootAutoConfiguration.class);

    /**
     * Name of the distributed map to use for notifications.
     */
    private static final String ALL_HERE_JOB_MAP = "ALL_HERE_JOB_MAP";

    @Bean
    @ConfigurationProperties("hoopes.cluster-boot")
    public ClusterBootProperties clusterBootProperties(){

        log.info("Creating ClusterBootProperties");

        return new ClusterBootProperties();
    }

    @Bean("hazelcastProperties")
    @ConfigurationProperties("spring.hazelcast")
    public HazelcastProperties hazelcastProperties(){

        log.info("Creating HazelcastProperties");

        return new HazelcastProperties();
    }

    @Bean
    public MessageChannel weeInboundLeaderMessageChannel(){

        log.debug("Configuring DirectChannel as WeeInboundLeaderMessageChannel");

        return new DirectChannel();
    }

    @Bean
    public LeaderEventPublisher weeLeaderEventPublisher(){

        log.debug("Configuring LeaderEventPublisher");

        return new DefaultLeaderEventPublisher();
    }

    @Bean
    public LeaderInitiator weeLeaderInitiator(){

        log.info("Configuring weeLeaderInitiator");

        LeaderInitiator leaderInitiator = new LeaderInitiator(weeHazelcastInstance(), weeLeaderCandidate());
        leaderInitiator.setLeaderEventPublisher(weeLeaderEventPublisher());

        return leaderInitiator;
    }

    /**
     * This is basically a copy-paste from {@link org.springframework.boot.autoconfigure.hazelcast.HazelcastServerConfiguration}
     */
    @Bean
    public HazelcastInstance weeHazelcastInstance(){

        try{
            final HazelcastInstance answer;
            final Resource config = hazelcastProperties().resolveConfigLocation();

            if(config != null){
                answer = new HazelcastInstanceFactory(config).getHazelcastInstance();
            }else{
                answer = Hazelcast.newHazelcastInstance();
            }
            answer
                .getConfig()
                .setProperty(
                    "hazelcast.http.healthcheck.enabled",
                    "true");

            return answer;
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    @Bean
    public WeeLeaderCandidate weeLeaderCandidate(){

        log.debug("Configuring WeeLeaderCandidate");

        return new WeeLeaderCandidate(
            weeClusterEventsMessageProducer(),
            weeHazelcastInstance()
                .getMap(ALL_HERE_JOB_MAP),
            clusterBootProperties());
    }

    @Bean
    public HazelcastEventDrivenMessageProducer weeClusterEventsMessageProducer(){

        log.info("Configuring weeClusterEventsMessageProducer");

        HazelcastEventDrivenMessageProducer producer = new HazelcastEventDrivenMessageProducer(
            weeHazelcastInstance().getMap(ALL_HERE_JOB_MAP)
        );
        producer.setOutputChannel(weeInboundLeaderMessageChannel());
        producer.setCacheEventTypes("ADDED,REMOVED,UPDATED,CLEAR_ALL");
        producer.setCacheListeningPolicy(CacheListeningPolicyType.SINGLE);
        producer.setAutoStartup(false);//Only want leader to use

        return producer;
    }
}
