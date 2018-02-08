package hoopes.keith.examples.hazelcast.clusterboot;

import com.hazelcast.config.Config;
import hoopes.keith.examples.hazelcast.clusterboot.beans.WeHazelConfigBuilder;
import hoopes.keith.examples.hazelcast.clusterboot.configuration.PrimaryBeansConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.configuration.MessagingBeansConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.configuration.HazelcastInstanceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * This is the root configuration class
 * for autoconfiguring node aware leaders.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
@AutoConfigurationPackage
@Import({
    MessagingBeansConfiguration.class,
    PrimaryBeansConfiguration.class,
    HazelcastInstanceConfiguration.class})
@AutoConfigureBefore({
    MessagingBeansConfiguration.class,
    PrimaryBeansConfiguration.class,
    HazelcastInstanceConfiguration.class})
@EnableConfigurationProperties(ClusterBootProperties.class)
public class ClusterBootAutoConfiguration{

    @Bean("clusterBootProperties")
    @ConfigurationProperties("hoopes.cluster")
    public ClusterBootProperties clusterBootProperties(){
        return new ClusterBootProperties();
    }

    // @Autowired
    // @Bean
    // public Config config(){
    //     // I am still unclear on when the config gets initialized,
    //     // and whether the default spring classes create an
    //     // injectable bean for it.
    //     return WeHazelConfigBuilder
    //         .newBuild()
    //         .withDefaultConfig()
    //         .build();
    // }
}
