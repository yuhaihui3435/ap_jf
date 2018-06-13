package com.sc.ap.controller.role;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Page;
import com.sc.ap.core.CoreController;
import com.sc.ap.model.Role;
import com.sc.ap.query.RoleQuery;
import com.sc.ap.service.role.RoleService;
import com.sc.ap.validator.role.RoleValidator;

import java.util.List;


public class RoleController extends CoreController {

    private RoleService roleService = Duang.duang(RoleService.class.getSimpleName(), RoleService.class);

    public void list() {
        RoleQuery roleQuery = (RoleQuery) getQueryModel(RoleQuery.class);
        List<Role> ret = roleService.findAll(roleQuery);
        renderJson(ret);
    }

    public void page() {
        RoleQuery roleQuery = (RoleQuery) getQueryModel(RoleQuery.class);
        Page<Role> ret = roleService.findPage(roleQuery);
        renderJson(ret);
    }

    @Before({RoleValidator.class})
    public void save() {
        Role role = getApModel(Role.class);
        if (currUser() != null) {
            role.setOpId(currUser().getId());
        }
        roleService.save(role);
        renderSuccessJSON("角色表新增成功");
    }

    @Before({RoleValidator.class})
    public void update() {
        Role role = getApModel(Role.class);
        if (currUser() != null) {
            role.setOpId(currUser().getId());
        }
        roleService.update(role);
        renderSuccessJSON("角色表修改成功");
    }

    //逻辑删除
    @Before({RoleValidator.class})
    public void logicDel() {
        Integer[] ids = getParaValuesToInt("ids");
        roleService.batchLogicDel(ids, currUser() == null ? null : currUser().getId());
        renderSuccessJSON("角色表删除成功");
    }

    //真实删除
    @Before({RoleValidator.class})
    public void del() {
        Integer[] ids = getParaValuesToInt("ids");
        roleService.batchDel(ids);
        renderSuccessJSON("角色表删除成功");
    }

    public void get() {
        Integer id = getParaToInt("id");
        renderJson(roleService.findOne(id));
    }
}