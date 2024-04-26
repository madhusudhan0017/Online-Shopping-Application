package com.retail.e_com.cache;

import java.time.Duration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CacheStore<T>{

private Cache<String, T> cache;
	
	
	public CacheStore(int maxAge){
		this.cache=CacheBuilder.newBuilder()
				.expireAfterWrite( Duration.ofMinutes(maxAge))
				.concurrencyLevel(Runtime.getRuntime().availableProcessors())
				.build();
	}
	
	
	public void add(String st,T value) {
		cache.put(st, value);
	}
	
	public T get(String key){
		return cache.getIfPresent(key);
	}
	
	public void remove(String Key) {
         cache.invalidate(Key);
	}
	
}
