package hoopes.keith.examples.hazelcast.simplecluster;

import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.ClusterBootAutoConfiguration;
import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services.AllHereSentCache;
import hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services.AllHereSentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.lang.Boolean.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@Configuration
@ExtendWith(SpringExtension.class)
@Import(ClusterBootAutoConfiguration.class)
@TestInstance(PER_METHOD)
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = WeeClusterBootApplication.class)
class AllHereSentServiceTest{

    @Mock
    private AllHereSentCache allHereSentCache;

    @Autowired
    @InjectMocks
    private AllHereSentService allHereSentService;

    @Test
    void verifyLoad(){

    }

    @Test
    void isAllHereSentTrue(){

        doReturn(TRUE).when(allHereSentCache).get("isAllHereSent");
        assertTrue(allHereSentService.isAllHereSent());
        verify(allHereSentCache, atLeastOnce()).get("isAllHereSent");
        verify(allHereSentCache, atMost(1)).get("isAllHereSent");
    }

    @Test
    void isAllHereSentFalse(){

        doReturn(FALSE).when(allHereSentCache).get("isAllHereSent");
        assertFalse(allHereSentService.isAllHereSent());
        verify(allHereSentCache, atLeastOnce()).get("isAllHereSent");
        verify(allHereSentCache, atMost(2)).get("isAllHereSent");
    }

    @Test
    void setAllHereSentTrue(){

        allHereSentService.setAllHereSent(true);
        verify(allHereSentCache, atLeastOnce()).put("isAllHereSent", Boolean.TRUE);
        verify(allHereSentCache, atMost(1)).put("isAllHereSent", Boolean.TRUE);
    }

    @Test
    void setAllHereSentFalse(){

        allHereSentService.setAllHereSent(false);
        verify(allHereSentCache, atLeastOnce()).put("isAllHereSent", Boolean.FALSE);
        verify(allHereSentCache, atMost(1)).put("isAllHereSent", Boolean.FALSE);
    }
}