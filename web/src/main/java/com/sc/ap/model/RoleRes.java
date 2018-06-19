package com.sc.ap.model;

import com.jfinal.plugin.ehcache.CacheKit;
import com.sc.ap.Consts;
import com.sc.ap.model.base.BaseRoleRes;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class RoleRes extends BaseRoleRes<RoleRes> {
	public static final RoleRes dao = new RoleRes().dao();

//	public void delByRoleId(int roleId) {
//		List<RoleRes> roleResList = dao.find("select id from s_role_res where roleId=?", roleId);
//		for (RoleRes rr : roleResList) {
//			rr.delete();
//			List<UserRole> userRoles = UserRole.dao.find("select * from s_user_role where rid=?", rr.getRoleId());
//			for (UserRole userRole : userRoles) {
//				CacheKit.remove(Consts.CACHE_NAMES.userRoles.name(), "rolesByUserId_"+userRole.getUId());
//				CacheKit.remove(Consts.CACHE_NAMES.userReses.name(), userRole.getUId());
//			}
//		}
//	}


	public List<RoleRes> findByRoleCode(String roleCode){
		return dao.find("select * from s_role_res where roleCode=?",roleCode);
	}

	public void delByRoleCode(String roleCode){
		List<RoleRes> roleResList=findByRoleCode(roleCode);
		for(RoleRes roleRes:roleResList){
			roleRes.delete();
		}
	}
}
