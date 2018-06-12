package com.sc.ap.gen.directive;

import cn.hutool.core.util.StrUtil;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.io.IOException;

/**
 * 简介
 * <p>
 * 项目名称:   [ap]
 * 包:        [com.sc.ap.gen.directive]
 * 类名称:    [ColLabelResolve]
 * 类描述:    []
 * 创建人:    [于海慧]
 * 创建时间:  [2018/6/12]
 * 修改人:    []
 * 修改时间:  []
 * 修改备注:  []
 * 版本:     [v1.0]
 */
public class ColLabelResolve extends Directive {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        Expr expr=exprList.getFirstExpr();
        Object object=expr.eval(scope);
        String note=(String)object;
        if(StrUtil.isBlank(note))return;
        String[] strings=null;
        strings=note.split("-");
        try {
            writer.write(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
