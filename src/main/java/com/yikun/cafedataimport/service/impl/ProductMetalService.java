package com.yikun.cafedataimport.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.yikun.cafedataimport.entity.ProductIndexEntity;
import com.yikun.cafedataimport.factory.CafeProductFactory;
import com.yikun.cafedataimport.service.AbstractHanldeBillByType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/8 16:33
 */
@Service
public class ProductMetalService extends AbstractHanldeBillByType<ProductIndexEntity> {

    @Value("${upload.product.meta}")
    private String productMeta;

    @Autowired
    private CafeProductFactory cafeProductFactory;

    @Autowired
    private ProductServiceImpl productService;

    @Override
    protected void afterProcess(String prefix, List<ProductIndexEntity> list, String fileName) {
        System.out.println("我进来了");

    }


    @Override
    public void handleAllBusiness(List<ProductIndexEntity> resultList, String fileName) {
        if (CollectionUtils.isNotEmpty(resultList)){
            importProducts(resultList);
        }
    }

    @Override
    protected String[] getFileArray() {
        String[]fileArrays = {productMeta};
        return  fileArrays;
    }

    @Override
    protected String getFileInfix() {
        return "_meta_";
    }

    @Override
    public List<ProductIndexEntity> convertData(List<String> resultList, String fileName) {
        return cafeProductFactory.createMediaFactory(resultList,fileName);
    }

    public  void importProducts(List<ProductIndexEntity> resultList){
        List<List<ProductIndexEntity>> partition = Lists.partition(resultList, 1000);
        partition.forEach(
                i->{
                    productService.saveBatch(i);
                }
        );

    }



}
