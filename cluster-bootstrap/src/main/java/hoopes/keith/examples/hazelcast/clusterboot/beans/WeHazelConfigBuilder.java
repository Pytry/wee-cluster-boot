package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.config.Config;
import com.hazelcast.config.ConfigBuilder;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.ListenerConfig;

import java.util.EventListener;

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
 * This would be better off as a new implementation of the
 * ConfigBuilder interface.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class WeHazelConfigBuilder implements ConfigBuilder{

    private final Config config;

    /**
     * This is made private to ensure proper values
     * for the properties are passed in using the "of"
     * builder creator method.
     */
    private WeHazelConfigBuilder(){

        //TODO: Maybe don't instantiate Config until build() is called?
        // Instead, I think a type of proxy would be best, so we can
        // track which changes and defaults are made in order to
        // offer better and immediate validation. For instance,
        // if "build()" is called before any values are set then
        // an IllegalStateException, or a custom builder exception
        // extending it could be thrown.
        config = new Config();
    }

    /**
     * Creates a HazelcastInstance based on the properties.
     * Configuration is specific to the requirements of the
     * "we Are Started" coding challenge.
     *
     * This method might be better to return an instance of
     * a custom ConfigBuilder so that the properties don't
     * need to be passed to every internal method.
     *
     * @return HazelcastInstance
     */
    public static WeHazelConfigBuilder newBuild(){

        return new WeHazelConfigBuilder();
    }

    /**
     * Creates a HazelcastInstance based on the properties.
     * Configuration is specific to the requirements of the
     * "we Are Started" coding challenge.
     *
     * This method might be better to return an instance of
     * a custom ConfigBuilder so that the properties don't
     * need to be passed to every internal method.
     *
     * @return HazelcastInstance
     */
    public WeHazelConfigBuilder withDefaultConfig(){

        return withDefaultNetworkConfig();
    }

    /**
     * Sets a member attribute using the key:value pair given.
     *
     * @param key {@link String}
     * @param value int
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withMemberIntAttribute(final String key, final int value){

        if(key == null){
            throw new IllegalArgumentException("'key' is required.");
        }
        // Note: I was originally thinking I would set the maxNodes
        // value here, but now I am setting it inside the listener.
        // I kept the method as a proof of concept for setting attributes
        // and eventually for properties as well.
        config
            .getMemberAttributeConfig()
            .setIntAttribute(key, value);

        return this;
    }

    /**
     * Adds default listener configurations.
     *
     * This will NOT remove previously set listeners.
     * TODO: Maybe it should?
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withListener(final EventListener eventListener){

        if(eventListener == null){
            throw new IllegalArgumentException("'eventListener' is required.");
        }
        config.getListenerConfigs().add(
            new ListenerConfig()
                .setImplementation(eventListener));

        return this;
    }

    /**
     * Sets default values for the network configuration.
     *
     * WARNING: This will overwrite previously set values
     * for the JoinConfig, AwsConfig, and MulticastConfig.
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withDefaultNetworkConfig(){

        return withDefaultJoinConfig();
    }

    /**
     * Sets default values for the network join.
     *
     * WARNING: This will overwrite any previous settings
     * made during the build.
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withDefaultJoinConfig(){

        return withDefaultAwsConfig()
            .withDefaultMulticastConfig();
    }

    /**
     * Sets default values for multicast configuration.
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withDefaultMulticastConfig(){

        config
            .getNetworkConfig()
            .getJoin()
            .getMulticastConfig()
            .setMulticastGroup("WeClusterMulticastGroup");//TODO: Base this off od the spring.application.name?

        return this;
    }

    /**
     * Sets default values for the network joins AWS configuration.
     * The default is to disable AWS network joins.
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withDefaultAwsConfig(){

        config
            .getNetworkConfig()
            .getJoin()
            .getAwsConfig()
            .setEnabled(false);

        return this;
    }

    /**
     * Maybe add some child builders for the different sub-configurations as well.
     *
     * @param groupConfig {@link GroupConfig}
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withGroupConfig(final GroupConfig groupConfig){

        if(groupConfig == null){
            throw new IllegalArgumentException("'groupConfig' is required.");
        }
        config.setGroupConfig(groupConfig);
        return this;
    }

    /**
     * Sets default values for the group configuration based on
     * the properties|yaml file.
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withDefaultGroupConfig(){

        return withGroupName("WeAreStartedGroup")
            .withGroupPassword("WeAreStartedGroupPassword");
    }

    /**
     * Set the group name.
     *
     * This will trim excess leading and trailing whitespace.
     * If the given value is null, then the actual value set will
     * be an empty {@link String}.
     *
     * @param groupName {@link String} can be null or empty
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withGroupName(final String groupName){

        config
            .getGroupConfig()
            .setName(
                groupName == null ?
                    "" :
                    groupName.trim());

        return this;
    }

    /**
     * Set the group password.
     *
     * This will trim excess leading and trailing whitespace.
     * If the given value is null, then the actual value set will
     * be an empty {@link String}.
     *
     * @param groupPassword {@link String} can be null or empty
     *
     * @return this {@link WeHazelConfigBuilder}
     */
    public WeHazelConfigBuilder withGroupPassword(final String groupPassword){

        config
            .getGroupConfig()
            .setName(
                groupPassword == null ?
                    "" :
                    groupPassword.trim());

        return this;
    }

    @Override
    public Config build(){
        //TODO: add validation to make sure default values were actually set properly
        return config;
    }
}
