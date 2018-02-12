package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

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

    private int maxNodes = 1;

    private String allHereMessage = "All Here";

    public ClusterBootProperties(){

    }

    public ClusterBootProperties(
        final int maxNodes,
        final String allHereMessage){

        this.maxNodes = maxNodes;
        this.allHereMessage = allHereMessage;
    }

    public int getMaxNodes(){

        return maxNodes;
    }

    public void setMaxNodes(final int maxNodes){

        this.maxNodes = maxNodes;
    }

    public String getAllHereMessage(){

        return allHereMessage;
    }

    public void setAllHereMessage(final String allHereMessage){

        this.allHereMessage = allHereMessage;
    }
}
