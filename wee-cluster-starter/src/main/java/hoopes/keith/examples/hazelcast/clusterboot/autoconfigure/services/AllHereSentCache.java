package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@CacheConfig(cacheNames = "AllHereSentCache")
public class AllHereSentCache{

    private Map<String, Boolean> allHereSent = new ConcurrentHashMap<>();

    @CachePut(key = "#allHereSentKey", value = "#allHereSentValue")
    public Boolean put(final String allHereSentKey, final Boolean allHereSentValue){

        return this.allHereSent.put(allHereSentKey, allHereSentValue != null && allHereSentValue);
    }

    @Cacheable(key = "#allHereSentKey", value = "#allHereSentValue")
    public Boolean get(final String allHereSentKey){

        Boolean aBoolean = allHereSent.get(allHereSentKey);
        return aBoolean != null && aBoolean;
    }
}
