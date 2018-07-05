package com.sc.ap;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sc.ap.core.CoreController;
import com.sc.ap.interceptors.AdminAAuthInterceptor;
import com.sc.ap.interceptors.AdminIAuthInterceptor;
import com.sc.ap.kits.ResKit;

@Clear({AdminIAuthInterceptor.class,AdminAAuthInterceptor.class})
public class IndexCtr extends CoreController{
    public void index(){
        String domain= ResKit.getConfig("domain");
        redirect(domain+"index.html");
    }


}
