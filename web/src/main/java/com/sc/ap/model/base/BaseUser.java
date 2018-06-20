package com.sc.ap.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.sc.ap.core.CoreModel;


/**
 * Generated ap-jf.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends CoreModel<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setLoginname(java.lang.String loginname) {
		set("loginname", loginname);
	}

	public java.lang.String getLoginname() {
		return getStr("loginname");
	}

	public void setPassword(java.lang.String password) {
		set("password", password);
	}

	public java.lang.String getPassword() {
		return getStr("password");
	}

	public void setNickname(java.lang.String nickname) {
		set("nickname", nickname);
	}

	public java.lang.String getNickname() {
		return getStr("nickname");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}

	public java.lang.String getPhone() {
		return getStr("phone");
	}

	public void setEmail(java.lang.String email) {
		set("email", email);
	}

	public java.lang.String getEmail() {
		return getStr("email");
	}

	public void setAvatar(java.lang.String avatar) {
		set("avatar", avatar);
	}

	public java.lang.String getAvatar() {
		return getStr("avatar");
	}

	public void setStatus(java.lang.String status) {
		set("status", status);
	}

	public java.lang.String getStatus() {
		return getStr("status");
	}

	public void setSalt(java.lang.String salt) {
		set("salt", salt);
	}

	public java.lang.String getSalt() {
		return getStr("salt");
	}

	public void setLAt(java.util.Date lAt) {
		set("lAt", lAt);
	}

	public java.util.Date getLAt() {
		return get("lAt");
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

	public void setDAt(java.util.Date dAt) {
		set("dAt", dAt);
	}

	public java.util.Date getDAt() {
		return get("dAt");
	}

	public void setLastLoginTime(java.util.Date lastLoginTime) {
		set("lastLoginTime", lastLoginTime);
	}

	public java.util.Date getLastLoginTime() {
		return get("lastLoginTime");
	}

	public void setLastLoginIp(java.lang.String lastLoginIp) {
		set("lastLoginIp", lastLoginIp);
	}

	public java.lang.String getLastLoginIp() {
		return getStr("lastLoginIp");
	}


}
