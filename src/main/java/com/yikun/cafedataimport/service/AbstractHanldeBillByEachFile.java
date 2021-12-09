package com.yikun.cafedataimport.service;

import com.yikun.cafedataimport.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/3 16:50
 */
@Slf4j
public abstract class AbstractHanldeBillByEachFile<T> implements IBillHandler<T>{

    public static  final String FULL_PREFIX="a";
    public static  final String INCREMENT_PREFIX="i";
    public static  final long ONE_HUMDRED_M=100 * 1024 * 1024;

    @Override
    public List<CompletableFuture<Void>> handleBillAsync(String prefix) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Stream.of(getFileArray()).map(billDir->{
            CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
              handleBill(billDir,prefix);
            });
            return future;
        }).forEach(future->futures.add(future));
        return futures;
    }

    @Override
    public void handleBillSync(String prefix) {
         Stream.of(getFileArray()).forEach(billDir->handleBill(billDir,prefix));
    }

    protected abstract String[] getFileArray();

    protected void handleBill(String billDir,String prefix){
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
                        List<String>resultList = null;
                        if (length < ONE_HUMDRED_M){
                            resultList = CommonUtil.readFileByBufferedReader(fPath);
                        }else {
                            resultList = CommonUtil.readFileByMappedByteBuffer(fPath);
                        }
                        int billSize = resultList.size();
                        if (0 == billSize){
                             CommonUtil.reNameTo(new File(fPath.toString()));
                             return FileVisitResult.CONTINUE;
                        }
                        //分批处理(此处分批处理是为了缓解客户端发送消息的压力) --billBathSize行
                        try {
                            List<T> covertList = convertData(resultList, fileName);
                            preProcess(prefix,covertList,fileName);
                            int billBatchSize = getBillBatchSize();
                            if (billSize>billBatchSize){
                                List<List<T>> splitResultList = CommonUtil.splitList(covertList, billBatchSize);
                                splitResultList.stream().forEach(list -> {
                                        handleLineData(list,fileName,prefix);
                                });
                            }else {
                                handleLineData(covertList,fileName,prefix);
                            }
                            afterProcess(prefix,covertList,fileName);
                        } catch (Exception e) {
                            log.error("AbstractHanldeBillByEachFile-handleBill业务处理数据异常",e);
                        }
                        CommonUtil.reNameTo(new File(fPath.toString()));
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
            e.printStackTrace();
        }
    }

    protected abstract String getFileInfix();

    protected  String getFileSuffix(){
        return ".dat";
    }

    protected  int getBillBatchSize(){
        return 10000;
    }

    protected void preProcess(String prefix,List<T>list,String fileName){}

    protected void afterProcess(String prefix,List<T>list,String fileName){}

    protected void handleLineData(List<T>resultList,String fileName,String prefix){
        if (FULL_PREFIX.equals(prefix)){
            handleAllBusiness(resultList, fileName);
            return;
        }
        if (INCREMENT_PREFIX.equals(prefix)){
            handleIncrementBusiness(resultList, fileName);
            return;
        }
    }



}
