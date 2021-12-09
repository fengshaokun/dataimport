package com.yikun.cafedataimport.service;

import com.yikun.cafedataimport.entity.CommonVO;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/3 16:10
 */

@Slf4j
public abstract class MediaReaderService {

    /**
     * 读写公共类
     */
    protected void doReadFile(CommonVO commonVO) {
        File file = new File(commonVO.getPath());
        File[] files = file.listFiles(((dir, name) -> name.startsWith(commonVO.getPrefix()) && name.contains(commonVO.getMeta()) &&
                name.endsWith(".txt")));

        //获取当前能获取的文件
        if (files == null || files.length == 0){
            log.warn("当前目录没有获取到:{}",commonVO.getMsg());
            return;
        }

        Stream.of(files).forEach(a->{
            BufferedReader reader = null;
            String line = null;
            String fileName = a.getName();
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(commonVO.getPath()+ fileName), "UTF-8"));
                List<String> result = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                        result.add(line);
                        int size = result.size();
                        if (commonVO.getCommitSize() == size){
                            try {
                                doBusiness(result,fileName);
                            } catch (Exception e) {
                                log.error("文件名:{},MediaReaderService doBusiness 异常:{}",fileName,e);
                            }
                            result = new ArrayList<>();
                        }
                    }
                    //处理最后一次
                    if (result.size()>0){
                        try {
                            doBusiness(result,fileName);
                        } catch (Exception e) {
                            log.error("文件名:{},MediaReaderService doBusiness 异常:{}",fileName,e);
                        }
                    }
                    try {
                        doAfter();
                    } catch (Exception e) {
                        log.error("文件名:{},MediaReaderService doAfter 异常:{}",fileName,e);
                    }
            } catch (Exception e) {
                log.error("Class:MediaReadService,Method:doReadFile,handle file error,fileName:{},exception:{}",fileName,e);
            }finally {
                if (reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        log.error("Class:MediaReadService,Method:doReadFile,close stream exception:{}",e);
                    }
                }
                //修改文件后缀
                boolean flag = reNameTo(a);
                log.info("文件{}修改名称:{}",a.getName(),flag==true?"成功":"失败");
            }
        });

    }

    public abstract void doBusiness(List<String> result,String fileName);

    public abstract void doAfter();

    private static  boolean reNameTo(File file){
        boolean flag = false;
        try {
            String filePath = file.getAbsolutePath() + ".bak";
            File newFile = new File(filePath);
            flag = file.renameTo(newFile);
        } catch (Exception e) {
           log.error("文件{}修改名称异常",file.getName(),e);
        }
        return flag;
    }



}
