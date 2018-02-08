package hoopes.keith.examples.hazelcast.clusterboot.beans;

import com.hazelcast.aggregation.Aggregator;
import com.hazelcast.core.*;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.MapInterceptor;
import com.hazelcast.map.QueryCache;
import com.hazelcast.map.listener.MapListener;
import com.hazelcast.map.listener.MapPartitionLostListener;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.aggregation.Aggregation;
import com.hazelcast.mapreduce.aggregation.Supplier;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.projection.Projection;
import com.hazelcast.query.Predicate;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.*;

/**
 * I need an instance of IMap to initialize my startup listener, but
 * because I don't want to miss an event, I need to register the
 * listener before I create the HazelcastInstance.
 *
 * Of course, if I don't register the listener before the instance then
 * there is a possibility of missing STARTED events.
 *
 * This proxy allows me to do some lazy initialization, while also
 * caching the calls to "put" until the moment I can initialize the
 * actual map. Once initialized, all calls to put will be sent to
 * the new IMap in the order they were entered.
 *
 * I would eventually like a more robust local cache which covers other methods,
 * but this is the minimal I need for the exercise.
 *
 * I would like to add some unit tests to make sure all the methods
 * are mapped correctly, but that's going to take too long. Hopefully
 * I can commit this, and then add some tests before anyone actually
 * looks at it.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@SuppressWarnings({"unused", "FieldMayBeFinal"})//TODO: Needs unit test to ensure methods are mapped correctly.
public class StartupEventMapProxy implements IMap<String, String>, HazelcastInstanceAware{

    /**
     * The reference that will get populated with an actual IMap from the HazelcastInstance
     * This is initially null until the context can be created and this is initialized
     */
    private IMap<String, String> nullMap;

    private ConcurrentLinkedQueue<List<String>> cache;

    private final String name;

    public StartupEventMapProxy(final String name){

        if(name == null){
            throw new IllegalArgumentException("'name' is required when building a StartupEventMapProxy");
        }

        this.name = name;
        cache = new ConcurrentLinkedQueue<>();
        this.nullMap = null;
    }

    /**
     * Gets the HazelcastInstance reference when submitting a Runnable/Callable using Hazelcast ExecutorService.
     *
     * @param hazelcastInstance the HazelcastInstance reference
     */
    @Override
    public void setHazelcastInstance(final HazelcastInstance hazelcastInstance){

        if(hazelcastInstance == null){
            throw new IllegalArgumentException("'HazelcastInstance' is required.");
        }
        initializeMap(hazelcastInstance.getMap(name));
    }

    private void initializeMap(final IMap<String, String> iMap){

        if(iMap == null){
            throw new IllegalArgumentException("'iMap' required. Cannot initialize the  StartupEventMapProxy with a null IMap parameter.");
        }
        nullMap = iMap;
        cache.forEach(
            l -> nullMap.put(
                l.get(0),
                l.get(1)
            ));
        cache.clear();
    }

    private IMap<String, String> map(){

        return Optional
            .ofNullable(this.nullMap)
            .orElseThrow(() ->
                new IllegalStateException("StartupEventMap has not been initialized.")
            );
    }

    @Override
    public void putAll(final Map<? extends String, ? extends String> m){

        map().putAll(m);
    }

    @Override
    public int size(){

        return map().size();
    }

    @Override
    public boolean isEmpty(){

        return map().isEmpty();
    }

    @Override
    public boolean containsKey(final Object key){

        return map().containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value){

        return map().containsValue(value);
    }

    @Override
    public String get(final Object key){

        return map().get(key);
    }

    @Override
    public String put(final String key, final String value){

        if(nullMap == null){
            cache.add(asList(key, value));
        }
        return key;
    }

    @Override
    public String remove(final Object key){

        return map().remove(key);
    }

    @Override
    public boolean remove(final Object key, final Object value){

        return map().remove(key, value);
    }

    @Override
    public void removeAll(final Predicate<String, String> predicate){

        map().removeAll(predicate);
    }

    @Override
    public void delete(final Object key){

        map().delete(key);
    }

    @Override
    public void flush(){

        map().flush();
    }

    @Override
    public Map<String, String> getAll(final Set<String> keys){

        return map().getAll(keys);
    }

    @Override
    public void loadAll(final boolean replaceExistingValues){

        map().loadAll(replaceExistingValues);
    }

    @Override
    public void loadAll(final Set<String> keys, final boolean replaceExistingValues){

        map().loadAll(keys, replaceExistingValues);
    }

    @Override
    public void clear(){

        map().clear();
    }

    @Override
    public ICompletableFuture<String> getAsync(final String key){

        return map().getAsync(key);
    }

    @Override
    public ICompletableFuture<String> putAsync(final String key, final String value){

        return map().putAsync(key, value);
    }

    @Override
    public ICompletableFuture<String> putAsync(final String key, final String value, final long ttl, final TimeUnit timeunit){

        return map().putAsync(key, value, ttl, timeunit);
    }

    @Override
    public ICompletableFuture<Void> setAsync(final String key, final String value){

        return map().setAsync(key, value);
    }

    @Override
    public ICompletableFuture<Void> setAsync(final String key, final String value, final long ttl, final TimeUnit timeunit){

        return map().setAsync(key, value, ttl, timeunit);
    }

    @Override
    public ICompletableFuture<String> removeAsync(final String key){

        return map().removeAsync(key);
    }

    @Override
    public boolean tryRemove(final String key, final long timeout, final TimeUnit timeunit){

        return map().tryRemove(key, timeout, timeunit);
    }

    @Override
    public boolean tryPut(final String key, final String value, final long timeout, final TimeUnit timeunit){

        return map().tryPut(key, value, timeout, timeunit);
    }

    @Override
    public String put(final String key, final String value, final long ttl, final TimeUnit timeunit){

        return map().put(key, value, ttl, timeunit);
    }

    @Override
    public void putTransient(final String key, final String value, final long ttl, final TimeUnit timeunit){

        map().putTransient(key, value, ttl, timeunit);
    }

    @Override
    public String putIfAbsent(final String key, final String value){

        return map().putIfAbsent(key, value);
    }

    @Override
    public String putIfAbsent(final String key, final String value, final long ttl, final TimeUnit timeunit){

        return map().putIfAbsent(key, value, ttl, timeunit);
    }

    @Override
    public boolean replace(final String key, final String oldValue, final String newValue){

        return map().replace(key, oldValue, newValue);
    }

    @Override
    public String replace(final String key, final String value){

        return map().replace(key, value);
    }

    @Override
    public void set(final String key, final String value){

        map().set(key, value);
    }

    @Override
    public void set(final String key, final String value, final long ttl, final TimeUnit timeunit){

        map().set(key, value, ttl, timeunit);
    }

    @Override
    public void lock(final String key){

        map().lock(key);
    }

    @Override
    public void lock(final String key, final long leaseTime, final TimeUnit timeUnit){

        map().lock(key, leaseTime, timeUnit);
    }

    @Override
    public boolean isLocked(final String key){

        return map().isLocked(key);
    }

    @Override
    public boolean tryLock(final String key){

        return map().tryLock(key);
    }

    @Override
    public boolean tryLock(final String key, final long time, final TimeUnit timeunit) throws InterruptedException{

        return map().tryLock(key, time, timeunit);
    }

    @Override
    public boolean tryLock(final String key, final long time, final TimeUnit timeunit, final long leaseTime, final TimeUnit leaseTimeunit) throws InterruptedException{

        return map().tryLock(key, time, timeunit, leaseTime, leaseTimeunit);
    }

    @Override
    public void unlock(final String key){

        map().unlock(key);
    }

    @Override
    public void forceUnlock(final String key){

        map().forceUnlock(key);
    }

    @Override
    public String addLocalEntryListener(final MapListener listener){

        return map().addLocalEntryListener(listener);
    }

    @Override
    @Deprecated
    public String addLocalEntryListener(final EntryListener listener){

        return map().addLocalEntryListener(listener);
    }

    @Override
    public String addLocalEntryListener(final MapListener listener, final Predicate<String, String> predicate, final boolean includeValue){

        return map().addLocalEntryListener(listener, predicate, includeValue);
    }

    @Override
    @Deprecated
    public String addLocalEntryListener(final EntryListener listener, final Predicate<String, String> predicate, final boolean includeValue){

        return map().addLocalEntryListener(listener, predicate, includeValue);
    }

    @Override
    public String addLocalEntryListener(final MapListener listener, final Predicate<String, String> predicate, final String key, final boolean includeValue){

        return map().addLocalEntryListener(listener, predicate, includeValue);
    }

    @Override
    @Deprecated
    public String addLocalEntryListener(final EntryListener listener, final Predicate<String, String> predicate, final String key, final boolean includeValue){

        return map().addLocalEntryListener(listener, predicate, includeValue);
    }

    @Override
    public String addInterceptor(final MapInterceptor interceptor){

        return map().addInterceptor(interceptor);
    }

    @Override
    public void removeInterceptor(final String id){

        map().removeInterceptor(id);
    }

    @Override
    public String addEntryListener(final MapListener listener, final boolean includeValue){

        return map().addEntryListener(listener, includeValue);
    }

    @Override
    @Deprecated
    public String addEntryListener(final EntryListener listener, final boolean includeValue){

        return map().addEntryListener(listener, includeValue);
    }

    @Override
    public boolean removeEntryListener(final String id){

        return map().removeEntryListener(id);
    }

    @Override
    public String addPartitionLostListener(final MapPartitionLostListener listener){

        return map().addPartitionLostListener(listener);
    }

    @Override
    public boolean removePartitionLostListener(final String id){

        return map().removePartitionLostListener(id);
    }

    @Override
    public String addEntryListener(final MapListener listener, final String key, final boolean includeValue){

        return map().addEntryListener(listener, key, includeValue);
    }

    @Override
    @Deprecated
    public String addEntryListener(final EntryListener listener, final String key, final boolean includeValue){

        return map().addEntryListener(listener, key, includeValue);
    }

    @Override
    public String addEntryListener(final MapListener listener, final Predicate<String, String> predicate, final boolean includeValue){

        return map().addEntryListener(listener, predicate, includeValue);
    }

    @Override
    @Deprecated
    public String addEntryListener(final EntryListener listener, final Predicate<String, String> predicate, final boolean includeValue){

        return map().addEntryListener(listener, predicate, includeValue);
    }

    @Override
    public String addEntryListener(final MapListener listener, final Predicate<String, String> predicate, final String key, final boolean includeValue){

        return map().addEntryListener(listener, predicate, key, includeValue);
    }

    @Override
    @Deprecated
    public String addEntryListener(final EntryListener listener, final Predicate<String, String> predicate, final String key, final boolean includeValue){

        return map().addEntryListener(listener, predicate, key, includeValue);
    }

    @Override
    public EntryView<String, String> getEntryView(final String key){

        return map().getEntryView(key);
    }

    @Override
    public boolean evict(final String key){

        return map().evict(key);
    }

    @Override
    public void evictAll(){

        map().evictAll();
    }

    @Override
    public Set<String> keySet(){

        return map().keySet();
    }

    @Override
    public Collection<String> values(){

        return map().values();
    }

    @Override
    public Set<Entry<String, String>> entrySet(){

        return map().entrySet();
    }

    @Override
    public Set<String> keySet(final Predicate predicate){

        return map().keySet(predicate);
    }

    @Override
    public Set<Entry<String, String>> entrySet(final Predicate predicate){

        return map().entrySet(predicate);
    }

    @Override
    public Collection<String> values(final Predicate predicate){

        return map().values(predicate);
    }

    @Override
    public Set<String> localKeySet(){

        return map().localKeySet();
    }

    @Override
    public Set<String> localKeySet(final Predicate predicate){

        return map().localKeySet(predicate);
    }

    @Override
    public void addIndex(final String attribute, final boolean ordered){

        map().addIndex(attribute, ordered);
    }

    @Override
    public LocalMapStats getLocalMapStats(){

        return map().getLocalMapStats();
    }

    @Override
    public Object executeOnKey(final String key, final EntryProcessor entryProcessor){

        return map().executeOnKey(key, entryProcessor);
    }

    @Override
    public Map<String, Object> executeOnKeys(final Set<String> keys, final EntryProcessor entryProcessor){

        return map().executeOnKeys(keys, entryProcessor);
    }

    @Override
    public void submitToKey(final String key, final EntryProcessor entryProcessor, final ExecutionCallback callback){

        map().submitToKey(key, entryProcessor, callback);
    }

    @Override
    public ICompletableFuture submitToKey(final String key, final EntryProcessor entryProcessor){

        return map().submitToKey(key, entryProcessor);
    }

    @Override
    public Map<String, Object> executeOnEntries(final EntryProcessor entryProcessor){

        return map().executeOnEntries(entryProcessor);
    }

    @Override
    public Map<String, Object> executeOnEntries(final EntryProcessor entryProcessor, final Predicate predicate){

        return map().executeOnEntries(entryProcessor, predicate);
    }

    @Override
    public <R> R aggregate(final Aggregator<Entry<String, String>, R> aggregator){

        return map().aggregate(aggregator);
    }

    @Override
    public <R> R aggregate(final Aggregator<Entry<String, String>, R> aggregator, final Predicate<String, String> predicate){

        return map().aggregate(aggregator, predicate);
    }

    @Override
    public <R> Collection<R> project(final Projection<Entry<String, String>, R> projection){

        return map().project(projection);
    }

    @Override
    public <R> Collection<R> project(final Projection<Entry<String, String>, R> projection, final Predicate<String, String> predicate){

        return map().project(projection, predicate);
    }

    @Override
    @Deprecated
    public <SuppliedValue, Result> Result aggregate(final Supplier<String, String, SuppliedValue> supplier, final Aggregation<String, SuppliedValue, Result> aggregation){

        return map().aggregate(supplier, aggregation);
    }

    @Override
    @Deprecated
    public <SuppliedValue, Result> Result aggregate(final Supplier<String, String, SuppliedValue> supplier, final Aggregation<String, SuppliedValue, Result> aggregation, final JobTracker jobTracker){

        return map().aggregate(supplier, aggregation, jobTracker);
    }

    @Override
    public QueryCache<String, String> getQueryCache(final String name){

        return map().getQueryCache(name);
    }

    @Override
    public QueryCache<String, String> getQueryCache(final String name, final Predicate<String, String> predicate, final boolean includeValue){

        return map().getQueryCache(name, predicate, includeValue);
    }

    @Override
    public QueryCache<String, String> getQueryCache(final String name, final MapListener listener, final Predicate<String, String> predicate, final boolean includeValue){

        return map().getQueryCache(name, listener, predicate, includeValue);
    }

    @Override
    public String getPartitionKey(){

        return map().getPartitionKey();
    }

    @Override
    public String getName(){

        return map().getName();
    }

    @Override
    public String getServiceName(){

        return map().getServiceName();
    }

    @Override
    public void destroy(){

        map().destroy();
    }

}
