package com.yikun.cafedataimport.entity;

import lombok.Data;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/9 14:22
 */
@Data
public class CommonVO {
    private String prefix;
    private String path;
    private String meta;
    private int commitSize;
    private String msg;
}
