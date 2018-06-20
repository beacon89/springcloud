package com.kayak.base.action;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.base.service.abs.ComnServiceAbstract;
import com.kayak.base.system.KResult;
import com.kayak.base.system.RequestSupport;
import com.kayak.base.system.RequestSupportDb;

@RestController
public class ComnQueryAction extends BaseController {

	@Resource
	private ComnServiceAbstract comnService;

	@RequestMapping(value = "/base/comn-query.json")
	public String queryJson() {
		try {
			KResult result = comnService.comnQuery(RequestSupport.getBodyParameters());
			return RequestSupportDb.result2JsonList(result);
		} catch (Exception e) {
			return updateFailure(e.getMessage());
		}

	}

	/**
	 * 树结构查询结果集
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/base/comn-query-tree.json")
	public String queryTree() {
		try {
			KResult result = comnService.comnQuery(RequestSupport.getBodyParameters());
			return RequestSupportDb.result2JsonTree(result);
		} catch (Exception e) {
			return updateFailure(e.getMessage());
		}
	}

}
