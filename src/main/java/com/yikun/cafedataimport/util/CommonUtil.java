package com.yikun.cafedataimport.util;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Cleaner;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/7 14:50
 */
@Slf4j
public class CommonUtil {

    public static List<String> readFileByBufferedReader(Path path){
        BufferedReader reader = null;
        List<String> resultList = new ArrayList<>();
        String line = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(path.toString()),"UTF-8"));
            while ((line = reader.readLine())!=null){
                resultList.add(line);
            }
        } catch (Exception e) {
            log.error("readFileByBufferedReader 读取文件数据发生系统异常:{}",e);
        }finally {
            if (reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }

    public static List<String> readFileByMappedByteBuffer(Path path){
    //读取文件
        MappedByteBuffer mbb = null;
        RandomAccessFile raf = null;
        //int j = 0;
        //读取文件流
        final  int extra = 200;
        int count = extra;
        byte[] buf=new byte[count];
        int j = 0;
        char ch = '\0';
        boolean flag = false;
        List<String> resultList = new ArrayList<>();
        try {
            raf = new RandomAccessFile(path.toString(),"r");
            mbb = raf.getChannel().map(FileChannel.MapMode.READ_ONLY,0,raf.length());
            //要根据文件行的平均字节大小来赋值
            while (mbb.remaining() > 0){
                byte by = mbb.get();
                switch (ch){
                    case '\n':
                        flag = true;
                        break;
                    case '\r':
                        mbb.get();
                        flag = true;
                        break;
                    default:
                        buf[j] = by;
                        break;
                }
                j++;
                //读取的字符超过了buf数组的大小,需要动态扩容
                if (!flag && j>=count){
                  count = count + extra;
                  buf = copyOf(buf,count);
                }
                if (flag){
                    String line = new String(buf,0,j-1,"GBK");
                    flag = false;
                    count = extra;
                    buf = new byte[count];
                    j = 0;
                    resultList.add(line);
                }
            }
            //处理最后一次读取
            if (j>0){
                String line = new String(buf,0,j-1,"GBK");
                resultList.add(line);
            }
        } catch (Exception e) {
            log.error("readFileByMappedByteBuffer 读取文件数据发生系统异常:{}",e);
        } finally {
            if (mbb!=null){
                try {
                    close(mbb);
                }catch (Exception e){
                 log.error("关闭mbb系统异常");
                }
            }
            if (raf!=null){
                try {
                    raf.close();
                }catch (Exception e){
                    log.error("关闭raf系统异常");
                }
            }
        }
       return resultList;
    }

    public static  byte[] copyOf(byte[] original,int newLength){
        byte[] copy = new byte[newLength];
        System.arraycopy(original,0,copy,0,Math.min(original.length,newLength));
        return copy;
    }

    @SuppressWarnings("restriction")
    public static void  close(MappedByteBuffer mbb) throws Exception {
        Method cleaner = mbb.getClass().getMethod("cleaner");
        cleaner.setAccessible(true);
        Cleaner invoke = (Cleaner) cleaner.invoke(mbb);
        if (invoke!=null){
            invoke.clean();
        }
    }

    public static boolean reNameTo(File file){
        boolean flag = false;
        try {
            String filePath = file.getAbsolutePath() + ".bak";
            File newFile = new File(filePath);
            flag = file.renameTo(newFile);
        } catch (Exception e) {
            log.error("文件{}修改名称异常:{}",file.getName(),e);
        }
        return flag;

    }

    public static <T>List<List<T>> splitList (List<T>list,int groupSize){
        int size = list.size();
        int count = (size + groupSize - 1)/groupSize;
        return Stream.iterate(0,n-> n+1).limit(count).parallel().map(b->{
            List<T>tmpList = list.stream().skip(b*groupSize).limit(groupSize).parallel()
                    .collect(Collectors.toList());
            return tmpList;
        }).collect(Collectors.toList());
    }


}
