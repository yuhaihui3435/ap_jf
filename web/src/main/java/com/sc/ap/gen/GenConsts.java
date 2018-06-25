package com.sc.ap.gen;

import com.jfinal.kit.PathKit;
import com.sc.ap.kits.AppKit;

public interface GenConsts {
    String TYPE_JAVA="java";
    String TYPE_JS="js";
    String TYPE_ALL="all";
    String JAVA_FILE_SUFFIX=".java";
    String PROPERTIES_FILE_SUFFIX=".properties";
    String TXT_FILE_SUFFIX=".txt";
    String VUE_FILE_SUFFIX=".vue";
    String JS_FILE_SUFFIX=".js";
    String SQL_FILE_SUFFIX=".sql";
    String XML_FILE_SUFFIX=".xml";
    String GEN_CODE_OUT_PATH= PathKit.getWebRootPath()+ AppKit.getGenPath();
}
