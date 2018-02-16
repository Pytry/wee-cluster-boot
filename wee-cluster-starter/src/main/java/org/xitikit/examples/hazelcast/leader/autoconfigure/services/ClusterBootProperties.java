package org.xitikit.examples.hazelcast.leader.autoconfigure.services;

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

    /**
     * How many nodes are expected to connect to this cluster?
     */
    private int expectedMemberCount = 1;

    /**
     * This is a message that will be sent once all members that are expected to join
     * have joined.
     */
    private String allHereMessage = "All Here";

    public ClusterBootProperties(){

    }

    public ClusterBootProperties(
        final int expectedMemberCount,
        final String allHereMessage){

        this.expectedMemberCount = expectedMemberCount;
        this.allHereMessage = allHereMessage;
    }

    public int getExpectedMemberCount(){

        return expectedMemberCount;
    }

    public void setExpectedMemberCount(final int expectedMemberCount){

        this.expectedMemberCount = expectedMemberCount;
    }

    public String getAllHereMessage(){

        return allHereMessage;
    }

    public void setAllHereMessage(final String allHereMessage){

        this.allHereMessage = allHereMessage;
    }
}
