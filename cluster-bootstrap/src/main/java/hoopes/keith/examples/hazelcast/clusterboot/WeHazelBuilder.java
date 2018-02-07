package hoopes.keith.examples.hazelcast.clusterboot;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Note: I'm not sure if it exists, but it would be
 * nice to have a Spring Boot project with an
 * AutoConfiguration and @ConfigurationProperties
 * setup for this. If I had more time to become
 * familiar with the Hazelcast ecosystem, I probably
 * would have either used that, or fleshed this out
 * a lot more to handle a robust bootstrapping.
 *
 * For now, the intention is to just move out all this
 * logic out of the configuration file to make it readable
 * and easily extensible.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
public final class WeHazelBuilder{

    public static HazelcastInstance build(final WeAreStartedProperties weAreStartedProperties){

        return Hazelcast.newHazelcastInstance(weHazelcastConfig(weAreStartedProperties));
    }

    private static Config weHazelcastConfig(final WeAreStartedProperties weAreStartedProperties){

        return new Config()
            .setGroupConfig(groupConfig())
            .setNetworkConfig(networkConfig())
            .setProperty(
                "maxNodes",
                "" + weAreStartedProperties.getMaxNodes());
    }

    private static NetworkConfig networkConfig(){

        return new NetworkConfig().setJoin(joinConfig());
    }

    private static JoinConfig joinConfig(){

        return new JoinConfig()
            .setMulticastConfig(multicastConfig())
            .setAwsConfig(awsConfig());

    }

    private static MulticastConfig multicastConfig(){

        return new MulticastConfig()
            .setEnabled(true)
            .setMulticastGroup("WeAreStartedMulticastGroup");
    }

    private static AwsConfig awsConfig(){

        return new AwsConfig()
            .setEnabled(false);
    }

    private static GroupConfig groupConfig(){

        return new GroupConfig()
            .setName("WeAreStartedGroup")
            .setPassword("WeAreStartedGroupPassword");
    }
}
