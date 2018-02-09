package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.config.Config;
import com.hazelcast.config.ConfigBuilder;
import com.hazelcast.config.ListenerConfig;

import java.util.EventListener;
import java.util.Optional;

/**
 * Creates a default configuration specific to this code project.
 *
 * TODO: Add more robust builders to handle all possible configurations.
 * TODO: Add a method for configuring based on values in a properties or yml file
 * TODO: as opposed to an XML file (ie; Spring Boots application.properties|yml)
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
public final class WeeHazelConfigBuilder implements ConfigBuilder{

    private final Config config;

    private final ClusterBootProperties props;

    /**
     * This is made private to ensure proper values
     * for the properties are passed in using the "of"
     * builder creator method.
     */
    private WeeHazelConfigBuilder(ClusterBootProperties properties){

        if(properties == null){
            throw new IllegalArgumentException("'ClusterBootProperties' is required.");
        }
        //TODO: Maybe don't instantiate Config until build() is called?
        // Instead, I think a type of proxy would be best, so we can
        // track which changes and defaults are made in order to
        // offer better and immediate validation. For instance,
        // if "build()" is called before any values are set then
        // an IllegalStateException, or a custom builder exception
        // extending it could be thrown.
        config = new Config();
        props = properties;
    }

    /**
     * Creates a HazelcastInstance based on default property values.
     *
     * Configuration is specific to the requirements of the
     * "we Are Started" coding challenge.
     *
     * @return HazelcastInstance
     */
    public static WeeHazelConfigBuilder newBuild(){

        return new WeeHazelConfigBuilder(new ClusterBootProperties());
    }

    /**
     * Creates a HazelcastInstance based on the passed in {@link ClusterBootProperties}
     *
     * @return HazelcastInstance
     */
    public static WeeHazelConfigBuilder newBuild(final ClusterBootProperties clusterBootProperties){

        return new WeeHazelConfigBuilder(clusterBootProperties);
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
    public WeeHazelConfigBuilder withDefaultConfig(){

        return withDefaultNetworkConfig();
    }

    /**
     * Sets default values for the network configuration.
     *
     * WARNING: This will overwrite previously set values
     * for the JoinConfig, AwsConfig, and MulticastConfig.
     *
     * @return this {@link WeeHazelConfigBuilder}
     */
    public WeeHazelConfigBuilder withDefaultNetworkConfig(){

        return withDefaultJoinConfig();
    }

    /**
     * Sets default values for the network join.
     *
     * WARNING: This will overwrite any previous settings
     * made during the build.
     *
     * @return this {@link WeeHazelConfigBuilder}
     */
    public WeeHazelConfigBuilder withDefaultJoinConfig(){

        return withDefaultAwsConfig()
            .withDefaultMulticastConfig();
    }

    /**
     * Sets default values for multicast configuration.
     *
     * @return this {@link WeeHazelConfigBuilder}
     */
    public WeeHazelConfigBuilder withDefaultMulticastConfig(){

        config
            .getNetworkConfig()
            .getJoin()
            .getMulticastConfig()
            .setMulticastGroup(
                Optional
                    .ofNullable(
                        props.getMulticastGroup())
                    .orElse("224.0.0.1")
            );

        return this;
    }

    /**
     * Sets default values for the network joins AWS configuration.
     * The default is to disable AWS network joins.
     *
     * @return this {@link WeeHazelConfigBuilder}
     */
    public WeeHazelConfigBuilder withDefaultAwsConfig(){

        config
            .getNetworkConfig()
            .getJoin()
            .getAwsConfig()
            .setEnabled(false);

        return this;
    }

    /**
     * Adds default listener configurations.
     *
     * This will NOT remove previously set listeners.
     * TODO: Maybe it should?
     *
     * @return this {@link WeeHazelConfigBuilder}
     */
    public WeeHazelConfigBuilder withListener(final EventListener eventListener){

        if(eventListener == null){
            throw new IllegalArgumentException("'eventListener' is required.");
        }
        config.getListenerConfigs().add(
            new ListenerConfig()
                .setImplementation(eventListener));

        return this;
    }

    /**
     * Sets default values for the group configuration based on
     * the properties|yml file.
     *
     * @return this {@link WeeHazelConfigBuilder}
     */
    public WeeHazelConfigBuilder withDefaultGroupConfig(){

        return withGroupName("WeeClusterBootGroup")
            .withGroupPassword("WeeClusterBootGroupPassword");
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
     * @return this {@link WeeHazelConfigBuilder}
     */
    public WeeHazelConfigBuilder withGroupName(final String groupName){

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
     * @return this {@link WeeHazelConfigBuilder}
     */
    public WeeHazelConfigBuilder withGroupPassword(final String groupPassword){

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
