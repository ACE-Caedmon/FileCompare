package com.zxhd;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;

public class Util {
    public static final String MOCK_SUFFIX="MOCKSUFFIX";
    public static String getSuffix(File f){
        String originalSuffix=f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length());
        return  originalSuffix;
    }
    public static void changeSuffix(File src,String newSuffix) {
        if(!src.exists()){
            System.err.println("ERROR:change suffix to "+newSuffix+" failed,file not found "+src.getAbsolutePath());
            return;
        }
        String absPath=src.getAbsolutePath();
        String noSuffixFileName=absPath.substring(0,absPath.lastIndexOf(".")+1);
        src.renameTo(new File(noSuffixFileName+newSuffix));
    }
    public static void changeFileName(File src,File dst){
        if(!src.exists()){
            System.err.println("ERROR:文件不存在 "+src.getAbsolutePath());
            return;
        }
        src.renameTo(dst);
    }
    public static void createIfNotExists(File f,boolean isDir) throws IOException {
        if(!f.exists()){
            if(isDir){
                f.mkdirs();
            }else{
                File parent=f.getParentFile();
                if(parent!=null&&!parent.exists()){
                    parent.mkdirs();
                }

                f.createNewFile();
            }

        }
    }
    public static void copy(File src,File dest) throws IOException{
        FileInputStream in=new FileInputStream(src);
        FileOutputStream out=new FileOutputStream(dest);
        byte[] buff=new byte[1024];
        int len=0;
        while((len=in.read(buff))!=-1){
            out.write(buff,0,len);
        }
        out.flush();
    }
    public static boolean isEncrypt(File file){
        return  true;
    }
    public static void getAllFiles(List<File> files,File file,final String exculde){
        if(file.isFile()){
            files.add(file);
        }else if(file.isDirectory()){
            for(File tmp:file.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					if(exculde==null||exculde.trim().equals("")){
                        return true;
                    }
					return !pathname.getName().equals(exculde);
				}
			})){
                getAllFiles(files,tmp,exculde);
            }
        }
    }
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest =MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public static boolean isSameFile(String p1,String p2){
        File f1=new File(p1);
        File f2=new File(p2);
        return isSameFile(f1,f2);
    }
    public static boolean isSameFile(File f1,File f2){
        if(f1.isDirectory()||f2.isDirectory()){
            return false;
        }
        if(f1.length()!=f2.length()){
            return false;
        }
        String md51=getFileMD5(f1);
        String md52=getFileMD5(f2);
        return md51.equals(md52);
    }
}