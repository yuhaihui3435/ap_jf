package com.sc.ap.service.res;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sc.ap.core.CoreService;
import com.sc.ap.model.Res;
import com.sc.ap.query.ResQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public List<Res> findByRoleCodes(String[] roleCodes){
        String s= ArrayUtil.join(roleCodes,",");
        String sql="select r.* from s_role_res rr left join s_res r on rr.roleCode=r.code where r.enabled='0' and dAt is null and rr.roleCode in (?)";
        return Res.dao.find(sql,s);
    }

    public Set<String> findUrlByRoleCodes(String[] roleCodes){
        Set<String> ret=new HashSet<>();
        List<Res> resList=findByRoleCodes(roleCodes);
        for (Res res:resList){
            ret.add(res.getUrl());
        }
        return ret;
    }




}

