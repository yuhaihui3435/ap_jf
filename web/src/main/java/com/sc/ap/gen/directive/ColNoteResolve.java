package com.sc.ap.gen.directive;

import cn.hutool.core.util.StrUtil;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.io.IOException;

public class ColNoteResolve extends Directive {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        Expr expr=exprList.getFirstExpr();
        Object object=expr.eval(scope);
        String note=(String)object;
        if(StrUtil.isBlank(note))return;
        String[] strings=null;
        strings=note.split("-");
        StringBuilder ret=new StringBuilder();
        if(strings!=null&&strings.length==2){
            String data=strings[1];
            if(StrUtil.isNotBlank(data)) {
                strings = data.split(",");
                if(strings!=null){
                    for (int i = 0; i < strings.length; i++) {
                        data=strings[i];
                        String[] _strs=data.split(":");
                        if(_strs!=null&&_strs.length==2){
                            if(ret.length()==0){
                                ret.append("{").append("text:").append("\""+_strs[1]+"\"").append(",value:").append("\""+_strs[0]+"\"").append("}");
                            }else{
                                ret.append(",").append("{").append("text:").append("\""+_strs[1]+"\"").append(",value:").append("\""+_strs[0]+"\"").append("}");
                            }
                        }
                    }
                }
            }
        }

        if(ret.length()>0){
            try {
                writer.write(ret.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
