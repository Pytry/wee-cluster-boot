package hoopes.keith.examples.hazelcast.clusterboot.configuration;

import com.hazelcast.core.HazelcastInstance;
import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootAutoConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.beans.ClusterBootProperties;
import hoopes.keith.examples.hazelcast.clusterboot.beans.StartupEventMapProxy;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeClusterMemberStartedListener;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeeHazelConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.Candidate;
import org.springframework.integration.leader.event.LeaderEventPublisher;

import static com.hazelcast.core.Hazelcast.*;

/**
 * I prefer to keep my configuration classes as concise
 * and logically grouped as possible.
 *
 * This configuration group contains everything that depends
 * on an instantiated {@link HazelcastInstance}
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
@AutoConfigurationPackage
@AutoConfigureAfter({
    ClusterBootAutoConfiguration.class,
    PrimaryBeansConfiguration.class,
    MessagingBeansConfiguration.class
})
public class HazelcastInstanceConfiguration{

    private static final Logger log = LoggerFactory.getLogger(ClusterBootAutoConfiguration.class);

    /**
     * We need to actually set the name of the HazelcastInstance
     * bean in the annotation, or else Spring will not be able to
     * find it for Autowiring. This was discovered during testing
     * to act this way.
     */
    @Bean("hazelcastInstance")
    @Autowired
    public static HazelcastInstance hazelcastInstance(
        final ClusterBootProperties clusterBootProperties,
        final WeClusterMemberStartedListener weClusterMemberStartedListener,
        final StartupEventMapProxy leaderStartupEventMap){

        log.info("Creating new HazelcastInstance; " +
            "weClusterMemberStartedListener: " + weClusterMemberStartedListener + ", " +
            "leaderStartupEventMap: " + leaderStartupEventMap);

        HazelcastInstance hazelcastInstance = newHazelcastInstance(
            WeeHazelConfigBuilder
                .newBuild(clusterBootProperties)
                .withDefaultConfig()
                .withListener(weClusterMemberStartedListener)
                .build()
        );

        // Since I am not instantiating the instance until after the services,
        // I need to initiate them now.
        log.info("Injecting HazelcastInstance into leaderStartupEventMap");
        leaderStartupEventMap.setHazelcastInstance(hazelcastInstance);

        log.info("Injecting HazelcastInstance into weClusterMemberStartedListener");
        weClusterMemberStartedListener.setHazelcastInstance(hazelcastInstance);

        return hazelcastInstance;
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean(
        name = "weeLeaderInitiator",
        value = LeaderInitiator.class)
    public LeaderInitiator weeLeaderInitiator(
        //The HazelcastInstance is the only injection that needs a qualifier
        @Qualifier("hazelcastInstance") final HazelcastInstance hazelcastInstance,
        final Candidate weeLeaderCandidate,
        final LeaderEventPublisher weeLeaderEventPublisher){

        log.info("Configuring weeLeaderInitiator; " +
            "weeLeaderCandidate: {" +
            "id:" + weeLeaderCandidate.getId() + ", " +
            "role:" + weeLeaderCandidate.getRole() + "" +
            "}, " +
            "weeLeaderEventPublisher: " + weeLeaderEventPublisher + ", " +
            "hazelcastInstance: " + hazelcastInstance.getName());

        LeaderInitiator leaderInitiator = new LeaderInitiator(hazelcastInstance, weeLeaderCandidate);
        leaderInitiator.setLeaderEventPublisher(weeLeaderEventPublisher);

        return leaderInitiator;
    }
}
