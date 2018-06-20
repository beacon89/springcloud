package com.kayak.menu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;

@Repository
public class MenuDao extends ComnDao {

	/**
	 * 设置角色菜单
	 * @param role_id
	 * @param menus
	 * @throws Exception
	 */
	public void updateRoleMenus(int role_id, List<Integer> menus) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_id", role_id);
		super.doUpdateBySqlid("delete_sys_role_menu", params);

		for (int menu : menus) {
			params.put("menu_id", menu);
			super.doUpdateBySqlid("add_sys_role_menu", params);
		}
	}

}
