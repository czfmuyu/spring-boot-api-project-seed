package com.company.project.web;

import com.company.project.core.base.Result;
import com.company.project.core.constant.CodeConst;
import com.company.project.model.MongoUser;
import com.company.project.service.MongoUserService;
import com.company.project.service.TjmetricsService;
import io.swagger.annotations.Api;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
 * Date: 2016/11/15 15:49
 * Copyright(©) 2015 by xiaomo.
 **/

@RestController
@RequestMapping("mongodb")
@Api("mongodb測試")
public class TjmetricsController {

    private final TjmetricsService service;

    @Autowired
    public TjmetricsController(TjmetricsService service) {
        this.service = service;
    }

    @RequestMapping(value = "findByCityId/{cityId}", method = RequestMethod.GET)
    public List<Document> findByCityId(@PathVariable("cityId")int cityId){
        return this.service.findByCityId(cityId);
    }

    @RequestMapping(value = "findByCityIdDate/{cityId}/{date}", method = RequestMethod.GET)
    public List<Document>   findByCityIdDate(@PathVariable("cityId")int cityId,
                                             @PathVariable("date")String date){
        return this.service.findByCityIdDate(cityId, date);
    }

    @RequestMapping(value = "findByCityIdDateType/{cityId}/{date}/{type}", method = RequestMethod.GET)
    public List<Document>   findByCityIdDateType(@PathVariable("cityId") int cityId,
                                                 @PathVariable("date") String date,
                                                 @PathVariable("type") String type){
        return this.service.findByCityIdDateType(cityId, date, type);
    }

    @RequestMapping(value = "findByDateType/{date}/{type}", method = RequestMethod.GET)
    public List<Document>  findByDateType(@PathVariable("date") String date,
                                          @PathVariable("type") String type){
        return this.service.findByDateType(date, type);
    }

    @RequestMapping(value = "findByType/{type}", method = RequestMethod.GET)
    public List<Document>   findByType(@PathVariable("type") String type){
        return this.service.findByType(type);
    }


    @RequestMapping(value = "getCitys", method = RequestMethod.GET)
    public List<Document>   getCitys(){
        return this.service.getCitys();
    }

}
