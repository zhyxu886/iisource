package  com.ccservice.util;

import java.util.Date;
import java.util.Calendar;
import java.math.BigDecimal;


import java.text.SimpleDateFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import java.security.MessageDigest;

import javax.crypto.spec.DESKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;




public class InterfaceUtil {


    /**DES加密*/
    public static String DES(String creditnumber,String key) throws Exception {
        if (StringIsNull(creditnumber)) {
            throw new Exception("信用卡号为空.");
        }
        key = key.substring(key.length() - 8, key.length());
        //从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key.getBytes());//KEY值
        //创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        //创建一个SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(dks);
        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); //CBC模型
        //用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, new IvParameterSpec(key.getBytes()));//IvParameterSpec  IV值与KEY值一致
        //获取数据并加密  当前时间戳+#+信用卡号
        byte[] buf = cipher.doFinal((System.currentTimeMillis() / 1000 + "#" + creditnumber).getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            int val = ((int) buf[i]) & 0xff;
            if (val < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    /**字符串是否为空*/
    public static boolean StringIsNull(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**MD5加密*/
    public static String MD5(String input) throws Exception {
        if (StringIsNull(input)) {
            return "";
        }
        byte[] buf = input.getBytes("utf-8");
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(buf);
        byte[] md = m.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < md.length; i++) {
            int val = ((int) md[i]) & 0xff;
            if (val < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    /**获取当前日期，格式为yyyy-MM-dd*/
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**获取当前时间，格式为yyyy-MM-dd HH:mm:ss*/
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**指定日期yyyy-MM-dd加N天*/
    public static String getAddDate(String date, int days) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        cal.add(Calendar.DATE, days);
        return sdf.format(cal.getTime());
    }

    /**计算两个yyyy-MM-dd日期间的天数*/
    public static int getSubDays(String start, String end) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date s = sdf.parse(start);
        Date e = sdf.parse(end);
        long days = (e.getTime() - s.getTime()) / 1000 / 60 / 60 / 24;
        return Integer.parseInt(String.valueOf(days));
    }

    /**format格式时间转换成Calendar*/
    public static Calendar toCalendar(String time, String format) throws Exception {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.setTime(sdf.parse(time));
        return cal;
    }

    /**yyyy-MM-dd转成X年X月X日*/
    public static String getLocalDate(String date) {
        String[] dates = date.split("-");
        return dates[0] + "年" + dates[1] + "月" + dates[2] + "日";
    }

    /**比较两个HH:mm时间 timeB 减 timeA*/
    public static long subTime(String timeA, String timeB) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date a = sdf.parse(timeA);
        Date b = sdf.parse(timeB);
        return b.getTime() - a.getTime();
    }

    /**yyyy-MM-dd HH:mm:ss减N小时*/
    public static String getSubTime(String time, int hour) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long millis = sdf.parse(time).getTime() - hour * 60 * 60 * 1000;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return sdf.format(cal.getTime());
    }

    /**指定yyyy-MM-dd HH:mm:ss时间与当前时间比较，当前时间大于指定时间*/
    public static boolean CurrentTimeGreaterThanThisTime(String time) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long t = sdf.parse(time).getTime();
        long c = sdf.parse(sdf.format(new Date())).getTime();
        if (c > t) {
            return true;
        }
        return false;
    }

    /**指定*yyyy-MM-dd HH:mm时间 历时HH:MM 时间后，最终时间*/
    public static String getArrivalTime(String time1, String time2) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String[] dates = time2.split(":");
        int i = Integer.parseInt(dates[0]);
        int j = Integer.parseInt(dates[1]);
        Date a = sdf.parse(time1);
        long millis = a.getTime() + i * 60 * 60 * 1000 + j * 60 * 1000;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return sdf.format(cal.getTime());
    }

    /**加法*/
    public static double add(double a, double b) {
        BigDecimal x = new BigDecimal(a + "");
        BigDecimal y = new BigDecimal(b + "");
        return x.add(y).doubleValue();
    }

    /**乘法*/
    public static double multiply(double a, double b) {
        BigDecimal x = new BigDecimal(a + "");
        BigDecimal y = new BigDecimal(b + "");
        return x.multiply(y).doubleValue();
    }

    /**减法*/
    public static double subtract(double a, double b) {
        BigDecimal x = new BigDecimal(a + "");
        BigDecimal y = new BigDecimal(b + "");
        return x.subtract(y).doubleValue();
    }

    /**float加法*/
    public static float floatAdd(float a, float b) {
        BigDecimal x = new BigDecimal(a + "");
        BigDecimal y = new BigDecimal(b + "");
        return x.add(y).floatValue();
    }

    /**float减法*/
    public static float floatSubtract(float a, float b) {
        BigDecimal x = new BigDecimal(a + "");
        BigDecimal y = new BigDecimal(b + "");
        return x.subtract(y).floatValue();
    }

    /**float乘法 */
    public static float floatMultiply(float a, float b) {
        BigDecimal x = new BigDecimal(a + "");
        BigDecimal y = new BigDecimal(b + "");
        return x.multiply(y).floatValue();
    }

    /**
     * 获取错误类、方法、行数，进行异常信息拼接
     */
    public static String errormsg(Exception e) {
        String msg = e.getMessage();
        String ret = "出错了，错误信息为：" + (StringIsNull(msg) ? "空" : msg.trim());
        StackTraceElement stack = e.getStackTrace()[0];
        if (stack != null) {
            ret += "；异常类：" + stack.getFileName() + " ；方法： " + stack.getMethodName() + " ；行数： " + stack.getLineNumber();
        }
        return ret;
    }

    /**
     * 获取JSONObject String
     */
    public static String getJsonString(com.alibaba.fastjson.JSONObject obj, String key) {
        if (obj == null || StringIsNull(key) || !obj.containsKey(key)) {
            return "";
        }
        else {
            String value = obj.getString(key);
            return StringIsNull(value) ? "" : value;
        }
    }

    /**
     * 将字符串进行排序;
     * 对数组里的每一个值从a 到z 的顺序排序,若遇到相同首字母,则看第二个字母, 以此类推;
     * 排序完成之后,再把所有数组值以“&”字符连接起来
     * 
     * @param str
     * @return 返回排序后待加密字符串(目前艺龙使用)
     * @time 2015年12月10日 上午11:39:31
     * @author Mr.Wang
     */
    public static String sort(String[] str) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            String maxStr = str[i];
            int index = i;
            for (int j = i + 1; j < str.length; j++) {
                if (maxStr.compareTo(str[j]) >= 0) {
                    maxStr = str[j];
                    index = j;
                }
            }
            str[index] = str[i];
            str[i] = maxStr;
            sb.append(maxStr + "&");
        }
        String sign = sb.toString();
        if (sign.endsWith("&")) {
            sign = sign.substring(0, sb.toString().length() - 1);
        }
        return sign;
    }
}
