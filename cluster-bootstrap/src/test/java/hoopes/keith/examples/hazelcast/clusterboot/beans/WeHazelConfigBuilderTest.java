package hoopes.keith.examples.hazelcast.clusterboot.beans;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeHazelConfigBuilderTest{

    @Test
    void newBuildTest(){
        //Not much to test with such a simple implementation
        WeHazelDefaultConfigBuilder builder = WeHazelDefaultConfigBuilder.newBuild();
        assertNotNull(builder);
        assertEquals(builder, builder.withDefaultConfig());
        assertEquals(builder, builder.withDefaultAwsConfig());
        assertEquals(builder, builder.withDefaultGroupConfig());
        assertEquals(builder, builder.withDefaultJoinConfig());
        assertEquals(builder, builder.withDefaultMulticastConfig());
        assertEquals(builder, builder.withDefaultNetworkConfig());

    }
}