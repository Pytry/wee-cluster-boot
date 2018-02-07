package hoopes.keith.examples.hazelcast.clusterboot;

import org.junit.jupiter.api.Test;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.leader.Context;

import static org.mockito.Mockito.*;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
class WeLeaderCandidateTest{

    @SuppressWarnings("unchecked")
    @Test
    void onGranted(){

        HazelcastEventDrivenMessageProducer producer = spy(mock(HazelcastEventDrivenMessageProducer.class));
        Context context = mock(Context.class);
        WeLeaderCandidate can = new WeLeaderCandidate(producer);

        can.onGranted(context);

        verify(
            producer,
            times(1)
        ).start();

        verify(
            producer,
            never()
        ).stop();
    }

    @Test
    void onRevoked(){

        HazelcastEventDrivenMessageProducer producer = spy(mock(HazelcastEventDrivenMessageProducer.class));
        Context context = mock(Context.class);
        WeLeaderCandidate can = new WeLeaderCandidate(producer);

        can.onRevoked(context);

        verify(
            producer,
            never()
        ).start();

        verify(
            producer,
            times(1)
        ).stop();
    }
}