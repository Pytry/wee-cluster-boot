package hoopes.keith.examples.hazelcast.clusterboot.configuration;

import hoopes.keith.examples.hazelcast.clusterboot.ClusterBootAutoConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.beans.ClusterBootProperties;
import hoopes.keith.examples.hazelcast.clusterboot.beans.StartupEventMapProxy;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeClusterMemberStartedListener;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeeLeaderNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    @Bean("leaderStartupEventMap")
    public StartupEventMapProxy leaderStartupEventMap(
        @Value("${spring.application.name:weeClusterBoot}") final String springApplicationName){

        log.info("Configuring StartupEventMapProxy for " + springApplicationName);

        return new StartupEventMapProxy(springApplicationName + "-EventMap");
    }

    @Primary
    @Autowired
    @Bean("weClusterMemberStartedListener")
    public WeClusterMemberStartedListener weClusterMemberStartedListener(
        final ClusterBootProperties clusterBootProperties,
        final WeeLeaderNotificationService weeLeaderNotificationService){

        log.info("Configuring WeClusterMemberStartedListener; " +
            "ClusterBootProperties: " + clusterBootProperties + ", " +
            "WeeLeaderNotificationService: weeLeaderNotificationService");

        return new WeClusterMemberStartedListener(
            clusterBootProperties,
            weeLeaderNotificationService
        );
    }
}
