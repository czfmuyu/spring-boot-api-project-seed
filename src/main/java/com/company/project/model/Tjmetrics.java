package com.company.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;


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
 * Date: 2016/11/15 15:39
 * Description: 用户实体类
 * Copyright(©) 2015 by xiaomo.
 **/

@Data
@ToString(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Tjmetrics {

    @ApiModelProperty(value = "城市id")
    private int cityId;

    @ApiModelProperty(value = "时间分区")
    private String date;

    @ApiModelProperty(value = "指标类型")
    private String type;

    @ApiModelProperty(value = "指标数据")
    private String data;

}
