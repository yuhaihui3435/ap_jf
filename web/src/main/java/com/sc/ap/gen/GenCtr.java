package com.sc.ap.gen;

import cn.hutool.db.DbUtil;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sc.ap.Consts;
import com.sc.ap.core.CoreController;
import com.sc.ap.model.GenCfgCol;
import com.sc.ap.model.GenCfgTbl;
import com.sc.ap.model.GenSource;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.*;

public class GenCtr extends CoreController {

    private  GenSrv genSrv= Duang.duang(GenSrv.class.getSimpleName(),GenSrv.class);


    /**
     * 同步数据源中 表，字段的到本地数据库
     *
     */
    public void syncLocal() throws SQLException {
        int gsId=getParaToInt("gsId");
        GenSource genSource=GenSource.dao.findById(gsId);
        List<TableMeta> list=genSrv.initDataSource(genSource.getUrl(),genSource.getUser(),genSource.getPwd()).getTableMetas();
        DataSource ds = new SimpleDataSource(genSource.getUrl(), genSource.getUser(), genSource.getPwd(),"com.mysql.jdbc.Driver");
        GenCfgTbl genCfgTbl=null;
        GenCfgCol genCfgCol=null;
        List<GenCfgTbl> genCfgTbls=new ArrayList<>();
        for(TableMeta tableMeta:list) {
            genCfgTbl = new GenCfgTbl();
            genCfgTbl.setGsId(gsId);
            genCfgTbl.setTbl(tableMeta.name);
            genCfgTbl.setNote(GenKit.getTableComment(ds.getConnection(),genSource.getName(),tableMeta.name));
            for (ColumnMeta columnMeta : tableMeta.columnMetas) {
                genCfgCol = new GenCfgCol();
                genCfgCol.setTpe(columnMeta.javaType);
                genCfgCol.setCol(columnMeta.attrName);
                genCfgCol.setOrgCol(columnMeta.name);
                genCfgCol.setCAt(new Date());
                genCfgCol.setNote(GenKit.getColumnComment(ds.getConnection(),genSource.getName(),tableMeta.name,columnMeta.name));
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
        List ret=new ArrayList();
        for (Integer tblId:tblIds){
            data=genSrv.getGenData(tblId);
            ret.add(genSrv.genCodeFile(action,jsName,data));
        }
        renderSuccessJSON("生成代码成功请查看",JSON.toJSONString(ret) );
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


    public void getProjectTemplates(){
        String action=getPara("action");
        if("backendProject".equals(action))
            renderJson(CacheKit.get(Consts.CACHE_NAMES.dd.name(),"backendProjectType".concat("List")));
        else
            renderJson(CacheKit.get(Consts.CACHE_NAMES.dd.name(),"frontendProjectType".concat("List")));
    }

    public void genProject(){
        Method[] methods=GenSrv.class.getDeclaredMethods();
        String templateName=getPara("templateName");
        String groupId=getPara("groupId");
        String artifactId=getPara("artifactId");
        String projectName=getPara("projectName");
        String projectDesc=getPara("projectDesc");
        String projectAuthor=getPara("projectAuthor");
        String action=getPara("action");
        Map<String,String> map=new HashMap<>();
        map.put("templateName",templateName);
        map.put("groupId",groupId);
        map.put("artifactId",artifactId);
        map.put("projectName",projectName);
        map.put("projectDesc",projectDesc);
        map.put("projectAuthor",projectAuthor);
        String ret=null;
        if("backendProject".equals(action)) {
            ret= genSrv.genBackendProject(map);
        }else {
            ret=genSrv.genFrontendProject(map);
        }
        renderSuccessJSON("项目生成成功", ret);
    }

}
