package com.yikun.cafedataimport.controller;

import com.yikun.cafedataimport.service.impl.ProductMetalService;
import com.yikun.cafedataimport.service.impl.ProductNewMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/8 16:41
 */

@RestController
public class ProductController {

    @Autowired
    private ProductMetalService productMetalService;
    @Autowired
    private ProductNewMetaService productNewMetaService;


    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public void get(){
        productMetalService.handleBillSync("a");
    }

    @RequestMapping(value = "/get2",method = RequestMethod.GET)
    public void get2(){
        productNewMetaService.doFullProduct("a");
    }
}
