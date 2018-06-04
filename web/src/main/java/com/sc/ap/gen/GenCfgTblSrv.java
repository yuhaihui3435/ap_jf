package com.sc.ap.gen;

import com.jfinal.kit.Kv;
import com.sc.ap.model.GenCfgTbl;

public class GenCfgTblSrv {

    public GenCfgTbl findByGsIdAndTbl(Integer gsId,String tbl){
        Kv kv=Kv.create();
        kv.put("gsId=",gsId);
        kv.put("tbl=",tbl);
        return GenCfgTbl.dao.findFirstByAndCond(kv);
    }




}
