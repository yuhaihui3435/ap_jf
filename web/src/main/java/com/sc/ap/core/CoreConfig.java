package com.sc.ap.core;


import com.sc.ap.LoginCtr;
import com.sc.ap.controller.res.ResController;
import com.sc.ap.controller.role.RoleController;
import com.sc.ap.controller.ser.SerController;
import com.sc.ap.controller.user.UserController;
import com.sc.ap.gen.GenCtr;
import com.sc.ap.gen.GenSourceCtr;
import com.sc.ap.interceptors.UserInterceptor;
import com.sc.ap.controller.dd.DdCtr;
import com.sc.ap.controller.param.ParamCtr;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log4jLogFactory;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.JsonRender;
import com.jfinal.template.Engine;
import com.jfplugin.mail.MailPlugin;
import com.sc.ap.CMNCtr;
import com.sc.ap.Consts;
import com.sc.ap.IndexCtr;
import com.sc.ap.HomeCtr;
import com.sc.ap.interceptors.AdminAAuthInterceptor;
import com.sc.ap.interceptors.AdminIAuthInterceptor;
import com.sc.ap.interceptors.ExceptionInterceptor;
import com.sc.ap.kits.DateKit;
import com.sc.ap.kits.ResKit;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Created by yuhaihui8913 on 2017/11/14.
 */
public class CoreConfig extends JFinalConfig {

	static ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

	static {
		root.setLevel(ch.qos.logback.classic.Level.INFO);
	}

	@Override
	public void configConstant(Constants constants) {
		constants.setDevMode(ResKit.getConfigBoolean("devMode"));
		// constants.setMainRenderFactory(new BeetlRenderFactory());
		constants.setError500View("/WEB-INF/template/common/500.html");
		constants.setError404View("/WEB-INF/template/common/404.html");
		constants.setError403View("/WEB-INF/template/common/403.html");
		constants.setError401View("/WEB-INF/template/common/401.html");
		constants.setEncoding("UTF-8");
		constants.setJsonFactory(new FastJsonFactory());
		constants.setLogFactory(new Log4jLogFactory());
		constants.setJsonDatePattern(DateKit.STR_DATEFORMATE);

	}

	@Override
	public void configRoute(Routes routes) {
		routes.add(new Routes() {
			@Override
			public void config() {
				add("/param", ParamCtr.class);
				add("/dd", DdCtr.class);
				add("/gen", GenCtr.class);
				add("/genSource", GenSourceCtr.class);
				add("/user", UserController.class);
				add("/role", RoleController.class);
				add("/res", ResController.class);
				add("/ser", SerController.class);
				add("/home", HomeCtr.class);
				add("/cmn", CMNCtr.class);
				add("/", IndexCtr.class);
				add("/login", LoginCtr.class);
			}
		});
	}

	@Override
	public void configEngine(Engine engine) {
		engine.addSharedObject("ctx", JFinal.me().getContextPath());
		engine.addSharedMethod(new StrUtil());
		engine.addSharedObject("cKit", new CollectionUtil());
		engine.setDevMode(ResKit.getConfigBoolean("devMode", true));

		// 使用JF模板渲染通用页面
		engine.addSharedFunction("/WEB-INF/template/www/_layout.html");
	}

	@Override
	public void configPlugin(Plugins plugins) {
		// 开启druid数据库连接池
		DruidPlugin druidPlugin = createDruidPlugin();
		// StatFilter提供JDBC层的统计信息
		druidPlugin.addFilter(new StatFilter());
		// WallFilter的功能是防御SQL注入攻击
		WallFilter wallDefault = new WallFilter();
		wallDefault.setDbType("mysql");
		druidPlugin.addFilter(wallDefault);
		druidPlugin.setInitialSize(ResKit.getConfigInt("db.default.poolInitialSize"));
		druidPlugin.setMaxPoolPreparedStatementPerConnectionSize(ResKit.getConfigInt("db.default.poolMaxSize"));
		druidPlugin.setTimeBetweenConnectErrorMillis(ResKit.getConfigInt("db.default.connectionTimeoutMillis"));
		plugins.add(druidPlugin);
		// 开启DB+record 映射关系插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		_MappingKit.mapping(arp);
		arp.getEngine().setDevMode(true);
		arp.getEngine().addSharedMethod(new StrUtil());
		arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/sql");
		arp.addSqlTemplate("all.sql");
		arp.setShowSql(ResKit.getConfigBoolean("devMode")?true:false);
		plugins.add(arp);
		// 开启eheache缓存
		plugins.add(new EhCachePlugin());
		// 计划任务插件,开发模式默认关闭，如果需要测试自行打开，防止其他自动发起的对外界系统调用进行回掉
		if(!ResKit.getConfigBoolean("devMode")) {
			Cron4jPlugin cron4jPlugin = new Cron4jPlugin("task.properties", "cron4j");
			plugins.add(cron4jPlugin);
		}
//		MongoJFinalPlugin mongoJFinalPlugin = new MongoJFinalPlugin();
//		mongoJFinalPlugin.add(ResKit.getConfig("mongodb.ip"), ResKit.getConfigInt("mongodb.port"));
//		mongoJFinalPlugin.setDatabase(ResKit.getConfig("mongodb.db"));
//		mongoJFinalPlugin.auth(ResKit.getConfig("mongodb.user"), ResKit.getConfig("mongodb.pwd"));
//		plugins.add(mongoJFinalPlugin);
		// mail 插件
		plugins.add(new MailPlugin(PropKit.use("mail.properties").getProperties()));

	}

	private DruidPlugin createDruidPlugin() {
		DruidPlugin druidDefault = new DruidPlugin(ResKit.getConfig("db.default.url"),
				ResKit.getConfig("db.default.user"), ResKit.getConfig("db.default.password"),
				ResKit.getConfig("db.default.driver"));
		return druidDefault;
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		interceptors.add(new ExceptionInterceptor());
		interceptors.add(new AdminIAuthInterceptor());
		interceptors.add(new AdminAAuthInterceptor());
		interceptors.add(new UserInterceptor());
	}

	@Override
	public void configHandler(Handlers handlers) {
		
	}

	@Override
	public void afterJFinalStart() {
		super.afterJFinalStart();
		CoreData.loadAllCache();
		// 设置请求以json格式返回的时候，排除掉请求中用户身份信息相关的数据。
		JsonRender.addExcludedAttrs(Consts.CURR_USER, Consts.CURR_USER_MER, Consts.CURR_USER_RESES,
				Consts.CURR_USER_ROLES,Consts.CURR_USER_SERS);
	}
}
