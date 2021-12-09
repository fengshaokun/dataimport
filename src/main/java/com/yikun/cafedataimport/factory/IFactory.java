package com.yikun.cafedataimport.factory;

import java.util.List;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/3 15:50
 */
public interface IFactory<T> {


    List<T> createMediaFactory(List<String>lineString,String dataSource);
}
