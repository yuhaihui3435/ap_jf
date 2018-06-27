package com.sc.ap.interceptors;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Duang;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sc.ap.Consts;
import com.sc.ap.model.User;
import com.sc.ap.core.CoreController;
import com.sc.ap.core.CoreException;
import com.sc.ap.kits.CookieKit;
import com.sc.ap.kits.ReqKit;
import com.sc.ap.kits.ResKit;
import com.sc.ap.service.user.UserService;

import java.math.BigInteger;

/**
 *
 * 用户身份认证，前置处理
 *
 *
 */
public class AdminIAuthInterceptor implements Interceptor {

	private UserService userService= Duang.duang(UserService.class.getSimpleName(),UserService.class);
	public void intercept(Invocation inv) {
		CoreController controller = (CoreController) inv.getController();
		String userId = CookieKit.get(controller, Consts.USER_ACCESS_TOKEN);
		boolean flag = false;
		User user = null;
		if (StrUtil.isNotBlank(userId)) {
			user = userService.findByIdInCache(new Integer(userId));
			if (user != null) {
				flag = true;
			}
		}

		if (flag) {

			String reqCookieVal=controller.getCookie(Consts.USER_ACCESS_TOKEN);
			String currCookieVal=CacheKit.get(Consts.CURR_USER_COOKIE,"user_"+userId);
			if(StrUtil.isBlank(currCookieVal)){
				if (ReqKit.isAjaxRequest(controller.getRequest())) {
					controller.renderAuth901(null);
					return;
				}else{
					throw new CoreException(CoreController.ERR_MSG_901);
				}
			}
			if(reqCookieVal.equals(currCookieVal)){
				inv.invoke();
			}else{
				if (ReqKit.isAjaxRequest(controller.getRequest())) {
					controller.renderAuth900(null);
				} else {
					throw new CoreException(CoreController.ERR_MSG_900);
				}
			}
		} else {
			CookieKit.remove(controller, Consts.USER_ACCESS_TOKEN);
			if (ReqKit.isAjaxRequest(controller.getRequest())) {
				controller.renderAuth401(null);
			} else {
				throw new CoreException(CoreController.ERR_MSG_401);
			}
		}
	}
}
