package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure;

import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.leader.event.DefaultLeaderEventPublisher;
import org.springframework.integration.leader.event.LeaderEventPublisher;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * This is the root autoconfigure class
 * for the cluster-boot AutoConfiguration.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
@EnableCaching
@EnableIntegration
@AutoConfigurationPackage
@EnableConfigurationProperties
@Import(HazelcastInstanceConfiguration.class)
@AutoConfigureBefore(HazelcastAutoConfiguration.class)
@ConditionalOnClass({HazelcastInstance.class, HazelcastProperties.class})
@IntegrationComponentScan("hoopes.keith.examples.hazelcast.clusterboot.autoconfigure")
public class ClusterBootAutoConfiguration{

    private static final Logger log = LoggerFactory.getLogger(ClusterBootAutoConfiguration.class);

    @Bean
    @ConfigurationProperties("hoopes.cluster-boot")
    public ClusterBootProperties clusterBootProperties(){

        log.info("Creating ClusterBootProperties.");

        return new ClusterBootProperties();
    }

    @Bean("hazelcastProperties")
    @ConfigurationProperties("spring.hazelcast")
    public HazelcastProperties hazelcastProperties(){

        return new HazelcastProperties();
    }

    @Bean
    @ConditionalOnMissingBean(name = "weeInboundLeaderMessageChannel", value = MessageChannel.class)
    public MessageChannel weeInboundLeaderMessageChannel(){

        log.debug("Configuring DirectChannel as weeInboundLeaderMessageChannel");

        return new DirectChannel();
    }

    @Bean
    @ConditionalOnMissingBean(
        name = "weeLeaderEventPublisher",
        value = LeaderEventPublisher.class
    )
    public LeaderEventPublisher weeLeaderEventPublisher(){

        log.debug("Configuring LeaderEventPublisher");

        return new DefaultLeaderEventPublisher();
    }

    /**
     * I put this cache into it's own service because it was easier to configure.
     *
     * Copyright ${year}
     *
     * @author J. Keith Hoopes
     */
    @Service("allHereCache")
    public class AllHereCache{

        private boolean allHere = false;

        @Cacheable("AllHereCache")
        public boolean isAllHere(){

            return allHere;
        }

        @CachePut("AllHereCache")
        public void setAllHere(final boolean allHere){

            this.allHere = allHere;
        }
    }
}
