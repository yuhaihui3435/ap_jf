package com.sc.ap;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sc.ap.core.CoreController;
import com.sc.ap.interceptors.AdminAAuthInterceptor;
import com.sc.ap.interceptors.AdminIAuthInterceptor;

@Clear(AdminIAuthInterceptor.class)
public class IndexCtr extends CoreController{
    public void index(){
        String domain= CacheKit.get(Consts.CACHE_NAMES.paramCache.name(),"siteDomain");
        redirect(domain+"/ad/index.html");
    }
}
