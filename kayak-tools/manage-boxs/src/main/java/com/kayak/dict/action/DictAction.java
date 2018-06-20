package com.kayak.dict.action;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.base.action.BaseController;
import com.kayak.base.sql.SqlRow;
import com.kayak.dict.dao.DictDao;

@RestController
public class DictAction extends BaseController {

	@Autowired
	private DictDao dictDao;

	public static Map<String, JSONArray> dicts = new ConcurrentHashMap<String, JSONArray>();

	/**
	 * 查询数字字典列表
	 * 
	 * @param dict
	 * @return
	 */
	@RequestMapping(value = "/base/dict/{dict}.json")
	public String dictJsonRender(@PathVariable String dict) {
		JSONObject json = new JSONObject();
		JSONArray items = dicts.get("dict");
		try {
			if (items == null) {
				List<SqlRow> sqlRows = dictDao.findDictItems(dict);

				if (sqlRows != null && sqlRows.size() > 0) {
					JSONArray _items = new JSONArray();
					for (SqlRow sqlRow : sqlRows) {
						_items.put(new JSONObject(sqlRow));
					}

					dicts.put(dict, _items);
					json.put("rows", _items);
				}
			} else {
				json.put("rows", items);
			}
			json.put("success", true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return super.updateFailure("服务器异常，请稍后服务");
		}
		return json.toString();
	}

	@RequestMapping(value = "/base/dict/freshen/{dict}.json")
	public String dictJsonFreshen(@PathVariable String dict) {
		dicts.remove(dict);
		return super.updateSuccess();
	}

}
