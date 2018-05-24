package com.sc.ap.interceptors;


import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.sc.ap.Consts;
import com.sc.ap.model.Res;
import com.sc.ap.model.Role;
import com.sc.ap.model.User;
import com.sc.ap.core.CoreController;
import com.sc.ap.kits.CookieKit;


import java.math.BigInteger;

public class UserInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        CoreController controller = (CoreController)inv.getController();
        String userId= CookieKit.get(controller, Consts.USER_ACCESS_TOKEN);
        if(StrUtil.isNotBlank(userId)) {
            User user = User.dao.findFirstByCache(Consts.CACHE_NAMES.user.name(), new BigInteger(userId), "select * from s_user where status='0' and id=? ", new BigInteger(userId));
            controller.setAttr(Consts.CURR_USER, user);
//            controller.setAttr(Consts.CURR_USER_ROLES, Role.dao.findRolesByUserId(user.getId()));
//            controller.setAttr(Consts.CURR_USER_RESES, Res.dao.findAllResStrByUserId(user.getId()));
        }


        inv.invoke();
    }
}
