package com.sc.ap.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;
import com.sc.ap.core.CoreModel;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseResSer<M extends BaseResSer<M>> extends CoreModel<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return getInt("id");
	}

	public void setRId(Integer rId) {
		set("rId", rId);
	}
	
	public Integer getRId() {
		return getInt("rId");
	}

	public void setSId(Integer sId) {
		set("sId", sId);
	}
	
	public Integer getSId() {
		return getInt("sId");
	}

}
