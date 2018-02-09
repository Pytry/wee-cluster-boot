package hoopes.keith.examples.hazelcast.clusterboot;

import hoopes.keith.examples.hazelcast.clusterboot.beans.ClusterBootProperties;
import hoopes.keith.examples.hazelcast.clusterboot.configuration.HazelcastInstanceConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.configuration.MessagingBeansConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.configuration.PrimaryBeansConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

/**
 * This is the root configuration class
 * for the cluster-boot AutoConfiguration.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan
@AutoConfigurationPackage
@EnableConfigurationProperties
@Import({
    MessagingBeansConfiguration.class,
    PrimaryBeansConfiguration.class,
    HazelcastInstanceConfiguration.class})
@AutoConfigureBefore({
    MessagingBeansConfiguration.class,
    PrimaryBeansConfiguration.class,
    HazelcastInstanceConfiguration.class})
public class ClusterBootAutoConfiguration{

    /**
     * I think it would be great to configure some AOP for logging, but meh ...
     * Maybe later.
     */
    private static Logger log = LoggerFactory.getLogger(ClusterBootAutoConfiguration.class);

    @Bean("clusterBootProperties")
    @ConfigurationProperties("hazelcast.wee-cluster")
    public ClusterBootProperties clusterBootProperties(){

        log.info("Creating ClusterBootProperties.");

        return new ClusterBootProperties();
    }

    // @Bean
    // @Primary
    // @Autowired
    // public Config config(){
    //     // I am still unclear on when the config gets initialized,
    //     // and whether the default spring classes create an
    //     // injectable bean for it. It seems that, for whatever reason,
    //     // spring container can never find this been.
    //     return WeeHazelConfigBuilder
    //         .newBuild()
    //         .withDefaultConfig()
    //         .build();
    // }
}
