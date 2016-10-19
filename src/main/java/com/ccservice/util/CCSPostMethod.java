package com.ccservice.util;

import org.apache.commons.httpclient.Header;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class CCSPostMethod extends org.apache.commons.httpclient.methods.PostMethod {
	
	

	public CCSPostMethod() {
		super();
	}

	public CCSPostMethod(String uri) {
		super(uri);
	}

	/**
	 * 自动解压的getResponseBodyAsStream
	 */
	@Override
	public InputStream getResponseBodyAsStream() throws IOException {
		return getResponseBodyAsStream(false);
	}

	public InputStream getResponseBodyAsStream(boolean isAutoUnzip) throws IOException {		
		Header encode = this.getResponseHeader("Content-Encoding");
		if (isAutoUnzip && encode != null && encode.getValue().contains("gzip"))
			return new GZIPInputStream(super.getResponseBodyAsStream());
		else
			return super.getResponseBodyAsStream();
	}

	/**
	 * 自动解压的getResponseBodyAsString
	 */
	@Override
	public String getResponseBodyAsString() throws IOException {
		return getResponseBodyAsString(true);
	}

	public String getResponseBodyAsString(boolean isAutoUnzip) throws IOException {		
		Header encode = this.getResponseHeader("Content-Encoding");
		if (isAutoUnzip && encode != null && encode.getValue().contains("gzip"))
			return GzipTool.unZip(this.getURI().toString(), super.getResponseBodyAsStream(), this.getResponseCharSet());
		else if (encode != null && encode.getValue().contains("deflate"))
			return GzipTool.unDeflater(this.getURI().toString(), super.getResponseBodyAsStream(), this.getResponseCharSet());
		else
			return super.getResponseBodyAsString();
	}
}
