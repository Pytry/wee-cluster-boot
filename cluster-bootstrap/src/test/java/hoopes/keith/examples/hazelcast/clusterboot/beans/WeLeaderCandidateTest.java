package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import org.junit.jupiter.api.Test;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;

import static org.mockito.Mockito.*;

class WeLeaderCandidateTest{

    @Test
    void onGranted(){

        ;
        StartupEventMapProxy map = new StartupEventMapProxy("test-map");
        map.setHazelcastInstance(
            new TestHazelcastInstanceFactory()
                .newHazelcastInstance()
        );
        HazelcastEventDrivenMessageProducer producer = spy(new HazelcastEventDrivenMessageProducer(map));
        WeLeaderCandidate candidate = new WeLeaderCandidate(producer);
        candidate.onGranted(() -> true);
        candidate.onRevoked(() -> false);
        // Note: Since stop and start are final, they CANNOT be stubbed nor verified.
        // This test is just making sure that no exceptions would be thrown
    }
}