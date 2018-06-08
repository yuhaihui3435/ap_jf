package com.sc.ap.gen;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.sc.ap.model.GenSource;

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

}
