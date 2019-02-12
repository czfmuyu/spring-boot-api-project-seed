package com.company.project.service;

import com.company.project.dao.MongoUserDao;
import com.company.project.model.MongoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 *
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email: xiaomo@xiaomo.info
 * <p>
 * Date: 2016/11/15 15:45
 * Copyright(©) 2015 by xiaomo.
 **/

@Service
public class MongoUserService {
    private final MongoUserDao dao;


    public MongoUserService(MongoUserDao dao) {
        this.dao = dao;
    }


    public List<MongoUser> findAll() {
        return dao.findAll();
    }


    public MongoUser findById(Long id) {
        return dao.findOne(id);
    }


    public MongoUser findByName(String userName) {
        return dao.findByUserName(userName);
    }


    public MongoUser add(MongoUser mongoUser) {
        return dao.save(mongoUser);
    }


    public void delete(Long id) {
        dao.delete(id);
    }


    public MongoUser update(MongoUser mongoUser) {
        return dao.save(mongoUser);
    }
}
