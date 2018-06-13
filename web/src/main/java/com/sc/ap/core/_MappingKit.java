package com.sc.ap.core;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.sc.ap.model.*;


/**
 * Generated by JFinal, do not modify this file.
 * 
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {

		arp.addMapping("s_log_op", "id", LogOp.class);

		arp.addMapping("s_param", "id", Param.class);
		arp.addMapping("s_res", "id", Res.class);
		arp.addMapping("s_ser", "id", Ser.class);
		arp.addMapping("s_role", "id", Role.class);
		arp.addMapping("s_role_res", "id", RoleRes.class);

		arp.addMapping("s_user", "id", User.class);
		arp.addMapping("s_user_role", "id", UserRole.class);
		arp.addMapping("s_dd","id",Dd.class);
		arp.addMapping("s_gen_cfg_tbl","id",GenCfgTbl.class);
		arp.addMapping("s_gen_cfg_col","id",GenCfgCol.class);
		arp.addMapping("s_gen_source","id",GenSource.class);

	}
}
