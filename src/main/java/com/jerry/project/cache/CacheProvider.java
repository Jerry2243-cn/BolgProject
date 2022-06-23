package com.jerry.project.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class CacheProvider {

    private final static Map<String, CacheData> cacheDatas = new ConcurrentHashMap<>();
    private final static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);

    /**
     * 默认不超时缓存
     * @param key key
     * @param val
     */
    public synchronized void put(String key, Object val){
        cacheDatas.remove(key);
        this.put(key, val, -1L);
    }

    /**
     * 超时缓存清除
     * @param key key
     * @param val
     * @param expire 超时时间
     */
    public synchronized void put(String key, Object val, Long expire){
        cacheDatas.remove(key);
        if (expire > 0) {
            executor.schedule(()->{
                synchronized (this){
                    cacheDatas.remove(key);
                }
            },expire, TimeUnit.MILLISECONDS);
            cacheDatas.put(key,new CacheData(val,expire));
        } else {
            cacheDatas.put(key,new CacheData(val,-1L));
        }
    }

    /**
     * 缓存大小
     * @return 缓存大小
     */
    public synchronized int size(){
        return cacheDatas.size();
    }

    /**
     * 获取缓存内容
     * @param key key
     * @return
     * @param <T>
     */
    public synchronized <T> T get(String key){
        CacheData cacheData = cacheDatas.get(key);
        return cacheData == null ? null : (T)cacheData.data;
    }

    /**
     * 清除缓存
     * @param key key
     * @return
     * @param <T>
     */
    public synchronized <T> T remove(String key){
        CacheData cacheData = cacheDatas.remove(key);
        return cacheData == null ? null : (T)cacheData.data;
    }

    public synchronized void removeALl(){
        cacheDatas.clear();
    }


    class CacheData {

        public Object data;

        public Long expire;

        public CacheData(Object data, Long expire) {
            this.data = data;
            this.expire = expire;
        }
    }
}
