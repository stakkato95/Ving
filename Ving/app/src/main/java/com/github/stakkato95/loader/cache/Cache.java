package com.github.stakkato95.loader.cache;

/**
 * Created by Artyom on 15.12.2014.
 */
public interface Cache<Key, Value> {

    Value get(Key url) throws Exception;

    void put(Key url, Value bmp) throws Throwable;

    boolean containsKey(Key url);

}
