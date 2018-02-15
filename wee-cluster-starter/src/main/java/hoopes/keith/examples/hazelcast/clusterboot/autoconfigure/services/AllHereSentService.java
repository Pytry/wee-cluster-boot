package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class helps to simplify the interacts with the {@link AllHereSentCache}.
 * What is really needed is a simple "Is it sent or not?" type of operation, and
 * there should be any need for interacting with cache/amp "keys".
 *
 * So in other words, what I'm striving for here is a method for sharing configuration, properties,
 * and state across all nodes of the cluster without the need for an additional application or
 * service (such as config server or spring-admin).
 */
@Service
public class AllHereSentService{

    private final AllHereSentCache allHereSentCache;

    @Autowired
    public AllHereSentService(final AllHereSentCache allHereSentCache){

        if(allHereSentCache == null){
            throw new IllegalArgumentException("AllHereSentCache is required.");
        }
        this.allHereSentCache = allHereSentCache;
    }

    /**
     * @return true if the "All here" notification was already sent/printed, or false otherwise.
     */
    public boolean isAllHereSent(){

        if(allHereSentCache == null){
            throw new IllegalStateException("AllHereSentCache was null, even though the constructor forbids it.");
        }
        return allHereSentCache.get("isAllHereSent");
    }

    /**
     * Sets the state of the cluster so that every member can "know"
     * that the "All Here" notification was sent.
     *
     * @param isAllHereSent Set to true to indicate the message was sent, or false otherwise.
     */
    public void setAllHereSent(final boolean isAllHereSent){

        if(allHereSentCache == null){
            throw new IllegalStateException("AllHereSentCache was null, even though the constructor forbids it.");
        }
        allHereSentCache.put("isAllHereSent", isAllHereSent);
    }
}
