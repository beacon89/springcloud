package com.kayak.menu.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.base.action.BaseController;
import com.kayak.base.system.RequestSupport;
import com.kayak.base.util.Tools;
import com.kayak.menu.dao.MenuDao;

@RestController
public class MenuAction extends BaseController {

	@Autowired
	private MenuDao menuDao;

	@RequestMapping(value = "/menu/setRoleMenus.json")
	public String setRoleMenus() {

		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			int role_id = Tools.str2Int(Tools.obj2Str(parmas.get("role_id")));
			String menus = Tools.obj2Str(parmas.get("menus"));
			List<Integer> _menus = new ArrayList<Integer>();
			if (!Tools.strIsEmpty(menus)) {
				String[] menus2 = menus.split(",");
				for (String menu : menus2) {
					_menus.add(Tools.str2Int(menu));
				}
			}
			menuDao.updateRoleMenus(role_id, _menus);
			
			return super.updateSuccess("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}

	}

}
