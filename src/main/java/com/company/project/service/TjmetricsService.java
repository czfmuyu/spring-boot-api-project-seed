package com.company.project.service;

import com.company.project.dao.TjmetricsDao;
import org.bson.Document;
import org.springframework.stereotype.Service;

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
 * Date: 2016/11/15 15:45
 * Copyright(©) 2015 by xiaomo.
 **/

@Service
public class TjmetricsService {
    private TjmetricsDao dao =  new TjmetricsDao();

    public List<Document>   findByCityId(int cityId){
        return dao.findByCityId(cityId);
    }

    public List<Document>   findByCityIdDate(int cityId, String date){
        return dao.findByCityIdDate(cityId, date);
    }

    public List<Document>   findByCityIdDateType(int cityId, String date, String type){
        return dao.findByCityIdDateType(cityId, date, type);
    }

    public List<Document>   findByDateType(String date, String type){
        return dao.findByDateType(date, type);
    }

    public List<Document>   findByType(String type){
        return dao.findByType(type);
    }

    public List<Document> getCitys() {
        return dao.getCitys();
    }
}
