package com.yikun.cafedataimport.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yikun.cafedataimport.entity.ProductIndexEntity;
import com.yikun.cafedataimport.mapper.ProductMapper;
import com.yikun.cafedataimport.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/9 11:22
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductIndexEntity> implements ProductService {
}
