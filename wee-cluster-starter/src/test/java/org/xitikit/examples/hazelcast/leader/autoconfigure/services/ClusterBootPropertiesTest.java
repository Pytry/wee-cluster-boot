package org.xitikit.examples.hazelcast.leader.autoconfigure.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClusterBootPropertiesTest{

    @Test
    void verifyWYSIWYG(){

        ClusterBootProperties props;

        props = new ClusterBootProperties();
        assertEquals(1, props.getExpectedMemberCount());
        assertEquals("All Here", props.getAllHereMessage());

        props = new ClusterBootProperties(10, "No Bears Out Tonight");
        assertEquals(10, props.getExpectedMemberCount());
        assertEquals("No Bears Out Tonight", props.getAllHereMessage());

        props.setExpectedMemberCount(801);
        props.setAllHereMessage(" Papa killed them all last night! \t\n");
        assertEquals(801, props.getExpectedMemberCount());
        assertEquals(" Papa killed them all last night! \t\n", props.getAllHereMessage());
    }
}