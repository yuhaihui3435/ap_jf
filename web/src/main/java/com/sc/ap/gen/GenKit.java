package com.sc.ap.gen;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.sc.ap.model.GenSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class GenKit {

    /**
     *
     * 转换table名=>class名
     *
     * @param tbl   table名
     * @param gsId  数据源id
     * @return
     */
    public static String getModelName(String tbl, Integer gsId) {
        GenSource genSource = GenSource.dao.findById(gsId);
        String prefixs = genSource.getRemovePrefix();
        String[] prefixs_array = prefixs!=null?prefixs.split(","):null;

        String tblName = tbl;
        String[] strings = tblName.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        if (prefixs_array!=null&&ArrayUtil.contains(prefixs_array, strings[0]+"_")) {
            for (int i = 1; i < strings.length; i++) {
                stringBuilder.append(strings[i]);
            }
        } else {
            for (int i = 0; i < strings.length; i++) {
                if(i==0)
                    stringBuilder.append(strings[i]);
                else
                    stringBuilder.append(StrUtil.upperFirst(strings[i]));
            }
        }
        return stringBuilder.toString();
    }

    public static String getClassName(String tbl, Integer gsId) {
        GenSource genSource = GenSource.dao.findById(gsId);
        String prefixs = genSource.getRemovePrefix();
        String[] prefixs_array = prefixs!=null?prefixs.split(","):null;

        String tblName = tbl;
        String[] strings = tblName.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        if (prefixs_array!=null&&ArrayUtil.contains(prefixs_array, strings[0]+"_")) {
            for (int i = 1; i < strings.length; i++) {
                stringBuilder.append(StrUtil.upperFirst(strings[i]));
            }
        } else {
            for (int i = 0; i < strings.length; i++) {
                stringBuilder.append(StrUtil.upperFirst(strings[i]));
            }
        }
        return stringBuilder.toString();
    }


    public static String getColumnComment(Connection connection, String tableSchema, String tableName, String columnName){
//        Map<String,String> map=new HashMap<>();
//        map.put("tableName",tableName);
//        map.put("tableSchema",tableSchema);
//        map.put("columnName",columnName);

        String sql="select column_comment from INFORMATION_SCHEMA.Columns where table_name='"+tableName+"' and table_schema='"+tableSchema+"' and column_name='"+columnName+"'";

        try {
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery();
            resultSet.next();
            return resultSet.getString("column_comment");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        SqlPara sqlPara= Db.getSqlPara("queryColumnComment",map);
//        Record record=Db.findFirst(sqlPara);
        return "";
    }

    public static String getTableComment(Connection connection, String tableSchema, String tableName) throws SQLException {
//        Map<String,String> map=new HashMap<>();
//        map.put("tableName",tableName);
//        map.put("tableSchema",tableSchema);
        String sql="select table_comment from INFORMATION_SCHEMA.Tables where table_name='"+tableName+"' and table_schema='"+tableSchema+"'";
//        SqlPara sqlPara= Db.getSqlPara("queryTableComment",map);
//        Record record=Db.findFirst(sqlPara);
//        return record.getStr("table_comment");

        try {
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery();
            resultSet.next();
            return resultSet.getString("table_comment");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
           if(connection!=null){
               connection.close();
           }
        }
        return "";
    }

}
