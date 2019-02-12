package com.company.project.dao;

import com.company.project.model.MongoUser;
import com.company.project.model.Tjmetrics;
import com.company.project.utils.MongoDBUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
 * Date: 2016/11/15 15:42
 * Description: 用户实体类
 * Copyright(©) 2015 by xiaomo.
 **/

@Repository
public class TjmetricsDao {
    MongoDBUtil mongoDBUtil = MongoDBUtil.getMongoDBUtilInstance();
    MongoClient meiyaClient = mongoDBUtil.getMongoConnect("127.0.0.1",27017);
    MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"gossip","tjmetrics");

    /**
     * 根据城市id查询数据
     *
     * @param cityId
     * @return
     */
    public List<Document> findByCityId(int cityId){
        List<Object> citys = Lists.newArrayList(cityId);
//        List<Object> types = Lists.newArrayList("occupant_dist","area_dist");

        Map<String,Object> queryParams = Maps.newHashMap();
        queryParams.put("cityId",new Document("$in",citys));
//        queryParams.put("type",new Document("$in",types));
        FindIterable<Document> documents = mongoDBUtil.queryDocumentInMultiCondition(collection,queryParams);
        mongoDBUtil.printDocuments(documents);
        List<Document> docs = Lists.newArrayList();
        for (Document d : documents) {
            docs.add(d);
        }
        return docs;
    }


    /**
     * 根据城市id + date 查询数据
     *
     * @param cityId
     * @return
     */
    public List<Document> findByCityIdDate(int cityId, String date){
        List<Object> citys = Lists.newArrayList(cityId);
        List<Object> dates = Lists.newArrayList(date);

        Map<String,Object> queryParams = Maps.newHashMap();
        queryParams.put("cityId",new Document("$in",citys));
        queryParams.put("date",new Document("$in",dates));
        FindIterable<Document> documents = mongoDBUtil.queryDocumentInMultiCondition(collection,queryParams);
        mongoDBUtil.printDocuments(documents);
        List<Document> docs = Lists.newArrayList();
        for (Document d : documents) {
            docs.add(d);
        }
        return docs;
    }


    /**
     * 根据城市id + date + type 查询数据
     *
     * @param cityId
     * @return
     */
    public List<Document>  findByCityIdDateType(int cityId, String date, String type){
        List<Object> citys = Lists.newArrayList(cityId);
        List<Object> dates = Lists.newArrayList(date);
        List<Object> types = Lists.newArrayList(type);

        Map<String,Object> queryParams = Maps.newHashMap();
        queryParams.put("cityId",new Document("$in",citys));
        queryParams.put("date",new Document("$in",dates));
        queryParams.put("type",new Document("$in",types));
        FindIterable<Document> documents = mongoDBUtil.queryDocumentInMultiCondition(collection,queryParams);
        mongoDBUtil.printDocuments(documents);
        List<Document> docs = Lists.newArrayList();
        for (Document d : documents) {
            docs.add(d);
        }
        return docs;
    }


    /**
     * 根据date + type查询数据
     *
     * @param date
     * @param type
     * @return
     */
    public List<Document> findByDateType(String date, String type){
        List<Object> dates = Lists.newArrayList(date);
        List<Object> types = Lists.newArrayList(type);

        Map<String,Object> queryParams = Maps.newHashMap();
        queryParams.put("date",new Document("$in",dates));
        queryParams.put("type",new Document("$in",types));
        FindIterable<Document> documents = mongoDBUtil.queryDocumentInMultiCondition(collection,queryParams);
        mongoDBUtil.printDocuments(documents);
        List<Document> docs = Lists.newArrayList();
        for (Document d : documents) {
            docs.add(d);
        }
        return docs;
    }


    /**
     * 根据type 查询数据
     *
     * @param type
     * @return
     */
    public List<Document> findByType(String type){
        List<Object> types = Lists.newArrayList(type);

        Map<String,Object> queryParams = Maps.newHashMap();
        queryParams.put("type",new Document("$in",types));
        FindIterable<Document> documents = mongoDBUtil.queryDocumentInMultiCondition(collection,queryParams);
        mongoDBUtil.printDocuments(documents);
        List<Document> docs = Lists.newArrayList();
        for (Document d : documents) {
            docs.add(d);
        }
        return docs;
    }

    /**
     * 根据type 查询数据
     *
     * @param type
     * @return
     */
    public List<Document> getCitys(){
        Map<String,Object> queryParams = Maps.newHashMap();
        MongoCollection<Document> cl = mongoDBUtil.getMongoCollection(meiyaClient,"gossip","tjcity");
        FindIterable<Document> documents = mongoDBUtil.queryDocumentInMultiCondition(cl,queryParams);
        mongoDBUtil.printDocuments(documents);
        List<Document> docs = Lists.newArrayList();
        for (Document d : documents) {
            docs.add(d);
        }
        return docs;
    }
}
