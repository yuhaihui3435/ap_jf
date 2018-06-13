package com.sc.ap.service.role;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sc.ap.core.CoreService;
import com.sc.ap.model.Role;
import com.sc.ap.query.RoleQuery;

import java.util.List;

public class RoleService extends CoreService {

    private static Role roleDao = Role.dao;

    public List<Role> findAll(RoleQuery roleQuery) {
        Kv kv = Kv.create();
        if (StrUtil.isNotBlank(roleQuery.getName())) {
            kv.put("name like", "%" + roleQuery.getName() + "%");
        }
        if (StrUtil.isNotBlank(roleQuery.getCode())) {
            kv.put("code like", "%" + roleQuery.getCode() + "%");
        }
        kv.put("dAt", "");
        if (StrUtil.isNotBlank(roleQuery.getOrderBy())) {
            kv.put("orderBy", roleQuery.getOrderBy());
        }
        kv = Role.buildParamMap(Role.class, kv);
        return roleDao.findByAndCond(kv);
    }


    public Page<Role> findPage(RoleQuery roleQuery) {
        Kv kv = Kv.create();
        if (StrUtil.isNotBlank(roleQuery.getName())) {
            kv.put("name like", "%" + roleQuery.getName() + "%");
        }
        if (StrUtil.isNotBlank(roleQuery.getCode())) {
            kv.put("code like", "%" + roleQuery.getCode() + "%");
        }
        kv.put("dAt", "");
        if (StrUtil.isNotBlank(roleQuery.getOrderBy())) {
            kv.put("orderBy", roleQuery.getOrderBy());
        }
        kv = Role.buildParamMap(Role.class, kv);
        return roleDao.pageByAndCond(kv, roleQuery.getPn(), roleQuery.getPs());
    }

    public Role findOne(Integer id) {
        return roleDao.findById(id);
    }

    @Before({Tx.class})
    public void save(Role role) {
        role.save();
    }

    @Before({Tx.class})
    public void update(Role role) {
        role.update();
    }

    @Before({Tx.class})
    public void logicDel(Integer id, Integer opId) {
        Role role = findOne(id);

        if (opId != null) {
            role.setOpId(opId);
        }

        role.apDel();

    }

    @Before({Tx.class})
    public void del(Integer id) {
        Role role = findOne(id);
        role.delete();
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

