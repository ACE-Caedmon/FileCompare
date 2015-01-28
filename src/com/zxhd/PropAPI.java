

package com.zxhd;

import java.io.*;
import java.util.Properties;

public class PropAPI {
    public static String getProperty(String path,String key){
        Properties properties=getProps(path);
        return properties.getProperty(key);
    }
    public static void removeProperty(String path,String key){
        Properties properties=getProps(path);
        properties.remove(key);
        OutputStream out=null;
        try {
            out = new FileOutputStream(new File(path));
            properties.store(out, "Remove property ( key = "+key+")");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static void setProperty(String path,String key,String value){
        Properties properties=getProps(path);
        properties.setProperty(key, value);
        OutputStream out=null;
        try {
            out = new FileOutputStream(new File(path));
            properties.store(out, "Update property ( key = "+key+", value = "+value+")");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static Properties getProps(String path){
        InputStream in=null;
        Properties properties=new Properties();
        try {
            in=new FileInputStream(new File(path));

            properties.load(in);

            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return properties;
    }
}

