package com.sc.ap.model;

import com.sc.ap.model.base.BaseUserRole;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class UserRole extends BaseUserRole<UserRole> {
	public static final UserRole dao = new UserRole().dao();

	public void delByRoleId(int roleId) {
		List<UserRole> userRoles = dao.find("select id from s_user_role where rId=?", roleId);
		for (UserRole ur : userRoles) {
			ur.delete();
			// CacheKit.remove(Consts.CACHE_NAMES.userRoles.name(),ur.getUid());
			// CacheKit.remove(Consts.CACHE_NAMES.userReses.name(),ur.getUid());
		}
	}

	public void delByUserId(int userId) {
		List<UserRole> userRoles = dao.find("select id from s_user_role where uId=?", userId);
		for (UserRole ur : userRoles) {
			ur.delete();
			// CacheKit.remove(Consts.CACHE_NAMES.userRoles.name(),ur.getUid());
			// CacheKit.remove(Consts.CACHE_NAMES.userReses.name(),ur.getUid());
		}
	}
}
