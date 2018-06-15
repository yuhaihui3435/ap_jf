package com.sc.ap.service.res;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sc.ap.core.CoreService;
import com.sc.ap.model.Res;
import com.sc.ap.query.ResQuery;

import java.util.List;

public class ResService extends CoreService {

    private static Res resDao = Res.dao;

    public List<Res> findAll(ResQuery resQuery) {
        Kv kv = Kv.create();
        if (StrUtil.isNotBlank(resQuery.getName())) {
            kv.put("name like", "%" + resQuery.getName() + "%");
        }
        if (StrUtil.isNotBlank(resQuery.getEnabled())) {
            kv.put("enabled=", resQuery.getEnabled());
        }
        if (resQuery.getPId()!=null) {
            kv.put("pId=", resQuery.getPId());
        }
        if (StrUtil.isNotBlank(resQuery.getCode())) {
            kv.put("code like", "%" + resQuery.getCode() + "%");
        }
        kv.put("dAt", "");
        if (StrUtil.isNotBlank(resQuery.getOrderBy())) {
            kv.put("orderBy", resQuery.getOrderBy());
        }
        kv = Res.buildParamMap(Res.class, kv);
        return resDao.findByAndCond(kv);
    }


    public Page<Res> findPage(ResQuery resQuery) {
        Kv kv = Kv.create();
        if (StrUtil.isNotBlank(resQuery.getName())) {
            kv.put("name like", "%" + resQuery.getName() + "%");
        }
        if (StrUtil.isNotBlank(resQuery.getEnabled())) {
            kv.put("enabled=", resQuery.getEnabled());
        }
        if (resQuery.getPId()!=null) {
            kv.put("pId=", resQuery.getPId());
        }
        if (StrUtil.isNotBlank(resQuery.getCode())) {
            kv.put("code like", "%" + resQuery.getCode() + "%");
        }
        kv.put("dAt", "");
        if (StrUtil.isNotBlank(resQuery.getOrderBy())) {
            kv.put("orderBy", resQuery.getOrderBy());
        }
        kv = Res.buildParamMap(Res.class, kv);
        return resDao.pageByAndCond(kv, resQuery.getPn(), resQuery.getPs());
    }

    public Res findOne(Integer id) {
        return resDao.findById(id);
    }

    @Before({Tx.class})
    public void save(Res res) {
        res.save();
    }

    @Before({Tx.class})
    public void update(Res res) {
        res.update();
    }

    @Before({Tx.class})
    public void logicDel(Integer id, Integer opId) {
        Res res = findOne(id);

        if (opId != null) {
            res.setOpId(opId);
        }

        res.apDel();

    }

    @Before({Tx.class})
    public void del(Integer id) {
        Res res = findOne(id);
        res.delete();
    }

    @Before({Tx.class})
    public void batchLogicDel(Integer[] ids, Integer opId) {
        if (ids != null) {
            for (Integer id : ids) {
                logicDel(id, opId);
            }
        }
    }

    @Before({Tx.class})
    public void batchDel(Integer[] ids) {
        if (ids != null) {
            for (Integer id : ids) {
                del(id);
            }
        }
    }
}

