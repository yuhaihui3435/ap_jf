package com.sc.ap.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.kit.StrKit;
import com.sc.ap.gen.GenKit;
import com.sc.ap.model.base.BaseGenCfgTbl;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class GenCfgTbl extends BaseGenCfgTbl<GenCfgTbl> {

	private String modelName;

	private String className;


	public static final GenCfgTbl dao = new GenCfgTbl().dao();

	private List<GenCfgCol> genCfgColList=new ArrayList<>();

	public void addGenCfgCol(GenCfgCol genCfgCol){
		genCfgColList.add(genCfgCol);
	}

	public GenCfgCol getGenCfgCol(int idx){
		return genCfgColList.get(idx);
	}

	public List<GenCfgCol> getGenCfgColList() {
		if(genCfgColList.isEmpty())
			return GenCfgCol.dao.findByPropEQ("tblId",getId());
		else
			return genCfgColList;
	}

	public void setGenCfgColList(List<GenCfgCol> genCfgColList) {
		this.genCfgColList = genCfgColList;
	}


	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getClassName() {

		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
