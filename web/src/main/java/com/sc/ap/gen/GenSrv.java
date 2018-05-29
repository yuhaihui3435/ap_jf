package com.sc.ap.gen;

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

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenSrv {

    protected MetaBuilder metaBuilder;
    protected Dialect dialect = null;

    public GenSrv(){
        metaBuilder=new MetaBuilder(getDataSource());

    }

    public static DataSource getDataSource() {
        Prop p = PropKit.use("config.properties");
        DruidPlugin druidPlugin = new DruidPlugin(p.get("db.default.url"), p.get("db.default.user"), p.get("db.default.password"));
        druidPlugin.start();
        return druidPlugin.getDataSource();
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
