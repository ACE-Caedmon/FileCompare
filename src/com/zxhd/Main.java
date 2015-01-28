package com.zxhd;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2014/11/6.
 */
public class Main {
    static final String SETTINGS_PROPERTIES_PATH="settings.properties",WRAP="\n";
    public static void main(String[] args){
        System.out.println("开始比较");
        Properties properties=PropAPI.getProps(SETTINGS_PROPERTIES_PATH);
        List<String> dirForSearchStringArr=new ArrayList<String>();
        List<String> dirForCompareStringArr=new ArrayList<String>();
        Enumeration enumeration=properties.propertyNames();
        try{
            while(enumeration.hasMoreElements()){
                String key=enumeration.nextElement().toString();
                if(key.startsWith("forCompare")){
                    dirForCompareStringArr.add(new String(properties.getProperty(key).getBytes("ISO-8859-1"),"GBK"));
                }
                if(key.startsWith("forSearch")){
                    dirForSearchStringArr.add(new String(properties.getProperty(key).getBytes("ISO-8859-1"),"GBK"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("出现中文乱码问题");
        }

        System.out.println("forCompare:");
        for(String s:dirForCompareStringArr){
            System.out.println(s);
        }
        printEmptyLine();
        System.out.println("forSearch:");
        for(String s:dirForSearchStringArr){
            System.out.println(s);
        }
        printEmptyLine();
        List<File> filesForSearch=new LinkedList<File>();
        List<File> filesForCompare=new LinkedList<File>();
        List<File> dirsForSearch=new ArrayList<File>(dirForSearchStringArr.size());
        List<File> dirsForCompare=new ArrayList<File>(dirForSearchStringArr.size());
        for(String dirForSearchString:dirForSearchStringArr){
            File dirForSearch=new File(dirForSearchString);
            if(!dirForSearch.exists()){
                System.err.println(dirForSearchString+"文件夹不存在");
                continue;
            }
            if(!dirForSearch.isDirectory()){
                System.err.println(dirForSearchString+"必须是文件夹");
                continue;
            }
            dirsForSearch.add(dirForSearch);
        }
        for(String dirForCompareString:dirForCompareStringArr){
            File dirForCompare=new File(dirForCompareString);
            if(!dirForCompare.exists()){
                System.err.println(dirForCompareString+"文件/文件夹不存在");
                continue;
            }
            dirsForCompare.add(dirForCompare);
        }
        for(File dir:dirsForSearch){
            Util.getAllFiles(filesForSearch,dir,null);
        }
        for(File dir:dirsForCompare){
            Util.getAllFiles(filesForCompare,dir,null);
        }
        String searchResultOut=properties.getProperty("resultOut");
        File result=new File(searchResultOut);
        if(result.exists()){
            result.delete();
        }else{
            try {
                Util.createIfNotExists(result,false);
            } catch (IOException e) {
                System.err.println("创建"+searchResultOut+"失败");
                e.printStackTrace();
                return;
            }
        }
        BufferedWriter resultWriter=null;
        try {
            resultWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(result)));
            for(File forCompare:filesForCompare){
                resultWriter.write("================================================="+WRAP);
                System.out.println("=================================================");
                resultWriter.write(forCompare.getAbsolutePath() + WRAP);
                System.out.println(forCompare.getAbsolutePath());
                resultWriter.write(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+WRAP);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                for(File forSearch:filesForSearch){
                    if(Util.isSameFile(forCompare,forSearch)){
                        resultWriter.write(forSearch.getAbsolutePath()+WRAP);
                        System.out.println(forSearch.getAbsolutePath());
                    }
                }
                resultWriter.write("================================================="+WRAP);
                System.out.println("=================================================");
                resultWriter.write(WRAP);
                printEmptyLine();
                resultWriter.write(WRAP);
                printEmptyLine();
                resultWriter.write(WRAP);
                printEmptyLine();
            }
            resultWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                resultWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("比较完成");
    }
    public static final void printEmptyLine(){
        System.out.println();
    }
}
