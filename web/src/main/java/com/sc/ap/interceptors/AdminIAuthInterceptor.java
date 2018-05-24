package com.sc.ap.interceptors;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.sc.ap.Consts;
import com.sc.ap.model.User;
import com.sc.ap.core.CoreController;
import com.sc.ap.core.CoreException;
import com.sc.ap.kits.CookieKit;
import com.sc.ap.kits.ReqKit;
import com.sc.ap.kits.ResKit;

import java.math.BigInteger;

/**
 *
 * 用户身份认证，前置处理
 *
 *
 */
public class AdminIAuthInterceptor implements Interceptor {
	public void intercept(Invocation inv) {
		CoreController controller = (CoreController) inv.getController();
		String userId = CookieKit.get(controller, Consts.USER_ACCESS_TOKEN);
		boolean flag = false;
		User user = null;
		if (StrUtil.isNotEmpty(userId)) {
			flag = true;
			user = User.dao.findFirstByCache(Consts.CACHE_NAMES.user.name(), new BigInteger(userId),
					"select * from s_user where status='0' and id=? ", new BigInteger(userId));
			if (user == null) {
				if (ReqKit.isAjaxRequest(controller.getRequest())) {
					controller.renderUnauthenticationJSON("admin");

				} else {
					throw new CoreException("你的账户被停用");
				}
			}
		}
		// 是否需要用户身份认证,方便测试
		if (!ResKit.getConfigBoolean("userAuth"))
			flag = true;

		if (flag) {
			inv.invoke();
		} else {
			CookieKit.remove(controller, Consts.USER_ACCESS_TOKEN);
			if (ReqKit.isAjaxRequest(controller.getRequest())) {
				controller.renderUnauthenticationJSON("admin");
			} else {
				throw new CoreException("身份认证失败，请登录！");
			}
		}
	}
}
