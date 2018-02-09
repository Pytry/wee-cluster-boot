package hoopes.keith.examples.hazelcast.clusterboot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * A properties class is overkill and I know it.
 * This is just a placeholder for a more robust
 * configuration.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@ConfigurationProperties("hoopes.cluster")
public class ClusterBootProperties{

    private int maxNodes = 10;

    private String multicastGroup = "224.0.0.1";

    private String allHereMessage;

    public ClusterBootProperties(){

    }

    public ClusterBootProperties(
        final int maxNodes,
        final String multicastGroup,
        final String allHereMessage){

        this.maxNodes = maxNodes;
        this.multicastGroup = multicastGroup;
        this.allHereMessage = allHereMessage;
    }

    public int getMaxNodes(){

        return maxNodes;
    }

    public void setMaxNodes(final int maxNodes){

        this.maxNodes = maxNodes;
    }

    public String getMulticastGroup(){

        return multicastGroup;
    }

    public void setMulticastGroup(final String multicastGroup){

        this.multicastGroup = multicastGroup;
    }

    public String getAllHereMessage(){

        return allHereMessage;
    }

    public void setAllHereMessage(final String allHereMessage){

        this.allHereMessage = allHereMessage;
    }
}
