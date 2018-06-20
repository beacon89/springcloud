package com.kayak.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.json.JSONObject;

import com.kayak.base.util.Tools;

/**
 * HTTP请求对象
 * 
 */
public class HttpRequester {
	private String defaultContentEncoding;
	private String contentEncoding;
	private String contentType;
	private int connectTimeout;
	private int readTimeout;

	public HttpRequester() {
		this.defaultContentEncoding = Charset.defaultCharset().name();
	}

	/**
	 * 发送GET请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @return 响应对象
	 * @throws IOException
	 */
	public HttpResponse sendGet(String urlString) throws IOException {
		return this.send(urlString, "GET", null, null);
	}

	/**
	 * 发送GET请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @return 响应对象
	 * @throws IOException
	 */
	public HttpResponse sendGet(String urlString, Map<String, String> params) throws IOException {
		return this.send(urlString, "GET", params, null);
	}

	/**
	 * 发送GET请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @param propertys
	 *            请求属性
	 * @return 响应对象
	 * @throws IOException
	 */
	public HttpResponse sendGet(String urlString, Map<String, String> params, Map<String, String> propertys)
			throws IOException {
		return this.send(urlString, "GET", params, propertys);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @return 响应对象
	 * @throws IOException
	 */
	public HttpResponse sendPost(String urlString) throws IOException {
		return this.send(urlString, "POST", null, null);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @return 响应对象
	 * @throws IOException
	 */
	public HttpResponse sendPost(String urlString, Map<String, String> params) throws IOException {
		return this.send(urlString, "POST", params, null);
	}

	public HttpResponse sendPost(String urlString, JSONObject params) throws IOException {
		return this.sendPost(urlString, params.toString());
	}

	public HttpResponse sendPost(String urlString, String params) throws IOException {
		HttpURLConnection urlConnection = null;
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection) url.openConnection();

		if (connectTimeout > 0) {
			urlConnection.setConnectTimeout(connectTimeout);
		}
		if (readTimeout > 0) {
			urlConnection.setReadTimeout(readTimeout);
		}

		if (this.contentType != null) {
			urlConnection.setRequestProperty("Content-Type", this.contentType);
		}
		urlConnection.setRequestMethod("POST");
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);

		if (!Tools.strIsEmpty(contentEncoding)) {// 如果有设置编码格式，则不使用默认的
			urlConnection.getOutputStream().write(params.toString().getBytes(contentEncoding));
		} else {
			urlConnection.getOutputStream().write(params.toString().getBytes());
		}

		urlConnection.getOutputStream().flush();
		urlConnection.getOutputStream().close();

		return this.makeContent(urlString, urlConnection);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @param propertys
	 *            请求属性
	 * @return 响应对象
	 * @throws IOException
	 */
	public HttpResponse sendPost(String urlString, Map<String, String> params, Map<String, String> propertys)
			throws IOException {
		return this.send(urlString, "POST", params, propertys);
	}

	/**
	 * 发送HTTP请求
	 * 
	 * @param urlString
	 * @return 响映对象
	 * @throws IOException
	 */
	private HttpResponse send(String urlString, String method, Map<String, String> parameters,
			Map<String, String> propertys) throws IOException {
		HttpURLConnection urlConnection = null;

		if (method.equalsIgnoreCase("GET") && parameters != null) {
			StringBuffer param = new StringBuffer();
			int i = 0;
			for (String key : parameters.keySet()) {
				if (i == 0)
					param.append("?");
				else
					param.append("&");
				param.append(key).append("=").append(parameters.get(key));
				i++;
			}
			urlString += param;
		}
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection) url.openConnection();

		if (connectTimeout > 0) {
			urlConnection.setConnectTimeout(connectTimeout);
		}
		if (readTimeout > 0) {
			urlConnection.setReadTimeout(readTimeout);
		}

		urlConnection.setRequestMethod(method);
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);

		if (propertys != null)
			for (String key : propertys.keySet()) {
				urlConnection.addRequestProperty(key, propertys.get(key));
			}

		if (method.equalsIgnoreCase("POST") && parameters != null) {
			StringBuffer param = new StringBuffer();
			int i = 0;
			for (String key : parameters.keySet()) {
				if (i > 0)
					param.append("&");
				param.append(key).append("=").append(parameters.get(key));
				i++;
			}
			if (!Tools.strIsEmpty(contentEncoding)) {// 如果有设置编码格式，则不使用默认的
				urlConnection.getOutputStream().write(param.toString().getBytes(contentEncoding));
			} else {
				urlConnection.getOutputStream().write(param.toString().getBytes());
			}
			urlConnection.getOutputStream().flush();
			urlConnection.getOutputStream().close();
		}

		return this.makeContent(urlString, urlConnection);
	}

	/**
	 * 得到响应对象
	 * 
	 * @param urlConnection
	 * @return 响应对象
	 * @throws IOException
	 */
	private HttpResponse makeContent(String urlString, HttpURLConnection urlConnection) throws IOException {
		HttpResponse httpResponser = new HttpResponse();
		try {

			String ecod = urlConnection.getContentEncoding();
			if (ecod != null) {// 使用默认的编码格式
				ecod = this.defaultContentEncoding;
			}

			if (contentEncoding != null) {// 如果有设置编码格式，则不使用默认的
				ecod = contentEncoding;
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InputStream in = urlConnection.getInputStream();
			byte[] tem = new byte[1024];
			String temStr = "";
			int len;
			while ((len = in.read(tem)) != -1) {
				bos.write(tem, 0, len);
			}
			temStr = new String(bos.toByteArray(), ecod);

			in.close();

			httpResponser.urlString = urlString;

			httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
			httpResponser.file = urlConnection.getURL().getFile();
			httpResponser.host = urlConnection.getURL().getHost();
			httpResponser.path = urlConnection.getURL().getPath();
			httpResponser.port = urlConnection.getURL().getPort();
			httpResponser.protocol = urlConnection.getURL().getProtocol();
			httpResponser.query = urlConnection.getURL().getQuery();
			httpResponser.ref = urlConnection.getURL().getRef();
			httpResponser.userInfo = urlConnection.getURL().getUserInfo();

			httpResponser.content = temStr;
			httpResponser.contentEncoding = ecod;
			httpResponser.code = urlConnection.getResponseCode();
			httpResponser.message = urlConnection.getResponseMessage();
			httpResponser.contentType = contentType;
			httpResponser.method = urlConnection.getRequestMethod();
			httpResponser.connectTimeout = urlConnection.getConnectTimeout();
			httpResponser.readTimeout = urlConnection.getReadTimeout();

			return httpResponser;
		} catch (IOException e) {
			throw e;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	/**
	 * 默认的响应字符集
	 */
	public String getDefaultContentEncoding() {
		return this.defaultContentEncoding;
	}

	/**
	 * 设置默认的响应字符集
	 */
	public void setDefaultContentEncoding(String defaultContentEncoding) {
		this.defaultContentEncoding = defaultContentEncoding;
	}

	/**
	 * 获取响应字符集编码
	 * 
	 * @return
	 */
	public String getContentEncoding() {
		return contentEncoding;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * 设置响应字符集编码
	 * 
	 * @param contentEncoding
	 */
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	/**
	 * 设置连接主机超时（单位：毫秒）
	 * 
	 * @param connectTimeout
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 设置从主机读取数据超时（单位：毫秒）
	 * 
	 * @param readTimeout
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

}
