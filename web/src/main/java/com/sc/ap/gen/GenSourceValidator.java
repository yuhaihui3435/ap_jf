package com.sc.ap.gen;

import cn.hutool.core.util.StrUtil;
import com.jfinal.core.Controller;
import com.sc.ap.Consts;
import com.sc.ap.core.CoreValidator;
import com.sc.ap.model.GenSource;

import java.util.List;

public class GenSourceValidator extends CoreValidator {
    @Override
    protected void validate(Controller controller) {
        GenSource genSource=controller.getModel(GenSource.class,"",true);
        String ak=getActionKey();
        List<GenSource> list=null;

        if(StrUtil.isBlank(genSource.getName())){
            addError(Consts.REQ_JSON_CODE.fail.name(), "数据源名字不能为空");
            return;
        }

        if(ak.contains("save")){
            if(StrUtil.isNotBlank(genSource.getName())) {
                list = genSource.dao.findByPropEQ("name", genSource.getName());
                if (!list.isEmpty()) {
                    addError(Consts.REQ_JSON_CODE.fail.name(), "数据源名重复");
                    return;
                }
            }
        }else if(ak.contains("update")){
                list = genSource.dao.findByPropEQAndIdNEQ("name",genSource.getName(),genSource.getId());
                if (!list.isEmpty()) {
                    addError(Consts.REQ_JSON_CODE.fail.name(), "数据源名重复");
                    return;
                }
        }
    }

    @Override
    protected void handleError(Controller controller) {
        controller.renderJson(getErrorJSON());
    }
}
