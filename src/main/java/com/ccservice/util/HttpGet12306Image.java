package com.ccservice.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.ArrayUtils;

import com.ccservice.dama.FileUtil;
import com.ccservice.dama.HTHYGetCode;

public class HttpGet12306Image {

	static int i =0 ;
	


	private static String verificationUrl_other = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=other&rand=sjrand&";
	
	final private static String DEFAULT__PICTURE_DIRPATH = "D:/12306img/";

	private static String QueryUrl ="https://kyfw.12306.cn/otn/leftTicketPrice/query?leftTicketDTO.train_date=2016-10-18&leftTicketDTO.from_station=GZQ&leftTicketDTO.to_station=BJP&leftTicketDTO.ticket_type=1&randCode=";
	
	
	
	
	
	public static String queryPrice(String randcode,String cookie){
		String res = "-1";
		CCSHttpClientnew httpClient = new CCSHttpClientnew(false, 10000l);
		CCSGetMethod get = new CCSGetMethod(QueryUrl+randcode);
		get.addRequestHeader("Cookie", cookie);
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		get.setFollowRedirects(false);
		try {
			httpClient.executeMethod(get);
			res = get.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
		
		
	}
	
	
	public static String downloadimgbyhttpclient(String cookiestring) {
		String picturepath = "-1";
		try {
			CCSHttpClientnew httpClient = new CCSHttpClientnew(false, 10000L);
			CCSGetMethod get = new CCSGetMethod(verificationUrl_other);
			get.addRequestHeader("Cookie", cookiestring);
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", myhttps);
			get.setFollowRedirects(false);
			String filePath = System.currentTimeMillis() + ".jpg";
			String pictureDirPath = DEFAULT__PICTURE_DIRPATH;
			try {
				String pictureDirPathPro = PropertyUtil.getValue("pictureDirPath");
				if (pictureDirPathPro != null && !"".equals(pictureDirPathPro)) {
					pictureDirPath = pictureDirPathPro;
					File demo = new File(pictureDirPathPro);
					if (!demo.exists()) {
						demo.mkdir();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			picturepath = pictureDirPath + filePath;
			File storeFile = new File(picturepath);
			FileOutputStream output = new FileOutputStream(storeFile);
			httpClient.executeMethod(get);
			// 得到网络资源的字节数组,并写入文件
			output.write(get.getResponseBody());
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return picturepath;
	}
	/**
	 * 获取12306的cookie 
	 * @return
	 */
	public static String get12306cookie() {
		String cookie = "-1";
		try {
			CCSGetMethod get = null;
			CCSHttpClientnew httpClient = new CCSHttpClientnew(false, 10000L);
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			get = new CCSGetMethod("https://kyfw.12306.cn/otn/queryTrainInfo/init");
			Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", myhttps);
			get.setFollowRedirects(false);
			httpClient.executeMethod(get);
			Cookie[] cookies = httpClient.getState().getCookies();
			cookie = ArrayUtils.toString(cookies).replace("{", "").replace("}", "").replace(",", ";")
					+ ";current_captcha_type=C";
		} catch (HttpException e) {
		} catch (IOException e) {
		}
		return cookie;
	}

	/**
	 * 根据验证码进行判断12306的验证码是否正确
	 */
	public static boolean codeIsRight(String rand_code, String cookies) {
		String param = "randCode=" + rand_code + "&rand=sjrand";
		boolean result = false;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Cookie", cookies);
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Content-Length", String.valueOf(param.length()));
		header.put("Referer", "https://kyfw.12306.cn/otn/zwdch/init");
		header.put("Host", "kyfw.12306.cn");
		header.put("Origin", "https://kyfw.12306.cn");
		header.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36");
		header.put("X-Requested-With", "keep-alive");
		header.put("Accept-Language", "zh-CN,zh;q=0.8");
		header.put("Connection", "XMLHttpRequest");
		header.put("Accept-Encoding", "gzip, deflate");
		header.put("Accept", "*/*");
		String resultStr = RequestUtil.post(
				"https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn?randCode=" + rand_code + "&rand=sjrand", "",
				"", header, 0);
//		System.out.println("验证码的结果：" + resultStr);
		if (resultStr.contains("1")) {
			result = true;
		}
		return result;

	}
}
