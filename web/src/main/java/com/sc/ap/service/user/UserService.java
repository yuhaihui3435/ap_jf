package com.sc.ap.service.user;
import com.sc.ap.core.CoreService;
import com.sc.ap.model.User;
import com.sc.ap.query.UserQuery;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.kit.Kv;
import java.util.*;
import com.jfinal.plugin.activerecord.Page;

public class UserService extends CoreService{

    private static User userDao=User.dao;

    public List<User> findAll(UserQuery userQuery){
        Kv kv= Kv.create();
        if(StrUtil.isNotBlank(userQuery.getLoginname())){
            kv.put("loginname like","%"+userQuery.getLoginname()+"%");
        }
        if(StrUtil.isNotBlank(userQuery.getNickname())){
            kv.put("nickname like","%"+userQuery.getNickname()+"%");
        }
        if(StrUtil.isNotBlank(userQuery.getPhone())){
            kv.put("phone=",userQuery.getPhone());
        }
        if(StrUtil.isNotBlank(userQuery.getEmail())){
            kv.put("email=",userQuery.getEmail());
        }
        if(StrUtil.isNotBlank(userQuery.getStatus())){
            kv.put("status=",userQuery.getStatus());
        }
        if(StrUtil.isNotBlank(userQuery.getBeginCAt())){
            kv.put("cAt >=",userQuery.getBeginCAt());
        }
        if(StrUtil.isNotBlank(userQuery.getEndCAt())){
            kv.put("cAt <=",userQuery.getEndCAt());
        }
        kv.put("dAt","");
        if(StrUtil.isNotBlank(userQuery.getOrderBy())) {
            kv.put("orderBy", userQuery.getOrderBy());
        }
        kv=User.buildParamMap(User.class,kv);
        return userDao.findByAndCond(kv);
    }


    public Page<User> findPage(UserQuery userQuery){
        Kv kv= Kv.create();
        if(StrUtil.isNotBlank(userQuery.getLoginname())){
            kv.put("loginname like","%"+userQuery.getLoginname()+"%");
        }
        if(StrUtil.isNotBlank(userQuery.getNickname())){
            kv.put("nickname like","%"+userQuery.getNickname()+"%");
        }
        if(StrUtil.isNotBlank(userQuery.getPhone())){
            kv.put("phone=",userQuery.getPhone());
        }
        if(StrUtil.isNotBlank(userQuery.getEmail())){
            kv.put("email=",userQuery.getEmail());
        }
        if(StrUtil.isNotBlank(userQuery.getStatus())){
            kv.put("status=",userQuery.getStatus());
        }
        if(StrUtil.isNotBlank(userQuery.getBeginCAt())){
            kv.put("cAt >=",userQuery.getBeginCAt());
        }
        if(StrUtil.isNotBlank(userQuery.getEndCAt())){
            kv.put("cAt <=",userQuery.getEndCAt());
        }
        kv.put("dAt","");
        if(StrUtil.isNotBlank(userQuery.getOrderBy())) {
            kv.put("orderBy", userQuery.getOrderBy());
        }
        kv=User.buildParamMap(User.class,kv);
        return userDao.pageByAndCond(kv,userQuery.getPn(),userQuery.getPs());
    }

    public User findOne(Integer id){
        return userDao.findById(id);
    }
    @Before({Tx.class})
    public void save(User user){
        user.save();
    }
    @Before({Tx.class})
    public void update(User user){
        user.update();
    }
    @Before({Tx.class})
    public void logicDel(Integer id,Integer opId){
        User user=findOne(id);

        if(opId!=null){
            user.setOpId(opId);
        }

        user.apDel();

    }
    @Before({Tx.class})
    public void del(Integer id){
        User user=findOne(id);
        user.delete();
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

