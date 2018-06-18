package com.sc.ap.controller.ser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sc.ap.model.Ser;
import com.sc.ap.query.SerQuery;
import com.sc.ap.validator.ser.SerValidator;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.aop.Before;
import com.sc.ap.core.CoreController;
import com.sc.ap.service.ser.SerService;
import com.jfinal.aop.Duang;
import java.util.*;


public class SerController extends CoreController{

    private SerService serService=Duang.duang(SerService.class.getSimpleName(),SerService.class);

    public void list(){
        SerQuery serQuery=(SerQuery)getQueryModel(SerQuery.class);
        List<Ser> ret=serService.findAll(serQuery);
        renderJson(ret);
    }

    public void page(){
        SerQuery serQuery=(SerQuery)getQueryModel(SerQuery.class);
        Page<Ser> ret=serService.findPage(serQuery);
        renderJson(ret);
    }
    @Before({SerValidator.class})
    public void save(){
        Ser ser=getApModel(Ser.class);
                if(currUser()!=null){ser.setOpId(currUser().getId());}
        serService.save(ser);
        renderSuccessJSON("服务新增成功");
    }
    @Before({SerValidator.class})
    public void update(){
        Ser ser=getApModel(Ser.class);
                        if(currUser()!=null){ser.setOpId(currUser().getId());}
        serService.update(ser);
        renderSuccessJSON("服务修改成功");
    }

    //逻辑删除
    @Before({SerValidator.class})
    public void logicDel(){
        Integer[] ids=getParaValuesToInt("ids");
        serService.batchLogicDel(ids,currUser()==null?null:currUser().getId());
        renderSuccessJSON("服务删除成功");
    }

    //真实删除
    @Before({SerValidator.class})
    public void del(){
        Integer[] ids=getParaValuesToInt("ids");
        serService.batchDel(ids);
        renderSuccessJSON("服务删除成功");
    }
    public void get(){
        Integer id=getParaToInt("id");
        renderJson(serService.findOne(id));
    }

    public void getTreeJson(){
        Integer pId=getParaToInt("pId",0);
        SerQuery serQuery=new SerQuery();
        serQuery.setpId(pId);
        List<Ser> resList=serService.findAll(serQuery);
        renderJson(JSON.toJSONString(resList, SerializerFeature.DisableCircularReferenceDetect));
    }
}