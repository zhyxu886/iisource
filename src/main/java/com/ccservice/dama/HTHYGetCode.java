package com.ccservice.dama;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;


public class HTHYGetCode {


    private static String DLLPATH = null;

    private static int netIndex = 0;

    String dllpath;

    static {
        //DLL 完整路径
        DLLPATH = HTHYCodeUtil.getDLLPATH();
        DLLPATH += "CaptchaOCR.dll";
        try {
            netIndex = JPYZM.INSTANCE.VcodeInit("95426E83836FE9576B9566AE2144728F");//这个是DLL的初始化引擎密码 防止别人调用你的dll 妥善保存。
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {}

    public interface JPYZM extends StdCallLibrary {
        JPYZM INSTANCE = (JPYZM) Native.loadLibrary(DLLPATH, JPYZM.class);

        int VcodeInit(String pwd);

        boolean GetVcode(int index, byte[] img, int len, byte[] code);
    }

    @SuppressWarnings("resource")
    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取  
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }

    public static byte[] getContent(File file) throws IOException {
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取  
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }

    public static String[] getCode(byte[] imgbs) throws IOException {
        String[] resultary = { "0", "0", "0" };
        byte[] code = new byte[20]; //这里给接收的识别结果 申请下接收的空间 必须要的。
        String rtnCode = null;
        boolean result = JPYZM.INSTANCE.GetVcode(netIndex, imgbs, imgbs.length, code);
        if (result) {
            rtnCode = new String(code);
            rtnCode = rtnCode.trim();
        }
        resultary[0] = rtnCode;
        return resultary;
    }

    public static String getCodeStr(byte[] imgbs) throws IOException {
        String[] resultary = { "0", "0", "0" };
        byte[] code = new byte[20]; //这里给接收的识别结果 申请下接收的空间 必须要的。
        String rtnCode = null;
        boolean result = JPYZM.INSTANCE.GetVcode(netIndex, imgbs, imgbs.length, code);
        if (result) {
            rtnCode = new String(code);
            rtnCode = rtnCode.trim();
        }
        resultary[0] = rtnCode;
        return rtnCode;
    }

    public static String getCode(byte[] imgbs, int type) throws IOException {
        byte[] code = new byte[20]; //这里给接收的识别结果 申请下接收的空间 必须要的。
        String rtnCode = null;
        boolean result = JPYZM.INSTANCE.GetVcode(netIndex, imgbs, imgbs.length, code);
        if (result) {
            rtnCode = new String(code);
            rtnCode = rtnCode.trim();
        }
        return rtnCode;
    }

}
