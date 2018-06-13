package com.sc.ap.sm.user;

import com.sc.ap.core.CoreService;
import com.sc.ap.model.User;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.kit.Kv;

import java.util.*;

import com.jfinal.plugin.activerecord.Page;

public class UserService extends CoreService {

    private static User userdao = User.dao;

    public List<User> findAll(User user) {
        Kv kv = Kv.create();
        if (StrUtil.isNotBlank(user.getLoginname())) {
            kv.put("loginname like", "%" + user.getLoginname() + "%");
        }
        if (StrUtil.isNotBlank(user.getNickname())) {
            kv.put("nickname like", "%" + user.getNickname() + "%");
        }
        if (StrUtil.isNotBlank(user.getPhone())) {
            kv.put("phone=", user.getPhone());
        }
        if (StrUtil.isNotBlank(user.getEmail())) {
            kv.put("email=", user.getEmail());
        }
        if (StrUtil.isNotBlank(user.getStatus())) {
            kv.put("status=", user.getStatus());
        }
        if (StrUtil.isNotBlank(user.getBeginCAt())) {
            kv.put("cAt >=", user.getBeginCAt());
        }
        if (StrUtil.isNotBlank(user.getEndCAt())) {
            kv.put("cAt <=", user.getEndCAt());
        }
        kv = User.buildParamMap(User.class, kv);
        return userdao.findByAndCond(kv);
    }


    public Page<User> findPage(User user, int pn, int ps) {
        Kv kv = Kv.create();
        if (StrUtil.isNotBlank(user.getLoginname())) {
            kv.put("loginname like", "%" + user.getLoginname() + "%");
        }
        if (StrUtil.isNotBlank(user.getNickname())) {
            kv.put("nickname like", "%" + user.getNickname() + "%");
        }
        if (StrUtil.isNotBlank(user.getPhone())) {
            kv.put("phone=", user.getPhone());
        }
        if (StrUtil.isNotBlank(user.getEmail())) {
            kv.put("email=", user.getEmail());
        }
        if (StrUtil.isNotBlank(user.getStatus())) {
            kv.put("status=", user.getStatus());
        }
        if (StrUtil.isNotBlank(user.getBeginCAt())) {
            kv.put("cAt >=", user.getBeginCAt());
        }
        if (StrUtil.isNotBlank(user.getEndCAt())) {
            kv.put("cAt <=", user.getEndCAt());
        }
        kv = User.buildParamMap(User.class, kv);
        return userdao.pageByAndCond(kv, pn, ps);
    }

    public User findOne(Integer id) {
        return userdao.findById(id);
    }
    @Before({Tx.class})
    public void save(User user) {
        user.save();
    }
    @Before({Tx.class})
    public void update(User user) {
        user.update();
    }

    @Before({Tx.class})
    public void logicDel(Integer id, Integer opId) {
        User user = findOne(id);
        if (opId != null) {
            user.setOpId(opId);
        }
        user.apDel();
    }
    @Before({Tx.class})
    public void del(Integer id) {
        User user = findOne(id);
        user.delete();
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

