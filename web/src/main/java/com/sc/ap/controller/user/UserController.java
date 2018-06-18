package com.sc.ap.controller.user;
import cn.hutool.core.util.StrUtil;
import com.sc.ap.Consts;
import com.sc.ap.query.UserQuery;
import com.sc.ap.kits.ext.BCrypt;
import com.sc.ap.model.User;
import com.sc.ap.validator.user.UserValidator;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.aop.Before;
import com.sc.ap.core.CoreController;
import com.sc.ap.service.user.UserService;
import com.jfinal.aop.Duang;
import java.util.*;


public class UserController extends CoreController{

    private UserService userService=Duang.duang(UserService.class.getSimpleName(),UserService.class);

    public void list(){
        UserQuery userQuery=(UserQuery)getQueryModel(UserQuery.class);
        List<User> ret=userService.findAll(userQuery);
        renderJson(ret);
    }

    public void page(){
        UserQuery userQuery=(UserQuery)getQueryModel(UserQuery.class);
        Page<User> ret=userService.findPage(userQuery);
        renderJson(ret);
    }
    @Before({UserValidator.class})
    public void save(){
        User user=getApModel(User.class);
        if(currUser()!=null){user.setOpId(currUser().getId());}
        user.setStatus(Consts.STATUS.enable.getVal());
        String orgPwd=StrUtil.sub(user.getPhone(),user.getPhone().length()-6,user.getPhone().length());
        user.setSalt(BCrypt.gensalt());
        user.setPassword(BCrypt.hashpw(orgPwd, user.getSalt()));
        userService.save(user);
        renderSuccessJSON("用户信息表新增成功");
    }
    @Before({UserValidator.class})
    public void update(){
        User user=getApModel(User.class);
        if(currUser()!=null){user.setOpId(currUser().getId());}
        userService.update(user);
        renderSuccessJSON("用户信息表修改成功");
    }

    //逻辑删除
    @Before({UserValidator.class})
    public void logicDel(){
        Integer[] ids=getParaValuesToInt("ids");
        userService.batchLogicDel(ids,currUser()==null?null:currUser().getId());
        renderSuccessJSON("用户信息表删除成功");
    }

    //真实删除
    @Before({UserValidator.class})
    public void del(){
        Integer[] ids=getParaValuesToInt("ids");
        userService.batchDel(ids);
        renderSuccessJSON("用户信息表删除成功");
    }
    public void get(){
        Integer id=getParaToInt("id");
        renderJson(userService.findOne(id));
    }

    /**
     * 设置角色
     */
    public void saveUserRoles(){
        String loginname=getPara("loginname");
        String userRoleCodes=getPara("userRoleCodes");
        String[] userRoleCodesArray=null;
        if(StrUtil.isNotBlank(userRoleCodes))userRoleCodesArray=userRoleCodes.split(",");
        userService.saveUserRoles(loginname,userRoleCodesArray);
        renderSuccessJSON("设置角色成功");
    }
}