package com.sc.ap.model;

import com.sc.ap.Consts;
import com.sc.ap.model.base.BaseRole;
import org.omg.PortableInterceptor.Interceptor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Role extends BaseRole<Role> {
	public static final Role dao = new Role().dao();
	private List<Res> ownReses=null;
	private List<Ser> ownSers=null;

	public List<Role> findByLoginnameInCache(String loginname){
		String sql="select r.* from s_role r left join s_user_role ur on r.code=ur.roleCode where r.dAt is null and ur.loginname=?";
		return Role.dao.findByCache(Consts.CACHE_NAMES.userRoles.name(),"findByLoginnameInCache_"+loginname,sql,loginname);
	}



	public List<Res> getOwnReses() {
		return ownReses;
	}

	public void setOwnReses(List<Res> ownReses) {
		this.ownReses = ownReses;
	}

	public List<Ser> getOwnSers() {
		return ownSers;
	}

	public void setOwnSers(List<Ser> ownSers) {
		this.ownSers = ownSers;
	}
}
