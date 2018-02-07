package hoopes.keith.examples.hazelcast.clusterboot;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.Candidate;
import org.springframework.integration.leader.event.DefaultLeaderEventPublisher;
import org.springframework.integration.leader.event.LeaderEventPublisher;

/**
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Configuration
public class ClusterLeaderConfiguration{

    @Bean
    public LeaderEventPublisher leaderEventPublisher(){

        return new DefaultLeaderEventPublisher();
    }

    @Bean
    @Autowired
    public LeaderInitiator initiator(
        @Qualifier("weLeaderCandidate")
        final Candidate weLeaderCandidate,
        @Qualifier("leaderEventPublisher")
        final LeaderEventPublisher leaderEventPublisher,
        @Qualifier("weHazelcastInstance")
        final HazelcastInstance hazelcastInstance){

        LeaderInitiator leaderInitiator = new LeaderInitiator(hazelcastInstance, weLeaderCandidate);
        leaderInitiator.setLeaderEventPublisher(leaderEventPublisher);

        return leaderInitiator;
    }
}
