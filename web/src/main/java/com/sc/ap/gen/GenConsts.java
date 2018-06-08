package com.sc.ap.gen;

import com.jfinal.kit.PathKit;

public interface GenConsts {
    String TYPE_JAVA="java";
    String TYPE_JS="js";
    String TYPE_ALL="all";
    String JAVA_FILE_SUFFIX=".java";
    String TXT_FILE_SUFFIX=".txt";
    String VUE_FILE_SUFFIX=".vue";
    String JS_FILE_SUFFIX=".js";
    String GEN_CODE_OUT_PATH= PathKit.getWebRootPath()+"/WEB-INF/gen_tmp/";
}
