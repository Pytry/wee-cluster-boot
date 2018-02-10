package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.test.TestHazelcastInstanceFactory;
import org.junit.jupiter.api.Test;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;

class WeeLeaderCandidateTest{

    /**
     * Note: Since stop and start are final, they CANNOT be stubbed nor verified.
     * This test is just making sure that no exceptions would be thrown
     */
    @Test
    void noExceptions(){

        WeeLeaderCandidate candidate = new WeeLeaderCandidate(
            new HazelcastEventDrivenMessageProducer(
                new TestHazelcastInstanceFactory()
                    .newHazelcastInstance()
                    .getMap("whatever")
            ));
        candidate.onGranted(() -> true);
        candidate.onRevoked(() -> false);
    }
}