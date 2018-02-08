package hoopes.keith.examples.hazelcast.clusterboot.configuration;

import com.hazelcast.core.HazelcastInstance;
import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootAutoConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.beans.StartupEventMapProxy;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeClusterMemberStartedListener;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeHazelConfigBuilder;
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

    @Bean("hazelcastInstance")
    @Autowired
    public static HazelcastInstance hazelcastInstance(
        final WeClusterMemberStartedListener weClusterMemberStartedListener,
        final StartupEventMapProxy leaderStartupEventMap){

        HazelcastInstance hazelcastInstance = newHazelcastInstance(
            WeHazelConfigBuilder
                .newBuild()
                .withDefaultConfig()
                .withListener(weClusterMemberStartedListener)
                .build()
        );
        leaderStartupEventMap.setHazelcastInstance(hazelcastInstance);

        return hazelcastInstance;
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean(
        name = "weLeaderInitiator",
        value = LeaderInitiator.class)
    public LeaderInitiator weLeaderInitiator(
        @Qualifier("weLeaderCandidate") final Candidate weLeaderCandidate,
        @Qualifier("weLeaderEventPublisher") final LeaderEventPublisher leaderEventPublisher,
        @Qualifier("hazelcastInstance") final HazelcastInstance hazelcastInstance){

        LeaderInitiator leaderInitiator = new LeaderInitiator(hazelcastInstance, weLeaderCandidate);
        leaderInitiator.setLeaderEventPublisher(leaderEventPublisher);

        return leaderInitiator;
    }
}
