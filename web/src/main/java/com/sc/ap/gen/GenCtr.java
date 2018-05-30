package com.sc.ap.gen;

import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.sc.ap.core.CoreController;
import com.sc.ap.model.GenSource;

import java.util.List;

public class GenCtr extends CoreController {

    private  GenSrv genSrv= Duang.duang(GenSrv.class.getSimpleName(),GenSrv.class);

    public void list_table(){
        int gsId=getParaToInt("gsId");
        GenSource genSource=GenSource.dao.findById(gsId);
        List<TableMeta> list=genSrv.initDataSource(genSource.getUrl(),genSource.getUser(),genSource.getPwd()).getTableMetas();
        renderJson(list);
    }

    public void list_table_column(){
        String tableName=getPara("tableName");

    }
}
