package com.company.project.web;

import com.company.project.core.ResultGenerator;
import com.company.project.core.base.Result;
import com.company.project.dao.CommonRedisDao;
import com.company.project.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
 * Date: 2016/11/14 17:25
 * Copyright(©) 2015 by xiaomo.
 **/

@RestController
@RequestMapping("/redis")
public class TestController {

    private final CommonRedisDao dao;

    @Autowired
    public TestController(CommonRedisDao dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "get/{key}", method = RequestMethod.GET)
    public Result<String> find(@PathVariable("key") String key) {
        String value = dao.getValue(key);
        return new Result<>(value);
    }
    @PostMapping("/add")
    public Result<Boolean> add(@RequestParam String key, @RequestParam String value) {
        return new Result<>(dao.cacheValue(key, value));
    }

    @RequestMapping(value = "del/{key}", method = RequestMethod.GET)
    public Result<Boolean> del(@PathVariable("key") String key) {
        return new Result<>(dao.removeValue(key));
    }

    @RequestMapping(value = "count/{key}", method = RequestMethod.GET)
    public Result<Long> count(@PathVariable("key") String key) {
        return new Result<>(dao.getListSize(key));
    }


}
