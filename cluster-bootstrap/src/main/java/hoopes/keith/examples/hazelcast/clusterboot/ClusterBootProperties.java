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

    public ClusterBootProperties(){

    }

    public ClusterBootProperties(final int maxNodes){

        this.maxNodes = maxNodes;
    }

    public int getMaxNodes(){

        return maxNodes;
    }

    public void setMaxNodes(final int maxNodes){

        this.maxNodes = maxNodes;
    }
}
