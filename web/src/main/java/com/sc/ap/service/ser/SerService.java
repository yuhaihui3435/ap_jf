package com.sc.ap.service.ser;
import com.sc.ap.core.CoreService;
import com.sc.ap.model.Ser;
import com.sc.ap.query.SerQuery;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.kit.Kv;
import java.util.*;
import com.jfinal.plugin.activerecord.Page;

public class SerService extends CoreService{

    private static Ser serDao=Ser.dao;

    public List<Ser> findAll(SerQuery serQuery){
         Kv kv= Kv.create();
                    if(StrUtil.isNotBlank(serQuery.getCode())){
                        kv.put("code like","%"+serQuery.getCode()+"%");
                    }
                    if(StrUtil.isNotBlank(serQuery.getName())){
                        kv.put("name like","%"+serQuery.getName()+"%");
                    }
         kv.put("dAt","");
         if(StrUtil.isNotBlank(serQuery.getOrderBy())) {
             kv.put("orderBy", serQuery.getOrderBy());
         }
         kv=Ser.buildParamMap(Ser.class,kv);
         return serDao.findByAndCond(kv);
    }


    public Page<Ser> findPage(SerQuery serQuery){
        Kv kv= Kv.create();
                        if(StrUtil.isNotBlank(serQuery.getCode())){
                            kv.put("code like","%"+serQuery.getCode()+"%");
                        }
                        if(StrUtil.isNotBlank(serQuery.getName())){
                            kv.put("name like","%"+serQuery.getName()+"%");
                        }
             kv.put("dAt","");
             if(StrUtil.isNotBlank(serQuery.getOrderBy())) {
                kv.put("orderBy", serQuery.getOrderBy());
             }
             kv=Ser.buildParamMap(Ser.class,kv);
             return serDao.pageByAndCond(kv,serQuery.getPn(),serQuery.getPs());
    }

    public Ser findOne(Integer id){
           return serDao.findById(id);
    }
    @Before({Tx.class})
    public void save(Ser ser){
        ser.save();
    }
    @Before({Tx.class})
    public void update(Ser ser){
            ser.update();
    }
    @Before({Tx.class})
    public void logicDel(Integer id,Integer opId){
            Ser ser=findOne(id);

                if(opId!=null){
                    ser.setOpId(opId);
                }

            ser.apDel();

    }
    @Before({Tx.class})
    public void del(Integer id){
            Ser ser=findOne(id);
            ser.delete();
    }
    @Before({Tx.class})
    public void batchLogicDel(Integer[] ids,Integer opId){
            if(ids!=null){
                for(Integer id:ids){
                    logicDel(id,opId);
                }
            }
    }
    @Before({Tx.class})
    public void batchDel(Integer[] ids){
           if(ids!=null){
                for(Integer id:ids){
                    del(id);
                }
           }
    }
}

