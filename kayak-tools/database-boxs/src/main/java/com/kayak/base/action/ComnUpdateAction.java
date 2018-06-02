package com.kayak.base.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kayak.base.exception.KPromptException;
import com.kayak.base.service.abs.ComnServiceAbstract;
import com.kayak.base.sql.SqlUtil;
import com.kayak.base.system.RequestSupport;

@Controller
public class ComnUpdateAction extends BaseController {

	@Resource
	private ComnServiceAbstract comnService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/base/comn-update.json")
	public @ResponseBody String updateJson() {
		Map<String, Object> params = RequestSupport.getBodyParameters();
		try {
			String res = this.comnService.comnUpdate(params);
			if (res == null)
				return updateFailure("发生后台错误");

			Map<String, Object> returndata = new HashMap<String, Object>();
			returndata.put("newid", params.get(SqlUtil.COMN_UPDATE_NEWID_KEY));
			returndata.put("fetchrows",
					params.get(SqlUtil.COMN_UPDATE_FETCH_ROWS_KEY));
			// 尝试获取comnService中有没有要返回到前端的值
			Object addtoReturndata = params
					.get(ComnServiceAbstract.COMN_UPDATE_ADDTO_RETURNDATA);
			if (addtoReturndata != null) {
				if (addtoReturndata instanceof Map) {
					returndata.putAll((Map<String, Object>) addtoReturndata);
				}
			}
			return updateSuccess(res, returndata);
		} catch (KPromptException e) {// 获取返回提示的错误
			return updateFailure(e.getMessage(), e.getReturndata());
		}
	}

}
