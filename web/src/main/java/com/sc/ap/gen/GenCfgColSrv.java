package com.sc.ap.gen;

import com.jfinal.kit.Kv;
import com.sc.ap.model.GenCfgCol;
import com.sc.ap.model.GenCfgTbl;

import java.util.HashMap;
import java.util.Map;

public class GenCfgColSrv {

    public GenCfgCol findByTblIdAndCol(Integer tblId,String col){
        Kv kv= Kv.create();
        kv.put("tblId=",tblId);
        kv.put("col=",col);
        kv=GenCfgCol.buildParamMap(GenCfgCol.class,kv);
        return GenCfgCol.dao.findFirstByAndCond(kv);
    }

}
