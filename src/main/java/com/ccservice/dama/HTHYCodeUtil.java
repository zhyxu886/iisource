package com.ccservice.dama;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import org.apache.commons.codec.binary.Base64;


public class HTHYCodeUtil {

    /**
     * 非空  
     * @param str
     * @return
     */
    public static boolean isRealEnnull(String str) {
        if (str == null) {
            return false;
        }
        if ("".equals("str")) {
            return false;
        }
        return true;
    }

    public static String getDLLPATH() {
        String dllpath1 = HTHYCodeUtil.class.getResource("/").toString().replace("file:/", "");
        return dllpath1;
    }

    /** 
     * 对文件进行编码 
     * @param file 需要编码的问家 
     * @return 对文件进行base64编码后的字符串 
     * @throws Exception 
     */
    public static String file2String(File file) throws Exception {
        StringBuffer sb = new StringBuffer();
        FileInputStream in = new FileInputStream(file);
        int b;
        char ch;
        while ((b = in.read()) != -1) {
            ch = (char) b;
            sb.append(ch);
        }
        in.close();
        //将buffer转化为string  
        String oldString = new String(sb);

        //使用base64编码  
        String newString = compressData(oldString);

        return newString;
    }

    /** 
     * 对文件进行解码 
     * @param oldString 需要解码的字符串 
     * @param filePath  将字符串解码到filepath文件路径 
     * @return  返回解码后得到的文件 
     * @throws Exception 
     */
    public static File string2File(String oldString, String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("文件已经存在，不能将base64编码转换为文件");
            return null;
        }
        else {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);

        //对oldString进行解码  
        String newString = decompressData(oldString);

        //将问件内容转为byte[]  
        char[] str = newString.toCharArray();
        for (char ch : str) {
            byte b = (byte) ch;
            out.write(b);
        }
        out.close();
        return file;
    }

    /** 
     * base64--->byte[] 解码 
     * @param oldString 需要解码的字符串 
     * @return  byte[] 
     * @throws Exception 
     */
    public static byte[] base64tobyte(String oldString) throws Exception {
        //对oldString进行解码  
        String newString = decompressData(oldString);
        byte[] bt = newString.getBytes("UTF-8");
        return bt;
    }

    /** 
     * 使用base64编码字符串 
     * @param data 
     * @return 编码后的字符串 
     */
    public static String compressData(String data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DeflaterOutputStream zos = new DeflaterOutputStream(bos);
            zos.write(data.getBytes());
            zos.close();
            return new String(getenBASE64inCodec(bos.toByteArray()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "ZIP_ERR";
        }
    }

    /** 
     * 使用base64解码字符串 
     * @param encdata 
     * @return 解码后的字符串 
     */
    public static String decompressData(String encdata) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InflaterOutputStream zos = new InflaterOutputStream(bos);
            zos.write(getdeBASE64inCodec(encdata));
            zos.close();
            return new String(bos.toByteArray());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "UNZIP_ERR";
        }
    }

    /** 
     * 调用apache的编码方法 
     */
    public static String getenBASE64inCodec(byte[] b) {
        if (b == null)
            return null;
        return new String((new Base64()).encode(b));
    }

    /** 
     * 调用apache的解码方法 
     * @throws UnsupportedEncodingException 
     */
    public static byte[] getdeBASE64inCodec(String s) throws UnsupportedEncodingException {
        if (s == null)
            return null;
        return new Base64().decode(s.getBytes());
    }

    /**
     * inputstream  to  byte[] 
     * @param inStream
     * @return
     * @throws IOException
     * @time 2015年1月23日 下午3:35:27
     * @author fiend
     */
    public static byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public static File byte2File(byte[] bytes, String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("文件已经存在");
            return null;
        }
        else {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);
        out.write(bytes);
        out.close();
        return file;
    }

}
