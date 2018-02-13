package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@CacheConfig(cacheNames = "AllHereSentCache")
public class AllHereSentCache{

    private Map<String, Boolean> allHereSent = new ConcurrentHashMap<>(1);

    @CachePut(key = "#allHereSentKey", value = "#allHereSentValue")
    public Boolean put(final String allHereSentKey, final Boolean allHereSentValue){

        return this.allHereSent.put(allHereSentKey, allHereSentValue);
    }

    @Cacheable(key = "#allHereSentKey")
    public Boolean get(final String allHereSentKey){

        return Optional
            .ofNullable(
                allHereSent.get(allHereSentKey))
            .orElse(Boolean.FALSE);
    }
}
