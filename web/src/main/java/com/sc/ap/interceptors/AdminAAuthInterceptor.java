package com.sc.ap.interceptors;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Duang;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sc.ap.Consts;
import com.sc.ap.kits.CookieKit;
import com.sc.ap.model.Role;
import com.sc.ap.model.RoleSer;
import com.sc.ap.model.User;
import com.sc.ap.core.CoreController;
import com.sc.ap.core.CoreException;
import com.sc.ap.kits.ReqKit;
import com.sc.ap.kits.ResKit;
import com.sc.ap.service.role.RoleService;
import com.sc.ap.service.ser.SerService;
import com.sc.ap.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * Created by yuhaihui8913 on 2017/11/16.
 * 请求访问控制前置处理
 *
 */
public class AdminAAuthInterceptor implements Interceptor{
    private RoleService roleService= Duang.duang(RoleService.class.getSimpleName(),RoleService.class);
    private SerService serService= Duang.duang(SerService.class.getSimpleName(),SerService.class);
    private UserService userService= Duang.duang(UserService.class.getSimpleName(),UserService.class);

    @Override
    public void intercept(Invocation invocation) {
        CoreController controller=(CoreController) invocation.getController();
        String ak=invocation.getActionKey();
        HttpServletRequest request=controller.getRequest();
        boolean flag=false;
        String userId = CookieKit.get(controller, Consts.USER_ACCESS_TOKEN);
        if(!StrUtil.isBlank(userId)) {
            User user = userService.findByIdInCache(new Integer(userId));
            String[] roleCodes = roleService.findCodesByLoginnameInCache(user.getLoginname());
            Set<String> serSet = serService.findUrlByRoleCodesInCache(roleCodes);
            Set<String> allSer = CacheKit.get(Consts.CACHE_NAMES.allSer.name(), "allSer");
            if (!allSer.contains(ak)) {
                invocation.invoke();//如果服务未配置 直接放行
                return;
            }
            if (serSet != null && serSet.contains(ak)) {
                flag = true;
            }
        }

        if(flag) {
            invocation.invoke();
        } else {
            if(ReqKit.isAjaxRequest(controller.getRequest())){
                controller.renderUnauthorizationJSON("sm");
            }else {
                throw new CoreException("访问权限认证失败！");
            }
        }
    }

}
