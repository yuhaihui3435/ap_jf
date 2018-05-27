package com.sc.ap.sm.dd;

import com.jfinal.aop.Before;
import com.sc.ap.core.CoreController;
import com.sc.ap.model.Dd;

/**
 * 简介
 * <p>
 * 项目名称:   [ap]
 * 包:        [com.sc.ap.sm.dd]
 * 类名称:    [DdCtr]
 * 类描述:    []
 * 创建人:    [于海慧]
 * 创建时间:  [2018/5/26]
 * 修改人:    []
 * 修改时间:  []
 * 修改备注:  []
 * 版本:     [v1.0]
 */
public class DdCtr extends CoreController {

    public void list(){
        renderJson(Dd.dao.findParentAll());
    }
    @Before({DdValidator.class})
    public void save(){
        Dd dd=getApModel(Dd.class);
        if(currUser()!=null)dd.setOpId(currUser().getId());
        dd.save();
        renderSuccessJSON("数据字典新增成功");
    }
    @Before({DdValidator.class})
    public void update(){
        Dd dd=getApModel(Dd.class);
        if(currUser()!=null)dd.setOpId(currUser().getId());
        dd.update();
        renderSuccessJSON("数据字典修改成功");
    }

    public void del(){
        int id=getParaToInt("id");
        Dd dd=Dd.dao.findById(id);
        dd.apDel();
        renderSuccessJSON("数据字典删除成功");
    }

    public void get(){
        int id=getParaToInt("id");
        renderJson(Dd.dao.findById(id));
    }
    public void getByModule(){
        String module=getPara("module");
        renderJson(Dd.dao.findByDict(module));
    }
}
