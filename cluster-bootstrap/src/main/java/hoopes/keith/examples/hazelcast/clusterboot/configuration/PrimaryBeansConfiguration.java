package hoopes.keith.examples.hazelcast.clusterboot.configuration;

import com.hazelcast.core.IMap;
import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootAutoConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootProperties;
import hoopes.keith.examples.hazelcast.clusterboot.beans.LeaderClusterEventService;
import hoopes.keith.examples.hazelcast.clusterboot.beans.StartupEventMapProxy;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeClusterMemberStartedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Creates and configures the beans needed
 * for determining leadership in a cluster.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
@AutoConfigurationPackage
@AutoConfigureBefore({
    MessagingBeansConfiguration.class,
    HazelcastInstanceConfiguration.class
})
@AutoConfigureAfter(ClusterBootAutoConfiguration.class)
public class PrimaryBeansConfiguration{

    private static final Logger log = LoggerFactory.getLogger(PrimaryBeansConfiguration.class);

    /**
     * The events map proxy.
     *
     * @return and instance of {@link StartupEventMapProxy} so that
     *     all startup events will be cached locally until a HazelcastInstance
     *     is available.
     */
    @Primary
    @Bean("leaderStartupEventMap")
    public StartupEventMapProxy leaderStartupEventMap(){

        log.debug("Configuring StartupEventMapProxy");

        return new StartupEventMapProxy("leaderStartupEventMap");
    }

    @Primary
    @Bean("leaderClusterEventService")
    public LeaderClusterEventService leaderClusterEventService(
        @Qualifier("leaderStartupEventMap") final IMap<String, String> leaderStartupEventMap){

        log.debug("Configuring LeaderClusterEventService; leaderStartupEventMap: " + leaderStartupEventMap);

        return new LeaderClusterEventService(leaderStartupEventMap);
    }

    @Primary
    @Autowired
    @Bean("weClusterMemberStartedListener")
    public WeClusterMemberStartedListener weClusterMemberStartedListener(
        final ClusterBootProperties clusterBootProperties,
        final LeaderClusterEventService leaderClusterEventService){

        log.debug("Configuring WeClusterMemberStartedListener; " +
            "ClusterBootProperties: " + clusterBootProperties + ", " +
            "LeaderClusterEventService: leaderClusterEventService");

        return new WeClusterMemberStartedListener(
            clusterBootProperties,
            leaderClusterEventService
        );
    }
}
