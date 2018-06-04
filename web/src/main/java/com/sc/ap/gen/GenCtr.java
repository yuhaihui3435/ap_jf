package com.sc.ap.gen;

import com.jfinal.aop.Duang;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.sc.ap.core.CoreController;
import com.sc.ap.model.GenSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String colName=getPara("colName");
        Integer gsId=getParaToInt("gsId");

        Map<String,Object> map=new HashMap<>();
        map.put("tabl=",tableName);
        map.put("col=",colName);
        map.put("gsId=",gsId);

    }
}
