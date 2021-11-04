package org.springframework.data.redis.venus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VenusHttpUtils {
	private static final Integer READ_TIMEOUT = 5000;
	private static final Integer CONNECT_TIMEOUT = 10000;
	
	private static Logger logger = LoggerFactory.getLogger(VenusHttpUtils.class);
	
	public static String sendHttpGet(String url, String param) {
		HttpURLConnection connection = null;
		InputStream is = null;
        BufferedReader br = null;
		
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            
            connection = (HttpURLConnection)realUrl.openConnection();
            
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.connect();
            
            if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				
				StringBuffer sbf = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
				    sbf.append("\r\n");
				}
				
				return sbf.toString();
            } else {
            	logger.error("发送http/get请求失败,{}/{}" , connection.getResponseCode() , urlNameString);
            }
            
            return null;
        } catch (Exception e) {
        	logger.error("发送http/get请求异常,{}" , e.getMessage());
        	
            return null;
        } finally {
        	if (connection != null) {
        		connection.disconnect();
        	}
        	try {
	        	if (is != null) {
	        		is.close();
	        	}
	        	
	        	if (br != null) {
	        		br.close();
	        	}
        	} catch (Exception e) {
        		logger.error("发送http/get关闭流异常,{}" , e.getMessage());
        	}
        }
    }
	
	public static String sendHttpPost(String path, String postContent) {
        URL url = null;
        OutputStream os = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            
            os = httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();
            
            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                
                br.close();
                
                return sb.toString();
            } else {
            	logger.error("调用http/post失败 {}" , httpRspCode);
            	
            	return null;
            }
        } catch (Exception e) {
            logger.error("调用http/post失败 {}" , e.getMessage());
            
            return null;
        } finally {
        	if (os != null) {
        		try {
					os.close();
				} catch (IOException e) {
					logger.error("调用http/post失败 {}" , e.getMessage());
				}
        	}
        }
	}	
}
