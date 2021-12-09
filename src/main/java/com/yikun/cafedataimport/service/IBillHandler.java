package com.yikun.cafedataimport.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/3 16:44
 */
public interface IBillHandler<T> {

    /**
     * 话单文件解析异步处理
     * @param prefix
     * @return
     */
    List<CompletableFuture<Void>> handleBillAsync(String prefix);

    /**
     * 话单文件解析同步处理
     * @param prefix
     */
    void handleBillSync(String prefix);

    /**
     * 文件解析后 pojo转换
     * @param resultList
     * @param fileName
     * @return
     */
    List<T> convertData(List<String>resultList,String fileName);

    /**
     * 全量抽象类
     * @param resultList
     * @param fileName
     */
    default void handleAllBusiness(List<T>resultList,String fileName){}

    /**
     * 增量抽象类
     * @param resultList
     * @param fileName
     */
    default void handleIncrementBusiness(List<T>resultList,String fileName){}
}
