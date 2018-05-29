package com.sc.ap.gen;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.sc.ap.core.CoreController;
import com.sc.ap.model.GenSource;
import com.sun.tools.javac.jvm.Gen;

/**
 * 简介
 * <p>
 * 项目名称:   [ap]
 * 包:        [com.sc.ap.gen]
 * 类名称:    [GenSourceCtr]
 * 类描述:    []
 * 创建人:    [于海慧]
 * 创建时间:  [2018/5/29]
 * 修改人:    []
 * 修改时间:  []
 * 修改备注:  []
 * 版本:     [v1.0]
 */
public class GenSourceCtr extends CoreController {

    public void page(){
        String name=getPara("name");
        Page<GenSource> page=GenSource.dao.paginate(getPN(),getPS(), Db.getSqlPara("genSql.byNameLike", Kv.by("name",name)));
        renderJson(page);
    }

    public void save(){
        GenSource genSource=getApModel(GenSource.class);
        genSource.save();
        renderSuccessJSON("数据源保存成功");
    }

    public void update(){
        GenSource genSource=getApModel(GenSource.class);
        genSource.update();
        renderSuccessJSON("数据源更新成功");
    }

    public void get(){
        int id=getParaToInt("id");
        renderJson(GenSource.dao.findById(id));
    }

    public void del(){
        int id=getParaToInt("id");
        GenSource.dao.deleteById(id);
        renderSuccessJSON("数据源删除成功");
    }
}
