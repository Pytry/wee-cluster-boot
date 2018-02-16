package org.xitikit.examples.hazelcast.leader.autoconfigure.services;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import org.junit.jupiter.api.Test;
import org.springframework.integration.hazelcast.message.EntryEventMessagePayload;
import org.springframework.messaging.support.GenericMessage;
import org.xitikit.examples.hazelcast.leader.autoconfigure.ClusterBootAutoConfiguration;

import static java.lang.Boolean.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class WeeInboundLeaderMessageHandlerTest{

    @Test
    void handleMessageNotSent(){

        ClusterBootAutoConfiguration config = mock(ClusterBootAutoConfiguration.class);
        HazelcastInstance hazel = new TestHazelcastInstanceFactory().newHazelcastInstance();
        ClusterBootProperties props = new ClusterBootProperties();
        AllHereSentService allHere = mock(AllHereSentService.class);
        WeeInboundLeaderMessageHandler hand = new WeeInboundLeaderMessageHandler(config, allHere);
        GenericMessage<EntryEventMessagePayload> genericMessage = (GenericMessage<EntryEventMessagePayload>) mock(GenericMessage.class);
        EntryEventMessagePayload pay = new EntryEventMessagePayload("whatever", "whatever", "whatever");

        when(config.weeHazelcastInstance()).thenReturn(hazel);
        when(config.clusterBootProperties()).thenReturn(props);
        when(allHere.isAllHereSent()).thenReturn(FALSE);
        when(genericMessage.getPayload()).thenReturn(pay);
        hand.handleMessage(genericMessage);

        verify(allHere, times(1)).isAllHereSent();
        verify(allHere, times(1)).setAllHereSent(TRUE);
        verify(config, times(1)).weeHazelcastInstance();
    }

    @Test
    void handleMessageAllReadySent(){

        ClusterBootAutoConfiguration config = mock(ClusterBootAutoConfiguration.class);
        HazelcastInstance hazel = new TestHazelcastInstanceFactory().newHazelcastInstance();
        ClusterBootProperties props = new ClusterBootProperties();
        AllHereSentService allHere = mock(AllHereSentService.class);
        WeeInboundLeaderMessageHandler hand = new WeeInboundLeaderMessageHandler(config, allHere);
        GenericMessage<EntryEventMessagePayload> genericMessage = (GenericMessage<EntryEventMessagePayload>) mock(GenericMessage.class);
        EntryEventMessagePayload pay = new EntryEventMessagePayload("whatever", "whatever", "whatever");

        when(config.weeHazelcastInstance()).thenReturn(hazel);
        when(config.clusterBootProperties()).thenReturn(props);
        when(allHere.isAllHereSent()).thenReturn(TRUE);
        when(genericMessage.getPayload()).thenReturn(pay);
        hand.handleMessage(genericMessage);

        verify(allHere, times(1)).isAllHereSent();
        verify(allHere, never()).setAllHereSent(TRUE);
        verify(allHere, never()).setAllHereSent(FALSE);
        verify(config, never()).weeHazelcastInstance();
    }
}
