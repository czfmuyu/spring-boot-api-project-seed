package com.company.project.service.impl;

import com.company.project.dao.CityMapper;
import com.company.project.model.City;
import com.company.project.service.CityService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class CityServiceImpl extends AbstractService<City> implements CityService {
    @Resource
    private CityMapper cityMapper;

}
