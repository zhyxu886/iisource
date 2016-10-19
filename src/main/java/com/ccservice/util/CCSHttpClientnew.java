package com.ccservice.util;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;

public class CCSHttpClientnew extends HttpClient {

    private int TIME_OUT = 60000;

    public CCSHttpClientnew(boolean ifAddConnectionCloseHeader, Long TIME_OUT) {

        init(ifAddConnectionCloseHeader, TIME_OUT);
    }

    private void init(boolean ifAddConnectionCloseHeader, Long timeout) {
        if (null != timeout) {
            this.TIME_OUT = timeout.intValue();
        }

        Collection<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Accept", "*/*"));
        headers.add(new Header("Accept-Language", "zh-cn"));
        headers.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0"));
        headers.add(new Header("UA-CPU", "x86"));
        headers.add(new Header("Accept-Encoding", "gzip, deflate"));
        headers.add(new Header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));

        if (ifAddConnectionCloseHeader) {
            headers.add(new Header("Connection", "close"));
        }
        this.getHttpConnectionManager().getParams().setConnectionTimeout(TIME_OUT);
        this.getHttpConnectionManager().getParams().setSoTimeout(TIME_OUT);

        this.getParams().setParameter("http.default-headers", headers);
    }

}
