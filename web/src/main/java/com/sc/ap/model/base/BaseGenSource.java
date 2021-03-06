package com.sc.ap.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;
import com.sc.ap.core.CoreModel;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGenSource<M extends BaseGenSource<M>> extends CoreModel<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}
	
	public Long getId() {
		return getLong("id");
	}

	public void setUrl(String url) {
		set("url", url);
	}
	
	public String getUrl() {
		return getStr("url");
	}

	public void setUser(String user) {
		set("user", user);
	}
	
	public String getUser() {
		return getStr("user");
	}

	public void setPwd(String pwd) {
		set("pwd", pwd);
	}
	
	public String getPwd() {
		return getStr("pwd");
	}

	public void setName(String name) {
		set("name", name);
	}
	
	public String getName() {
		return getStr("name");
	}

	public void setRemovePrefix(String removePrefix) {
		set("removePrefix", removePrefix);
	}
	
	public String getRemovePrefix() {
		return getStr("removePrefix");
	}

	public void setCAt(java.util.Date cAt) {
		set("cAt", cAt);
	}
	
	public java.util.Date getCAt() {
		return get("cAt");
	}

	public void setOpId(Integer opId) {
		set("opId", opId);
	}
	
	public Integer getOpId() {
		return getInt("opId");
	}

	public void setBaseModelPackageName(String baseModelPackageName) {
		set("baseModelPackageName", baseModelPackageName);
	}
	
	public String getBaseModelPackageName() {
		return getStr("baseModelPackageName");
	}

	public void setModelPackageName(String modelPackageName) {
		set("modelPackageName", modelPackageName);
	}
	
	public String getModelPackageName() {
		return getStr("modelPackageName");
	}

	public void setDialect(String dialect) {
		set("dialect", dialect);
	}
	
	public String getDialect() {
		return getStr("dialect");
	}

	public void setExcludedTable(String excludedTable) {
		set("excludedTable", excludedTable);
	}
	
	public String getExcludedTable() {
		return getStr("excludedTable");
	}

	public void setRemark(String remark) {
		set("remark", remark);
	}
	
	public String getRemark() {
		return getStr("remark");
	}

	public void setBasePackage(String basePackage) {
		set("basePackage", basePackage);
	}

	public String getBasePackage() {
		return getStr("basePackage");
	}

}
