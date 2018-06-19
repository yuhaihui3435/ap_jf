package com.sc.ap.model;

import com.sc.ap.model.base.BaseSer;

import java.util.List;

/**
 * Generated by ap-jf.
 */
@SuppressWarnings("serial")
public class Ser extends BaseSer<Ser> {
    public static final Ser dao = new Ser().dao();

    public String getTypeStr() {
        return "0".equals(getType()) ? "服务" : "";
    }

    public String getLabel(){
        return this.getName();
    }


    public List<Ser> getChildren(){
        String sql="select * from s_ser where pId=? and dAt is null";
        List<Ser> serList=dao.find(sql,getId());
        return serList.isEmpty()?null:serList;
    }


}
