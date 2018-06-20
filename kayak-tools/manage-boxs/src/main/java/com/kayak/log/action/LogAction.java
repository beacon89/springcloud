package com.kayak.log.action;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.base.action.BaseController;
import com.kayak.base.system.RequestSupport;
import com.kayak.http.HttpRequester;
import com.kayak.http.HttpResponse;
import com.kayak.util.SysParams;

@RestController
public class LogAction extends BaseController {

	@RequestMapping(value = "/log/{index}/search.json")
	public String syncConfig(@PathVariable("index") String index) {

		try {
			String parmas = RequestSupport.getBodyParameters(null);

			String elasticUrl = SysParams.getParams("elastic_url");

			HttpRequester requester = new HttpRequester();

			requester.setContentEncoding("UTF-8");
			requester.setContentType("application/json");

			elasticUrl += index + "/_search";
			HttpResponse response = requester.sendPost(elasticUrl, parmas);
			
			JSONObject json = new JSONObject(response.getContent());
			json.put("success", true);
			
			return json.toString();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure(e.getMessage());
		}

	}

}
