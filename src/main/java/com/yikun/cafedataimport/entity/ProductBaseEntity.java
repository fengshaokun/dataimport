package com.yikun.cafedataimport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/3 15:19
 */
@Data
public class ProductBaseEntity implements Serializable {

    @TableId(value = "spuId")
    private String spuId;

    @TableField(value = "skuId")
    private String skuId;

    @TableField(value = "cnName")
    private String cnName;

    @TableField(value = "enName")
    private String enName;

    @TableField(value = "brand")
    private String brand;

    @TableField(value = "goodMarketArea")
    private String goodMarketArea;

    @TableField(value = "category")
    private String category;

    @TableField(value = "retailPrice")
    private Double retailPrice;

    @TableField(value = "memberPrice")
    private Double memberPrice;

    @TableField(value = "optimalPrice")
    private Double optimalPrice;

    @TableField(value = "valid")
    private String valid;

    @TableField(value = "salesVolume")
    private Integer salesVolume;

    @TableField(value = "pageView")
    private Integer pageView;

    @TableField(value = "delStatus")
    private Integer delstatus;

    @TableField(value = "brandId")
    private String brandId;


}
