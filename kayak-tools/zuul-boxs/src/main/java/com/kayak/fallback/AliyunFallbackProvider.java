package com.kayak.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class AliyunFallbackProvider implements FallbackProvider{

	@Override
    public String getRoute() {
        return "kayak-aliyun";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            
            @Override
            public HttpHeaders getHeaders() {
            	HttpHeaders headers = new HttpHeaders();
            	headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
            
            @Override
            public InputStream getBody() throws IOException {
            	JSONObject body = new JSONObject(); 
            	body.put("result", "kayak-aliyun微服务挂掉了!!");
            	return new ByteArrayInputStream(body.toString().getBytes());
            }
            
            @Override
            public String getStatusText() throws IOException {
            	return this.getStatusCode().getReasonPhrase();
            }
            
            @Override
            public HttpStatus getStatusCode() throws IOException {
            	return HttpStatus.OK;
            }
            
            @Override
            public int getRawStatusCode() throws IOException {
            	return this.getStatusCode().value();
            }
            
            @Override
            public void close() {
                
            }
        };
    }

}
