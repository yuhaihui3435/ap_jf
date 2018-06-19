package com.sc.ap.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.sc.ap.core.CoreModel;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRoleSer<M extends BaseRoleSer<M>> extends CoreModel<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return getInt("id");
	}

	public void setRoleCode(String roleCode) {
		set("roleCode", roleCode);
	}
	
	public String getRoleCode() {
		return getStr("roleCode");
	}

	public void setSerCode(String serCode) {
		set("serCode", serCode);
	}
	
	public String getSerCode() {
		return getStr("serCode");
	}

}
