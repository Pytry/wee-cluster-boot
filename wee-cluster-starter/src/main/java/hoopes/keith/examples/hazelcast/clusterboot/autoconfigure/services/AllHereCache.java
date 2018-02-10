package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * I put this cache into it's own service because it was easier to configure.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@Service("allHereCache")
public class AllHereCache{

    private boolean allHere = false;

    @Cacheable("AllHereCache")
    public boolean isAllHere(){

        return allHere;
    }

    @CachePut("AllHereCache")
    public void setAllHere(final boolean allHere){

        this.allHere = allHere;
    }
}
