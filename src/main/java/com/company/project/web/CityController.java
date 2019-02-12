package com.company.project.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.core.untils.HttpUtil;
import com.company.project.core.untils.StringUtil;
import com.company.project.dao.CommonRedisDao;
import com.company.project.model.City;
import com.company.project.service.CityService;
import com.company.project.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* Created by CodeGenerator on 2018/12/28.
*/
@RestController
@RequestMapping("/bus")
public class CityController {
    public static final String JISU_APPKEY = "153127e907384fe9";// 你的appkey
    public static final Map<String, String> HEADER = new HashMap<>();
    public static final Map<String, String> HOST = new HashMap<>();
    static {
        HEADER.put("Cookie","_vwo_uuid_v2=D649B1B822DD085C18921AB81FD3A7B11|44c8ec7df42ef9387239d63011ad2b17; _ga=GA1.3.1544492587.1545789674; _ga=GA1.4.1544492587.1545789674; _gid=GA1.4.437720987.1547426410");
        HEADER.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8");
        HEADER.put("Accept-Encoding","gzip, deflate");
        HEADER.put("Referer","http://web.chelaile.net.cn/ch5/?showFav=0&switchCity=1&cityId=028&cityName=%E5%98%89%E5%85%B4&hideFooter=0&showTopLogo=1&src=webapp_jiaxingbus&utm_source=webapp_jiaxingbus&utm_medium=entrance&cityVersion=0&supportSubway=0&homePage=around");
        HEADER.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
        HEADER.put("Accept","*/*");
        HEADER.put("Connection","keep-alive");
        HEADER.put("Host","web.chelaile.net.cn");

        HOST.put("玉林市","http://0775.mygolbs.com:8083/ylh5");
        HOST.put("柳州市","http://0772.mygolbs.com:8083/lzh5");
        HOST.put("南宁市","http://0771.mygolbs.com:9292/nnh5");
        HOST.put("桂林市", "http://tmp.api.zsgj.glchuxingwang.com");

    }


    @Resource
    private CityService cityService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final CommonRedisDao redisDao;
    @Autowired
    public CityController(CommonRedisDao dao) {
        this.redisDao = dao;
    }

