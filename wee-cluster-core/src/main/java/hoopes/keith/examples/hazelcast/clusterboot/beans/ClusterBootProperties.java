package hoopes.keith.examples.hazelcast.clusterboot.beans;

/**
 * A properties class is overkill and I know it. *
 * This is just a placeholder for a more robust
 * autoconfigure. My end goal would be to get yml
 * and properties supported.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
public class ClusterBootProperties{

    private int maxNodes = 10;

    private String multicastGroup = "224.0.0.1";

    private String allHereMessage = "All Here";

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
