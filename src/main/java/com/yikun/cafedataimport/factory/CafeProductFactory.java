package com.yikun.cafedataimport.factory;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikun.cafedataimport.entity.ProductBaseEntity;
import com.yikun.cafedataimport.entity.ProductIndexEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/9 10:00
 */
@Service
@Slf4j
public class CafeProductFactory implements IFactory<ProductIndexEntity> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<ProductIndexEntity> createMediaFactory(List<String> lineString, String dataSource) {

     /*   List<ProductIndexEntity> productList = new ArrayList<>();
        lineString.forEach(e->{
            productList.add(createVO(e));
        });*/
        List<ProductIndexEntity> productList  = lineString.stream().map(b->{
            ProductIndexEntity p = null;
            try {
                p = createVO(b);
            } catch (Exception e) {
               log.error("文件名：{},数据:{},createMediaFactory 发生异常：{}",dataSource,b,e);
            }
            return p;
        } ).filter(Objects::nonNull).collect(Collectors.toList());
        return productList;
    }

    public ProductIndexEntity createVO(String line)  {
        JSONObject jsonObject = JSONObject.parseObject(line);
        ProductIndexEntity productIndexEntity = jsonObject.toJavaObject(ProductIndexEntity.class);
        return productIndexEntity;

    }
}
