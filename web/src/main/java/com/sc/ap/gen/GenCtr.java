package com.sc.ap.gen;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Duang;
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
        for(TableMeta tableMeta:list) {
            genCfgTbl = new GenCfgTbl();
            genCfgTbl.setGsId(gsId);
            genCfgTbl.setTbl(tableMeta.name);
            genCfgTbl.setNote(GenKit.getTableComment(genSource.getName(),tableMeta.name));
            for (ColumnMeta columnMeta : tableMeta.columnMetas) {
                genCfgCol = new GenCfgCol();
                genCfgCol.setTpe(columnMeta.javaType);
                genCfgCol.setCol(columnMeta.attrName);
                genCfgCol.setOrgCol(columnMeta.name);
                genCfgCol.setCAt(new Date());
                genCfgCol.setNote(GenKit.getColumnComment(genSource.getName(),tableMeta.name,columnMeta.name));
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

    public void genCode(){
        String action=getPara("action");
        Integer[] tblIds=getParaValuesToInt("tblId");
        String jsName=getPara("jsName","vuetify");

        Map data;
        for (Integer tblId:tblIds){
            data=genSrv.getGenData(tblId);
            genSrv.genCodeFile(action,jsName,data);
        }
        renderSuccessJSON("生成代码成功请查看");
    }

    public void list_genCode(){
        Integer tblId=getParaToInt("tblId");
        List<GenSrv.FileInfo> javaCodes=genSrv.getGenJavaCode(tblId);
        List<GenSrv.FileInfo> vuejsCodes=genSrv.getGenVuejsCode(tblId);
        Map<String,Object> ret=new HashMap<>();
        ret.put("javaCodes",javaCodes);
        ret.put("vuejsCodes",vuejsCodes);
        renderJson(ret);
    }

}
