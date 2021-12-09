package com.yikun.cafedataimport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yikun.cafedataimport.mapper")
public class CafeDataimportApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafeDataimportApplication.class, args);
    }

}
