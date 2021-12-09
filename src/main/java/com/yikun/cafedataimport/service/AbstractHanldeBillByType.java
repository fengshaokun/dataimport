package com.yikun.cafedataimport.service;

import com.yikun.cafedataimport.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/3 16:40
 */
@Slf4j
public abstract class AbstractHanldeBillByType<T> extends AbstractHanldeBillByEachFile<T>{

    protected void  handleBill(String billDir,String prefix){
        List<String> resultList = new ArrayList<>();
        Path dPath = Paths.get(billDir);
        try {
            Files.walkFileTree(dPath,new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path fPath, BasicFileAttributes attrs) throws IOException {
                    Path fp2 = fPath.getFileName();
                    String fileName = null!=fp2?fp2.toString():"";
                    //1.文件名称过滤
                    if (fileName.startsWith(prefix) && fileName.contains(getFileInfix())
                            && fileName.endsWith(getFileSuffix())){
                        long length = attrs.size();
                        if (length == 0){
                            log.warn("该文件是空文件,跳过处理,路径为{}!",fileName);
                            return FileVisitResult.CONTINUE;
                        }
                        //2.读取话单文件按行读取
                        //当文件小于100M时，使用普通的读写方式，否则使用MappedByteBuffer
                        List<String>readList = null;
                        if (length < ONE_HUMDRED_M){
                            readList = CommonUtil.readFileByBufferedReader(fPath);
                        }else {
                            readList = CommonUtil.readFileByMappedByteBuffer(fPath);
                        }
                        resultList.addAll(readList);
                        boolean flag = CommonUtil.reNameTo(new File(fPath.toString()));
                        log.info("文件{}修改名称:{}",fPath.toString(),flag==true?"成功":"失败");
                        return FileVisitResult.CONTINUE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.TERMINATE;
                }
            });
        } catch (Exception e) {
            log.error("处理话单发生异常：{}",e);
        }
        int billSize= resultList.size();
        if (0 == billSize){
            return;
        }
        int billBatchSize = getBillBatchSize();
        try {
            List<T> covertList = convertData(resultList, dPath.toString());
            preProcess(prefix,covertList,dPath.toString());
            if (billSize>billBatchSize){
                List<List<T>> splitResultList = CommonUtil.splitList(covertList, billBatchSize);
                splitResultList.stream().forEach(list -> {
                    handleLineData(list,dPath.toString(),prefix);
                });
            }else {
                handleLineData(covertList,dPath.toString(),prefix);
            }
            afterProcess(prefix,covertList,dPath.toString());
        } catch (Exception e) {
            log.error("AbstractHanldeBillByType-handleBill业务处理数据异常",e);
        }
    }
}
