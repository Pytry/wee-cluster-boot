package hoopes.keith.examples.hazelcast.clusterboot;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
@Import({
    ClusterLeaderConfiguration.class,
    MessagingConfiguration.class,
    WeHazelConfiguration.class})
@EnableConfigurationProperties(WeAreStartedProperties.class)
@ComponentScan("hoopes.keith.examples.hazelcast.clusterboot")
public class ClusterBootAutoConfiguration{
//Should probably add some @OnConditionals here
}
