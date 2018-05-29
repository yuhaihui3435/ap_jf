package com.sc.ap.gen;

import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.sc.ap.core.CoreController;

import java.util.List;

public class GenCtr extends CoreController {

    private  GenSrv genSrv= Duang.duang(GenSrv.class.getSimpleName(),GenSrv.class);

    public void list_table(){
        List<TableMeta> list=genSrv.getTableMetas();
        renderJson(genSrv.getTableMetas());
    }

    public void list_table_column(){
        String tableName=getPara("tableName");

    }
}
