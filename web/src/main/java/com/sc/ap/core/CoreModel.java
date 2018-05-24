package com.sc.ap.core;

import cn.hutool.core.util.ArrayUtil;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import com.sc.ap.Consts;
import com.sc.ap.kits.DateKit;
import org.jsoup.Jsoup;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 于海慧（125227112@qq.com） on 2016/11/30.
 */
public abstract class CoreModel<M extends CoreModel<M>> extends Model<M> {

	/**
	 * 防止xss攻击处理
	 * 
	 * @return
	 */

	public boolean xSSsave() {
		for (Map.Entry me : _getAttrs().entrySet()) {
			if (me.getValue() instanceof String) {
				set((String) me.getKey(), Jsoup.clean((String) me.getValue(), Consts.basicWithImages));
			}
		}
		return save();
	}
	@Override
	public boolean save() {

		String[] ans=_getAttrNames();
		if(ArrayUtil.contains(ans,"cAt")){
			set("cAt",new Date());
		}
		return super.save();
	}

	/**
	 * 防止xss攻击处理
	 * 
	 * @return
	 */
	public boolean xSSupdate() {
		for (Map.Entry me : _getAttrs().entrySet()) {
			if (me.getValue() instanceof String)
				set((String) me.getKey(), Jsoup.clean((String) me.getValue(), Consts.basicWithImages));
		}
		return update();
	}
	@Override
	public boolean update() {

		String[] ans=_getAttrNames();
		if(ArrayUtil.contains(ans,"lAt")){
			set("lAt",new Date());
		}
		return super.update();
	}

	public boolean apDel(){
		String[] ans=_getAttrNames();
		if(ArrayUtil.contains(ans,"dAt")){
			set("dAt",new Date());
		}
		return super.update();
	}


	public String getYOrNTxt(boolean val) {
		return (val) ? Consts.YORN.yes.getLabel() : Consts.YORN.no.getLabel();
	}

	public String getStatusTxt(String val) {
		if (val == null)
			return "";
		return (val.equals(Consts.STATUS.enable.getVal()) ? Consts.STATUS.enable.getValTxt()
				: Consts.STATUS.forbidden.getValTxt());
	}

	public List<M> findByPropEQ(String name, Object val) {
		return super.find("select * from " + getTableName() + " where " + name + "=?", val);
	}

	public M findFristByPropEQ(String name, Object val) {
		return super.findFirst("select * from " + getTableName() + " where " + name + "=?", val);
	}

	public List<M> findByPropLIKE(String name, String val) {
		return super.find("select * from " + getTableName() + " where " + name + " like ?", "%" + val + "%");
	}

	public List<M> findAll() {
		return super.find("select * from " + getTableName());
	}


	public  String getTableName(){
		return TableMapping.me().getTable(getClass()).getName();
	};

	public String getLAtStr(){
		String[] ans=_getAttrNames();
		if(ArrayUtil.contains(ans,"lAt")){
			Date lAt=getDate("lAt");
			if(lAt!=null){
				return DateKit.dateToStr(lAt,DateKit.STR_DATEFORMATE);
			}
		}
		return null;
	}

	public String getCAtStr(){
		String[] ans=_getAttrNames();
		if(ArrayUtil.contains(ans,"cAt")){
			Date cAt=getDate("cAt");
			if(cAt!=null){
				return DateKit.dateToStr(cAt,DateKit.STR_DATEFORMATE);
			}
		}
		return null;
	}

	public String getDAtStr(){
		String[] ans=_getAttrNames();
		if(ArrayUtil.contains(ans,"dAt")){
			Date dAt=getDate("dAt");
			if(dAt!=null){
				return DateKit.dateToStr(dAt,DateKit.STR_DATEFORMATE);
			}
		}
		return null;
	}

}
