package hoopes.keith.examples.hazelcast.clusterboot;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * I prefer to keep my configuration classes as concise
 * and logically grouped as possible.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
public class WeHazelConfiguration{

    @Bean("weHazelcastInstance")
    @Autowired
    public static HazelcastInstance weHazelcastInstance(
        final WeAreStartedProperties weAreStartedProperties){

        return WeHazelBuilder.build(weAreStartedProperties);
    }
}
