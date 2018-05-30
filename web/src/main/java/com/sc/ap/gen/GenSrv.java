package com.sc.ap.gen;

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
import com.jfinal.plugin.druid.DruidPlugin;
import com.sc.ap.core.CoreException;
import com.sc.ap.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenSrv {

    protected MetaBuilder metaBuilder;
    protected Dialect dialect = null;

    public GenSrv(){

    }

    public  GenSrv initDataSource(String url,String user,String pwd) {
        DruidPlugin druidPlugin = new DruidPlugin(url, user, pwd);
        druidPlugin.start();
        metaBuilder=new MetaBuilder(druidPlugin.getDataSource());
        return this;
    }


    public List<TableMeta> getTableMetas(){
        if(metaBuilder==null)
            throw new CoreException("MetaBuilder 获取失败");
        return metaBuilder.build();
    }


    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

}