    @PostMapping("/add")
    public Result add(City city) {
        cityService.save(city);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        cityService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(City city) {
        cityService.update(city);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        City city = cityService.findById(id);
        return ResultGenerator.genSuccessResult(city);
    }

    @GetMapping("/redisAddKV")
    public String redisAddKV(@RequestParam String key, @RequestParam String value) {
        boolean ret  = false;//(redisDao.cacheValue(key, value));
        if(ret){
            return key + "=" + value;
        }
        return "insert redis fail";
    }

    @GetMapping("/redisGetKV")
    public String redisGetKV(@RequestParam String key) {
        for(Map.Entry<String, String> e: HOST.entrySet()){
            if(key.contains(e.getKey())){
                return "true";
            }
        }
        return "";//redisDao.getValue(key);
    }

    @GetMapping("/line")
    public String line(@RequestParam String city,
                       @RequestParam(defaultValue = "1") String transitno) {
        String uri = "http://api.jisuapi.com/transit/line";
        String key = "appkey="+ JISU_APPKEY + "city=" + city + "&transitno=" +transitno + "&appkey="+ JISU_APPKEY ;
        LOGGER.info(uri + "?" +key);
        String redisKey = "jisu-line-" + key;
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus info in redis");
            try {
                Map<String, String> param = new HashMap<>();
                param.put("city",city);
                param.put("cityid","");
                param.put("transitno",transitno);
                param.put("appkey",JISU_APPKEY);
                res = HttpUtil.post(uri, param);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null && res.contains("\"status\":\"0\"")){
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("查询失败，请重试...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/station")
    public String station(@RequestParam String city,
                            @RequestParam String station) {
        String uri = "http://api.jisuapi.com/transit/station";
        String key = "appkey="+ JISU_APPKEY +"&cityid=&city=" + city +  "&station=" +station;
        LOGGER.info(uri+"?"+key);

        String redisKey = "jisu-station-" + key;
        String res = null;// redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus info in redis");
            try {
                Map<String, String> param = new HashMap<>();
                param.put("cityid","");
                param.put("city",city);
                param.put("station",station);
                param.put("appkey",JISU_APPKEY);
                res = HttpUtil.post(uri, param);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null && res.contains("\"status\":\"0\"")){
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("查询失败，请重试...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/city")
    public String city() {
        String uri = "http://api.jisuapi.com/transit/city";
        String key = "appkey="+ JISU_APPKEY;
        LOGGER.info(uri+"?"+key);

        String redisKey = "jisu-city-cityid";
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus info in redis");
            try {
                res = JisuHttpUtil.sendGet(uri+"?"+key, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null && res.contains("\"status\":\"0\"")){
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24*1);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("查询失败，请重试...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/nearby")
    public String nearby(@RequestParam String city,
                          @RequestParam String address) {
        String uri = "http://api.jisuapi.com/transit/nearby";
        String key = "appkey="+ JISU_APPKEY + "&city=" + city +  "&address=" +address;
        Map<String, String> param = new HashMap<>();
        param.put("city",city);
        param.put("address",address);
        param.put("appkey",JISU_APPKEY);

        LOGGER.info(uri+"?"+key);
        String res = "";
        try {
            res = HttpUtil.post(uri, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res == null){
            throw new ServiceException("查询失败，请重试...");
        }

        LOGGER.info(res);
        return res;
    }

    @GetMapping("/station2s")
    public String station2s(@RequestParam String city,
                            @RequestParam String start,
                            @RequestParam String end,
                            @RequestParam(defaultValue = "transit") String type) {
        String uri = "http://api.jisuapi.com/transit/station2s";
        String key = "appkey="+ JISU_APPKEY + "&city=" + city + "&start=" +start + "&end=" +end + "&type=" +type;
        Map<String, String> param = new HashMap<>();
        param.put("city",city);
        param.put("start",start);
        param.put("end",end);
        param.put("type",type);
        param.put("appkey",JISU_APPKEY);
        LOGGER.info(uri+"?"+key);
        String res = "";
        try {
            res = HttpUtil.post(uri, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res == null){
            throw new ServiceException("查询失败，请重试...");
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/baiduGeoConv")
    public String baiduGeoConv(@RequestParam String ak,
                         @RequestParam String coords,
                               @RequestParam String from,
                               @RequestParam String to) {
        String uri = "http://api.map.baidu.com/geoconv/v1/";
        String key = "ak="+ ak + "&coords=" + coords +  "&from=" +from+  "&to=" +to;
        Map<String, String> param = new HashMap<>();
        param.put("ak",ak);
        param.put("coords",coords);
        param.put("from",from);
        param.put("to",to);

        LOGGER.info(uri+"?"+key);
        String res = "";
        try {
            res = HttpUtil.get(uri, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res == null){
            throw new ServiceException("查询失败，请重试...");
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/stationYL")
    public String stationYL(@RequestParam String city,
                          @RequestParam String station,
                            @RequestParam String upperOrDown) {
        String uri = this.getURIByCity(city) + "/searchPassByBuslineByStationName";
        String key = "city=" + city + "&station=" +(station);

        System.out.println(key);
        LOGGER.info(uri);
        LOGGER.info(key);
        String redisKey = "stationYL-" + key;
        String res = null;// redisDao.getValue(redisKey);
        if(res == null){

            Map<String, String> param = new HashMap<>();
            param.put("stationName",station);
            param.put("city",city);
            try {
                res = HttpUtil.post(uri, param);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (res != null){
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("无该公交线路，请输入正确的线路名称...");
            }
        }
        LOGGER.info(res);
        return res;
    }


    @GetMapping("/stationGL")
    public String stationGL(@RequestParam String city,
                            @RequestParam String station,
                            @RequestParam String upperOrDown) {
        String uri = this.getURIByCity(city) + "/lineApi/ver2/queryLinesByStationName";
        String key = "city=" + city + "&station=" +(station);

        System.out.println(key);
        LOGGER.info(uri);
        LOGGER.info(key);
        String redisKey = "stationGL-" + key;
        String res = null;// redisDao.getValue(redisKey);
        if(res == null){
            Map<String, String> param = new HashMap<>();
            param.put("stationName",station);
            param.put("up_down",upperOrDown);
            try {
                res = HttpUtil.post(uri, param);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (res != null){
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("无该公交线路，请输入正确的线路名称...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/get")
    public String bus(@RequestParam String busName,
                      @RequestParam(defaultValue = "1") Integer upperOrDown,
                      @RequestParam(defaultValue = "") String city) {
        String uri = this.getURIByCity(city) + "/searchLineDetailByBusName";
        String key = "busName=" + busName + "&upperOrDown=" +Integer.toString(upperOrDown) +"&city=" +city ;

        System.out.println(key);
        LOGGER.info(uri);
        LOGGER.info(key);
        String redisKey = "businfo-" + key;
        String res = null;// redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus info in redis");
            res = HttpRequest.sendPost(uri, key);
            if (res != null){
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("无该公交线路，请输入正确的线路名称...");
            }
        }
        LOGGER.info(res);
        return res;
    }



    @GetMapping("/lineGL")
    public String lineGL(@RequestParam String busName,
                      @RequestParam(defaultValue = "1") String upperOrDown,
                      @RequestParam(defaultValue = "") String city) {
        String uri = this.getURIByCity(city) + "/lineStationApi/queryLineStations";
        String key = "busName=" + busName + "&upperOrDown=" +(upperOrDown) +"&city=" +city ;

        System.out.println(key);
        LOGGER.info(uri);
        LOGGER.info(key);
        String redisKey = "businfo-" + key;
        String res = null;// redisDao.getValue(redisKey);
        if(res == null){

            Map<String, String> param = new HashMap<>();
            param.put("lineId",busName);
            param.put("up_down",upperOrDown);
            try {
                res = HttpUtil.post(uri, param);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (res != null){
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("无该公交线路，请输入正确的线路名称...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/realtimeGL")
    public String realtimeGL(@RequestParam String busName,
                           @RequestParam(defaultValue = "1") String upperOrDown,
                           @RequestParam String city) {
        String uri = this.getURIByCity(city) + "/lineApi/queryLineBusInfo";
        String key = "buslineId=" + busName + "&upperOrDown=" + upperOrDown +"&city=" +city ;
        LOGGER.info(uri);
        LOGGER.info(key);
        System.out.println(key);
        String redisKey = "realtime-" + key;
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus realtime info in redis");
            Map<String, String> param = new HashMap<>();
            param.put("lineId",busName);
            param.put("travelType",upperOrDown);
//            param.put("city",city);
            try {
                res = HttpUtil.post(uri, param);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (res != null){
                //res = this.updateDistanceAndTime(res, key);
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 30);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("无该公交线路，请输入正确的线路名称...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/realtime")
    public String realtime(@RequestParam String busName,
                      @RequestParam(defaultValue = "1") String upperOrDown,
                      @RequestParam String city) {
        String uri = this.getURIByCity(city) + "/getBusPosition";
        String key = "buslineId=" + busName + "&upperOrDown=" + upperOrDown +"&city=" +city ;
        LOGGER.info(uri);
        LOGGER.info(key);
        System.out.println(key);
        String redisKey = "realtime-" + key;
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus realtime info in redis");
            Map<String, String> param = new HashMap<>();
            param.put("buslineId",busName);
            param.put("upperOrDown",upperOrDown);
            param.put("city",city);
            param.put("lineId",busName);
            param.put("travelType",upperOrDown);
            try {
                res = HttpUtil.post(uri, param);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (res != null){
//                res = this.updateDistanceAndTime(res, key);
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 30);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("无该公交线路，请输入正确的线路名称...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    private String updateDistanceAndTime(String res, String key) {
        JSONArray buses = JSON.parseArray(res);
        if(buses == null || buses.size() == 0){
            return null;
        }
        List<JSONObject> list = new ArrayList<>();
        for(Object bus: buses){
            JSONObject point = ((JSONObject)bus).getJSONObject("point");
            JSONObject station = ((JSONObject)bus).getJSONObject("station");
            LngLat currentGps = new LngLat(point.getDoubleValue("longitude"),
                    point.getDoubleValue("latitude"));
//            JSONObject nextStation = this.getStationByName(station.getString("name"), key);
            LngLat nearestStationGps = new LngLat(point.getDoubleValue("longitude"),
                    point.getDoubleValue("latitude"));
            double distance = AMapUtils.calculateLineDistance(currentGps, nearestStationGps);
            list.add((JSONObject)bus);
        }
        return JSON.toJSONString(list);
    }

    private JSONObject getStationByName(String stationName, String key){
        String redisKey = "businfo-" + key;
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            return null;
        }
        JSONObject lineInfo = JSON.parseObject(res) ;
        JSONArray stations = lineInfo.getJSONArray("stations");
        for(int i =0; i < stations.size(); i++){
            JSONObject station = (JSONObject)stations.get(i);
            if (stationName.equals(station.getString("name"))){
                station.put("index",i);
                return station;
            }
        }
        return  null;
    }
    private String getURIByCity(String city){
        if (city == null){
            return null;
        }
        if(!city.contains("市")){
            city = city + "市";
        }
        String uri = HOST.get(city);
        return  uri;
    }

    @GetMapping("/searchLine")
    public String searchLine(@RequestParam Integer type,
                             @RequestParam String startP,
                             @RequestParam String endP,
                             @RequestParam String startX,
                             @RequestParam String startY,
                             @RequestParam String endX,
                             @RequestParam String endY,
                             @RequestParam  String city) {
        String uri = this.getURIByCity(city) + "/getBusPosition";
        String key = "type=" + type
                + "&startP=" +startP
                + "&endP=" +endP
                + "&startX=" +startX
                + "&startY=" +startY
                + "&endX=" +endX
                + "&endY=" +endY
                + "&city=" +city ;
        System.out.println(key);
        LOGGER.info(uri);
        LOGGER.info(key);

        String redisKey = "line-" + key;
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus realtime info in redis");
            res = HttpRequest.sendPost(uri, key);
            if (res != null){
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("无线路...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/cll/city")
    public String cityCLL(@RequestParam(defaultValue = "h5") String s,
                          @RequestParam(defaultValue = "3.3.19") String v,
                          @RequestParam(defaultValue = "webapp_jiaxingbus") String src,
                          @RequestParam(defaultValue = "browser_1547426409523") String userId,
                          @RequestParam(defaultValue = "browser_1547426409523") String h5Id,
                          @RequestParam(defaultValue = "") String unionId,
                          @RequestParam(defaultValue = "028") String cityId) {
        String uri = "http://web.chelaile.net.cn/wwd/ncitylist";

        String redisKey = "chelaile-city";
        String res = null;// redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus info in redis");
            try {
                Map<String, String> param = new HashMap<>();
                param.put("s",s);
                param.put("v",v);
                param.put("src",src);
                param.put("userId",userId);
                param.put("h5Id",h5Id);
                param.put("unionId",unionId);
                param.put("cityId",cityId);
                res = HttpUtil.get(uri, param, HEADER);
                LOGGER.info(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null){
                boolean ins = false;// redisDao.cacheValue(redisKey, res, 60*60*24*15);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("查询失败，请重试...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/cll/line")
    public String lineCLL(@RequestParam(defaultValue = "h5") String s,
                          @RequestParam(defaultValue = "3.3.19") String v,
                          @RequestParam(defaultValue = "webapp_jiaxingbus") String src,
                          @RequestParam(defaultValue = "browser_1547426409523") String userId,
                          @RequestParam(defaultValue = "browser_1547426409523") String h5Id,
                          @RequestParam(defaultValue = "") String unionId,
                          @RequestParam(defaultValue = "1") String sign,
                          @RequestParam String cityId,
                          @RequestParam String lineId,
                          @RequestParam(defaultValue = "") String lineName,
                          @RequestParam(defaultValue = "") String direction,
                          @RequestParam(defaultValue = "") String stationName,
                          @RequestParam(defaultValue = "") String nextStationName,
                          @RequestParam(defaultValue = "") String targetOrder,
                          @RequestParam(defaultValue = "") String lineNo) {
        String uri = "http://web.chelaile.net.cn/api/bus/line!lineDetail.action";
        String key = "cityId=" + cityId + "&lineId=" +lineId;
        String redisKey = "chelaile-line-"+key;
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus info in redis");
            try {
                Map<String, String> param = new HashMap<>();
                param.put("s",s);
                param.put("v",v);
                param.put("src",src);
                param.put("userId",userId);
                param.put("h5Id",h5Id);
                param.put("unionId",unionId);
                param.put("sign",sign);
                param.put("cityId",cityId);
                param.put("lineId",lineId);
                param.put("lineName",lineName);
                param.put("direction",direction);
                param.put("stationName",stationName);
                param.put("nextStationName",nextStationName);
                param.put("targetOrder",targetOrder);
                param.put("lineNo",lineNo);
                res = HttpUtil.get(uri, param, HEADER);
                LOGGER.info(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null && res.contains("\"status\":\"00\"")){
                res = this.formatResponse(res);
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24*15);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("查询失败，请重试...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/cll/station")
    public String stationCLL(@RequestParam(defaultValue = "h5") String s,
                          @RequestParam(defaultValue = "3.3.19") String v,
                          @RequestParam(defaultValue = "webapp_jiaxingbus") String src,
                          @RequestParam(defaultValue = "browser_1547426409523") String userId,
                          @RequestParam(defaultValue = "browser_1547426409523") String h5Id,
                          @RequestParam(defaultValue = "") String unionId,
                          @RequestParam(defaultValue = "1") String sign,
                          @RequestParam(defaultValue = "-1") String destSId,
                          @RequestParam String stationId,
                          @RequestParam String cityId) {

        String uri = "http://web.chelaile.net.cn/api/bus/stop!stationDetail.action";
        String key = "cityId=" + cityId + "&stationId=" +stationId;

        String redisKey = "chelaile-station-"+key;
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus info in redis");
            try {
                Map<String, String> param = new HashMap<>();
                param.put("s",s);
                param.put("v",v);
                param.put("src",src);
                param.put("userId",userId);
                param.put("h5Id",h5Id);
                param.put("unionId",unionId);
                param.put("sign",sign);
                param.put("destSId",destSId);
                param.put("stationId",stationId);
                param.put("cityId",cityId);
                res = HttpUtil.get(uri, param, HEADER);
                LOGGER.info(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null && res.contains("\"status\":\"00\"")){
                res = this.formatResponse(res);
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 60*60*24*15);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("查询失败，请重试...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/cll/realtime")
    public String realtimeCLL(@RequestParam(defaultValue = "h5") String s,
                              @RequestParam(defaultValue = "3.3.19") String v,
                              @RequestParam(defaultValue = "webapp_jiaxingbus") String src,
                              @RequestParam(defaultValue = "browser_1547426409523") String userId,
                              @RequestParam(defaultValue = "browser_1547426409523") String h5Id,
                              @RequestParam(defaultValue = "") String unionId,
                              @RequestParam(defaultValue = "1") String sign,
                              @RequestParam String cityId,
                              @RequestParam String lineId,
                              @RequestParam(defaultValue = "") String stationName,
                              @RequestParam(defaultValue = "") String nextStationName,
                              @RequestParam(defaultValue = "") String targetOrder) {

        String uri = "http://web.chelaile.net.cn/api/bus/line!timeTable.action";
        String key = "cityId=" + cityId + "&lineId=" +lineId + "&targetOrder=" +targetOrder;

        String redisKey = "chelaile-realtime-"+key;
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus info in redis");
            try {
                Map<String, String> param = new HashMap<>();
                param.put("s",s);
                param.put("v",v);
                param.put("src",src);
                param.put("userId",userId);
                param.put("h5Id",h5Id);
                param.put("unionId",unionId);
                param.put("sign",sign);
                param.put("cityId",cityId);
                param.put("lineId",lineId);
                param.put("stationName",stationName);
                param.put("nextStationName",nextStationName);
                param.put("targetOrder",targetOrder);
                res = HttpUtil.get(uri, param, HEADER);
                LOGGER.info(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null && res.contains("\"status\":\"00\"")){
                res = this.formatResponse(res);
                boolean ins = false;//redisDao.cacheValue(redisKey, res, 20);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("查询失败，请重试...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    @GetMapping("/cll/nearby")
    public String nearbyCLL(@RequestParam(defaultValue = "h5") String s,
                              @RequestParam(defaultValue = "3.3.19") String v,
                              @RequestParam(defaultValue = "webapp_jiaxingbus") String src,
                              @RequestParam(defaultValue = "browser_1547426409523") String userId,
                              @RequestParam(defaultValue = "browser_1547426409523") String h5Id,
                              @RequestParam(defaultValue = "") String unionId,
                              @RequestParam(defaultValue = "1") String sign,
                              @RequestParam String cityId,
                              @RequestParam(defaultValue = "2")  String cityState,
                              @RequestParam(defaultValue = "") String geo_lat,
                              @RequestParam(defaultValue = "") String geo_lng,
                              @RequestParam String lat,
                              @RequestParam String lng,
                              @RequestParam(defaultValue = "wgs") String gpstype) {
        String uri = "http://web.chelaile.net.cn/api/bus/stop!nearlines.action";
        String res = null;
        try {
            Map<String, String> param = new HashMap<>();
            param.put("s",s);
            param.put("v",v);
            param.put("src",src);
            param.put("userId",userId);
            param.put("h5Id",h5Id);
            param.put("unionId",unionId);
            param.put("sign",sign);
            param.put("cityId",cityId);
            param.put("cityState",cityState);
            param.put("geo_lat",geo_lat);
            param.put("geo_lng",geo_lng);
            param.put("lat",lat);
            param.put("lng",lng);
            param.put("gpstype",gpstype);
            res = HttpUtil.get(uri, param, HEADER);
            LOGGER.info(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null && res.contains("\"status\":\"00\"")){
                res = this.formatResponse(res);
            } else {
                throw new ServiceException("查询失败，请重试...");
            }
        LOGGER.info(res);
        return res;
    }


    @GetMapping("/cll/search")
    public String searchCLL(@RequestParam(defaultValue = "h5") String s,
                              @RequestParam(defaultValue = "3.3.19") String v,
                              @RequestParam(defaultValue = "webapp_jiaxingbus") String src,
                              @RequestParam(defaultValue = "browser_1547426409523") String userId,
                              @RequestParam(defaultValue = "browser_1547426409523") String h5Id,
                              @RequestParam(defaultValue = "") String unionId,
                              @RequestParam(defaultValue = "1") String sign,
                              @RequestParam String cityId,
                              @RequestParam String key,
                              @RequestParam(defaultValue = "3") String count) {
        String uri = "http://web.chelaile.net.cn/api/basesearch/client/clientSearch.action";
        String keys = "cityId=" + cityId + "&key=" +key;

        String redisKey = "chelaile-search-"+keys;
        String res = null;//redisDao.getValue(redisKey);
        if(res == null){
            LOGGER.warn("no bus info in redis");
            try {
                Map<String, String> param = new HashMap<>();
                param.put("s",s);
                param.put("v",v);
                param.put("src",src);
                param.put("userId",userId);
                param.put("h5Id",h5Id);
                param.put("unionId",unionId);
                param.put("sign",sign);
                param.put("cityId",cityId);
                param.put("key",key);
                param.put("count",count);
                res = HttpUtil.get(uri, param, HEADER);
                LOGGER.info(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null && res.contains("\"status\":\"00\"")){
                res = this.formatResponse(res);
                boolean ins = false;// redisDao.cacheValue(redisKey, res, 60*60*24*15);
                if(!ins){
                    LOGGER.warn("insert redis fail. " + redisKey);
                }else{
                    LOGGER.warn("insert redis success. " + redisKey);
                }
            } else {
                throw new ServiceException("查询失败，请重试...");
            }
        }
        LOGGER.info(res);
        return res;
    }

    private String formatResponse(String resp){
        if(resp == null){
            return null;
        }
        resp = resp.replace("**YGKJ","");
        resp = resp.replace("YGKJ##","");
        return resp;
    }
}
