package com.sc.ap.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.sc.ap.core.CoreModel;

/**
 * Generated ap-jf.
 */
@SuppressWarnings("serial")
public abstract class BaseRole<M extends BaseRole<M>> extends CoreModel<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public void setCode(java.lang.String code) {
		set("code", code);
	}

	public java.lang.String getCode() {
		return getStr("code");
	}

	public void setOpId(java.lang.Integer opId) {
		set("opId", opId);
	}

	public java.lang.Integer getOpId() {
		return getInt("opId");
	}

	public void setCAt(java.util.Date cAt) {
		set("cAt", cAt);
	}

	public java.util.Date getCAt() {
		return get("cAt");
	}

	public void setLAt(java.util.Date lAt) {
		set("lAt", lAt);
	}

	public java.util.Date getLAt() {
		return get("lAt");
	}

	public void setDAt(java.util.Date dAt) {
		set("dAt", dAt);
	}

	public java.util.Date getDAt() {
		return get("dAt");
	}

}
