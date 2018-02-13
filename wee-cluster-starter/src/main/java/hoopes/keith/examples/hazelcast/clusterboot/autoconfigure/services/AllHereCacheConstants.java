package hoopes.keith.examples.hazelcast.clusterboot.autoconfigure.services;

/**
 * I went back and forth alot about whether to use a Service or setup Caching with annotations.
 * I also wanted to see if I could come up with a way to do it without having a "Global" cache
 * In the end, I stuck with a simple call to "getMap" because the problem itself was a simple one.
 * For concurrency issues, using the leader role to "lock" the message channel is enough to avoid
 * most cases where two or more nodes would end up printing the message.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
class AllHereCacheConstants{

    static final String ALL_HERE_DISTRIBUTED_MAP = "ALL_HERE_DISTRIBUTED_MAP";

    static final String IS_ALL_HERE_JOB_SENT = "IS_ALL_HERE_JOB_SENT";

}
