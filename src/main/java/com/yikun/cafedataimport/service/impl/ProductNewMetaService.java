package com.yikun.cafedataimport.service.impl;

import com.google.common.collect.Lists;
import com.yikun.cafedataimport.entity.CommonVO;
import com.yikun.cafedataimport.entity.ProductIndexEntity;
import com.yikun.cafedataimport.factory.CafeProductFactory;
import com.yikun.cafedataimport.service.MediaReaderService;
import com.yikun.cafedataimport.service.ProductMetaService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/9 14:38
 */
@Service
public class ProductNewMetaService extends MediaReaderService implements ProductMetaService {

    @Value("${upload.product.meta}")
    private String productMeta;

    @Autowired
    private CafeProductFactory cafeProductFactory;

    @Autowired
    private ProductServiceImpl productService;

    @Override
    public void doBusiness(List<String> result, String fileName) {
        List<ProductIndexEntity> mediaFactory = cafeProductFactory.createMediaFactory(result, fileName);
        importProducts(mediaFactory);
    }

    @Override
    public void doAfter() {

    }

    public  void importProducts(List<ProductIndexEntity> resultList){
        List<List<ProductIndexEntity>> partition = Lists.partition(resultList, 1000);
        partition.forEach(
                i->{
                    productService.saveBatch(i);
                }
        );

    }

    @Override
    public void doFullProduct(String prefix) {
        CommonVO commonVO = new CommonVO();
        commonVO.setMeta("cafe_product_meta");
        commonVO.setCommitSize(1000);
        commonVO.setPath(productMeta);
        commonVO.setPrefix("a");
        commonVO.setMsg("咖啡全量文件");
        doReadFile(commonVO);
    }
}
