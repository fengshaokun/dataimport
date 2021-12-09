package com.yikun.cafedataimport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/3 15:25
 */
@Data
@TableName("cafe_product")
public class ProductIndexEntity extends ProductBaseEntity {

    @TableField(exist = false)
    private String changeType;
}
