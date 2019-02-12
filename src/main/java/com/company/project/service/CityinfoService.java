package com.company.project.service;

import com.company.project.model.CityInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author : xiaomo
 */
@Component
@CacheConfig(cacheNames = "CityinfoService")
public class CityinfoService {

    @Cacheable
    public CityInfo getCity(int id, String city) {
        return new CityInfo(id, city);
    }
}
