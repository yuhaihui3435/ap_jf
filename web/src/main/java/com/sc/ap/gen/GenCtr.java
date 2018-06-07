package com.sc.ap.gen;

import com.jfinal.aop.Duang;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.sc.ap.core.CoreController;
import com.sc.ap.model.GenCfgCol;
import com.sc.ap.model.GenCfgTbl;
import com.sc.ap.model.GenSource;

import java.util.*;

public class GenCtr extends CoreController {

    private  GenSrv genSrv= Duang.duang(GenSrv.class.getSimpleName(),GenSrv.class);


    /**
     * 同步数据源中 表，字段的到本地数据库
     *
     */
    public void syncLocal(){
        int gsId=getParaToInt("gsId");
        GenSource genSource=GenSource.dao.findById(gsId);
        List<TableMeta> list=genSrv.initDataSource(genSource.getUrl(),genSource.getUser(),genSource.getPwd()).getTableMetas();
        GenCfgTbl genCfgTbl=null;
        GenCfgCol genCfgCol=null;
        List<GenCfgTbl> genCfgTbls=new ArrayList<>();
        for(TableMeta tableMeta:list){
            genCfgTbl=new GenCfgTbl();
            genCfgTbl.setGsId(gsId);
            genCfgTbl.setTbl(tableMeta.name);
            for (ColumnMeta columnMeta:tableMeta.columnMetas){
                genCfgCol=new GenCfgCol();
                genCfgCol.setTpe(columnMeta.javaType);
                genCfgCol.setCol(columnMeta.attrName);
                genCfgCol.setCAt(new Date());
                genCfgTbl.addGenCfgCol(genCfgCol);
            }
            genCfgTbls.add(genCfgTbl);
        }

        genSrv.saveTbl(genCfgTbls);

        renderSuccessJSON("数据源中表信息本地同步成功");

    }


    public void list_table(){
        int gsId=getParaToInt("gsId");
        List<GenCfgTbl> list=GenCfgTbl.dao.findByPropEQ("gsId",gsId);
        renderJson(list);
    }



    public void saveColConfig(){
        GenCfgCol genCfgCol=getApModel(GenCfgCol.class);
        if(currUser()!=null)genCfgCol.setOpId(currUser().getId());
        genCfgCol.update();
        renderSuccessJSON("字段配置成功");
    }
}
