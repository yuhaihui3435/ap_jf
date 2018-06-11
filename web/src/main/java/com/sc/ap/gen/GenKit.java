package com.sc.ap.gen;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.sc.ap.model.GenSource;

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
        String[] prefixs_array = prefixs.split(",");

        String tblName = tbl;
        String[] strings = tblName.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        if (prefixs_array!=null&&ArrayUtil.contains(prefixs_array, strings[0]+"_")) {
            for (int i = 1; i < strings.length; i++) {
                stringBuilder.append(strings[i]);
            }
        } else {
            for (int i = 0; i < strings.length; i++) {
                stringBuilder.append(strings[i]);
            }
        }
        return stringBuilder.toString();
    }


    public static String getColumnComment(String tableSchema,String tableName,String columnName){
        Map<String,String> map=new HashMap<>();
        map.put("tableName",tableName);
        map.put("tableSchema",tableSchema);
        map.put("columnName",columnName);
        SqlPara sqlPara= Db.getSqlPara("queryColumnComment",map);
        Record record=Db.findFirst(sqlPara);
        return record.getStr("column_comment");
    }

    public static String getTableComment(String tableSchema,String tableName){
        Map<String,String> map=new HashMap<>();
        map.put("tableName",tableName);
        map.put("tableSchema",tableSchema);
        SqlPara sqlPara= Db.getSqlPara("queryTableComment",map);
        Record record=Db.findFirst(sqlPara);
        return record.getStr("table_comment");
    }

}
