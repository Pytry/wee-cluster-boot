package hoopes.keith.examples.hazelcast.clusterboot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@ConfigurationProperties("hoopes.cluster")
public class WeAreStartedProperties{

    private int maxNodes;

    public WeAreStartedProperties(){

    }

    public WeAreStartedProperties(final int maxNodes){

        this.maxNodes = maxNodes;
    }

    public int getMaxNodes(){

        return maxNodes;
    }

    public void setMaxNodes(final int maxNodes){

        this.maxNodes = maxNodes;
    }
}
