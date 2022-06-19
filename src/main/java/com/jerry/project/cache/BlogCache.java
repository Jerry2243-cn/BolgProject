package com.jerry.project.cache;

import org.springframework.stereotype.Component;

@Component
public class BlogCache {

    private final CacheProvider cacheProvider = new CacheProvider();

}
