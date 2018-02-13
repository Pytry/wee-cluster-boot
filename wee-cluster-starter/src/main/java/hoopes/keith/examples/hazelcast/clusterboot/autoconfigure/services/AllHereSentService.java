package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllHereSentService{

    private AllHereSentCache allHereSentCache;

    @Autowired
    public AllHereSentService(final AllHereSentCache allHereSentCache){

        this.allHereSentCache = allHereSentCache;
    }

    public boolean isAllHereSent(){

        return allHereSentCache.get("isAllHereSent");
    }

    public void setAllHereSent(final boolean isAllHereSent){

        allHereSentCache.put("isAllHereSent", isAllHereSent);
    }
}
