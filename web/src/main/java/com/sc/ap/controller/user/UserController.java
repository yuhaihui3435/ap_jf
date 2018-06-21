package com.sc.ap.controller.user;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Clear;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.upload.UploadFile;
import com.qiniu.common.QiniuException;
import com.sc.ap.Consts;
import com.sc.ap.interceptors.AdminAAuthInterceptor;
import com.sc.ap.kits.DateKit;
import com.sc.ap.kits.QiNiuKit;
import com.sc.ap.kits._StrKit;
import com.sc.ap.query.UserQuery;
import com.sc.ap.kits.ext.BCrypt;
import com.sc.ap.model.User;
import com.sc.ap.validator.user.UserValidator;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.aop.Before;
import com.sc.ap.core.CoreController;
import com.sc.ap.service.user.UserService;
import com.jfinal.aop.Duang;

import java.io.File;
import java.util.*;

@Clear(AdminAAuthInterceptor.class)
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
        UploadFile uf = getFile("file");
        File file = uf.getFile();
        String avatar=null;
        if(file!=null) {
            String savePath = getPara("sp");
            if (StrUtil.isBlank(savePath))
                savePath = "/cmn/pic/";

            String picServerUrl = CacheKit.get(Consts.CACHE_NAMES.paramCache.name(), "qn_url");
            String picName = DateKit.dateToStr(new Date(), DateKit.yyyyMMdd) + "/" + _StrKit.getUUID() + ".jpg";
            try {
                QiNiuKit.upload(file, savePath + picName);
            } catch (QiniuException e) {
                LogKit.error("七牛上传图片失败>>" + e.getMessage());
            }
            avatar=picServerUrl + savePath + picName;
        }
        User user=getApModel(User.class);
        user.setAvatar(avatar );
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
        renderJson(userService.findByIdInCache(id));
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

    public void getCurrUser(){
        renderJson(currUser());
    }
}