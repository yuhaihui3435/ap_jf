package com.sc.ap.gen;

import com.sc.ap.model.GenCfgCol;

import java.util.HashMap;
import java.util.Map;

public class GenCfgColSrv {

    public GenCfgCol findByTblIdAndCol(Integer tblId,String col){
        Map<String,Object> map=new HashMap<>();

        map.put("tblId=",tblId);
        map.put("col=",col);

        return GenCfgCol.dao.findFirstByAndCond(map);
    }

}
