package com.sc.ap.gen;

import cn.hutool.core.bean.BeanUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.plugin.activerecord.generator.TypeMapping;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.druid.DruidPlugin;
import com.sc.ap.core.CoreException;
import com.sc.ap.model.GenCfgCol;
import com.sc.ap.model.GenCfgTbl;
import com.sc.ap.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenSrv {

    protected MetaBuilder metaBuilder;
    protected Dialect dialect = null;
    private GenCfgTblSrv genCfgTblSrv = Duang.duang(GenCfgTblSrv.class.getSimpleName(), GenCfgTblSrv.class);
    private GenCfgColSrv genCfgColSrv = Duang.duang(GenCfgColSrv.class.getSimpleName(), GenCfgColSrv.class);

    public GenSrv() {

    }

    public GenSrv initDataSource(String url, String user, String pwd) {
        DruidPlugin druidPlugin = new DruidPlugin(url, user, pwd);
        druidPlugin.start();
        metaBuilder = new MetaBuilder(druidPlugin.getDataSource());
        return this;
    }


    public List<TableMeta> getTableMetas() {
        if (metaBuilder == null)
            throw new CoreException("MetaBuilder 获取失败");
        return metaBuilder.build();
    }


    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }
    @Before({Tx.class})
    public void saveCol(List<GenCfgCol> genCfgCols,Integer tblId) {
        GenCfgCol genCfgCol=null;
        GenCfgCol genCfgCol1=null;
        for (int i = 0; i < genCfgCols.size(); i++) {
            genCfgCol=genCfgCols.get(i);
            genCfgCol1=genCfgColSrv.findByTblIdAndCol(tblId,genCfgCol.getCol());
            if (genCfgCol1==null){
                genCfgCol.setTblId(tblId);
                genCfgCol.save();
            }else{
                genCfgCol1.setTpe(genCfgCol.getTpe());
                genCfgCol1.update();
            }
        }
    }
    @Before({Tx.class})
    public void saveTbl(List<GenCfgTbl> genCfgTblList) {
        GenCfgTbl genCfgTbl, genCfgTbl1;
        for (int i = 0; i < genCfgTblList.size(); i++) {
            genCfgTbl = genCfgTblList.get(i);
            genCfgTbl1 = genCfgTblSrv.findByGsIdAndTbl(genCfgTbl.getGsId(), genCfgTbl.getTbl());
            if (genCfgTbl1 == null) {
                genCfgTbl.save();
                saveCol(genCfgTbl.getGenCfgColList(),genCfgTbl.getId());
            }else{
                saveCol(genCfgTbl.getGenCfgColList(),genCfgTbl1.getId());
            }
        }

    }



}
