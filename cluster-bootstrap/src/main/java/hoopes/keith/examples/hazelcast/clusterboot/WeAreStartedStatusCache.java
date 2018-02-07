package hoopes.keith.examples.hazelcast.clusterboot;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright ${year}
 */
@Service
public class WeAreStartedStatusCache{

    private final Map<String, Boolean> cache = new ConcurrentHashMap<>(1);

    @CachePut("WeAreStartedStatusCache")
    public void setWeAreStarted(final boolean processed){

        cache.put("WE_ARE_STARTED", processed);
    }

    @Cacheable("WeAreStartedStatusCache")
    public boolean isWeAreStarted(){

        return Optional
            .ofNullable(
                cache.get("WE_ARE_STARTED"))
            .orElse(Boolean.FALSE);
    }

}
