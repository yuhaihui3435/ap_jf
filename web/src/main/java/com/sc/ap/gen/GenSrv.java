package com.sc.ap.gen;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.kit.*;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.sc.ap.core.CoreException;
import com.sc.ap.kits.DateKit;
import com.sc.ap.model.GenCfgCol;
import com.sc.ap.model.GenCfgTbl;
import com.sc.ap.model.GenSource;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

import static com.sc.ap.gen.GenConsts.*;


public class GenSrv {

    protected MetaBuilder metaBuilder;
    protected Dialect dialect = null;
    private GenCfgTblSrv genCfgTblSrv = Duang.duang(GenCfgTblSrv.class.getSimpleName(), GenCfgTblSrv.class);
    private GenCfgColSrv genCfgColSrv = Duang.duang(GenCfgColSrv.class.getSimpleName(), GenCfgColSrv.class);
    protected Map<String, String> getterTypeMap;
    protected Engine engine;
    protected String baseModeltemplate = "/com/sc/ap/gen/tmpl/java/baseModel.txt";
    protected String modeltemplate = "/com/sc/ap/gen/tmpl/java/model.txt";
    protected String srvtemplate = "/com/sc/ap/gen/tmpl/java/srv.txt";
    protected String ctrtemplate = "/com/sc/ap/gen/tmpl/java/ctr.txt";
    protected String validatortemplate = "/com/sc/ap/gen/tmpl/java/validator.txt";
    protected String vueMaintemplate = "/com/sc/ap/gen/tmpl/vuejs/vuetify/main.txt";
    protected String vueRoutertemplate = "/com/sc/ap/gen/tmpl/vuejs/vuetify/router.txt";
    protected String vueStoretemplate = "/com/sc/ap/gen/tmpl/vuejs/vuetify/store.txt";
    public GenSrv() {
        this.getterTypeMap = new HashMap<String, String>() {
            {
                this.put("java.lang.String", "getStr");
                this.put("java.lang.Integer", "getInt");
                this.put("java.lang.Long", "getLong");
                this.put("java.lang.Double", "getDouble");
                this.put("java.lang.Float", "getFloat");
                this.put("java.lang.Short", "getShort");
                this.put("java.lang.Byte", "getByte");
            }
        };
    }
    public void initEngine(){
        this.engine = new Engine();
        this.engine.setToClassPathSourceFactory();
        this.engine.addSharedMethod(new StrKit());
        this.engine.addSharedObject("getterTypeMap", this.getterTypeMap);
        this.engine.addSharedObject("javaKeyword", JavaKeyword.me);
    }
    public GenSrv initDataSource(String url, String user, String pwd) {
        DruidPlugin druidPlugin = new DruidPlugin(url, user, pwd);
        druidPlugin.start();
        metaBuilder = new MetaBuilder(druidPlugin.getDataSource());
        return this;
    }
    public List<TableMeta> getTableMetas() {
        if (metaBuilder == null)
            throw new CoreException("MetaBuilder 获取失败");
        return metaBuilder.build();
    }
    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }
    @Before({Tx.class})
    public void saveCol(List<GenCfgCol> genCfgCols,Integer tblId) {
        GenCfgCol genCfgCol=null;
        GenCfgCol genCfgCol1=null;
        for (int i = 0; i < genCfgCols.size(); i++) {
            genCfgCol=genCfgCols.get(i);
            genCfgCol1=genCfgColSrv.findByTblIdAndCol(tblId,genCfgCol.getCol());
            if (genCfgCol1==null){
                genCfgCol.setTblId(tblId);
                genCfgCol.save();
            }else{
                genCfgCol1.setNote(genCfgCol.getNote());
                genCfgCol1.setCol(genCfgCol.getCol());
                genCfgCol1.setTpe(genCfgCol.getTpe());
                genCfgCol1.setOrgCol(genCfgCol.getOrgCol());
                genCfgCol1.update();
            }
        }
    }
    @Before({Tx.class})
    public void saveTbl(List<GenCfgTbl> genCfgTblList) {
        GenCfgTbl genCfgTbl, genCfgTbl1;
        for (int i = 0; i < genCfgTblList.size(); i++) {
            genCfgTbl = genCfgTblList.get(i);
            genCfgTbl1 = genCfgTblSrv.findByGsIdAndTbl(genCfgTbl.getGsId(), genCfgTbl.getTbl());
            if (genCfgTbl1 == null) {
                genCfgTbl.save();
                saveCol(genCfgTbl.getGenCfgColList(),genCfgTbl.getId());
            }else{
                genCfgTbl.setId(genCfgTbl1.getId());
                genCfgTbl.update();
                saveCol(genCfgTbl.getGenCfgColList(),genCfgTbl1.getId());
            }
        }
    }
    public Map<String,Object> getGenData(Integer tblId){
        LogKit.info("查询tbl配置元数据");
        GenCfgTbl genCfgTbl=GenCfgTbl.dao.findById(tblId);
        GenSource genSource=GenSource.dao.findById(genCfgTbl.getGsId());
        genCfgTbl.setModelName(GenKit.getModelName(genCfgTbl.getTbl(),genCfgTbl.getGsId()));
        Map<String,Object> ret=new HashMap();
        ret.put("basePackage",genSource.getBasePackage());
        ret.put("baseModelPackage",genSource.getBaseModelPackage());
        ret.put("modelPackage",genSource.getModelPackage());
        ret.put("servicePackage",genSource.getServicePakcage());
        ret.put("controllerPackage",genSource.getControllerPackage());
        ret.put("tbl",genCfgTbl);
        ret.put("outDir",buildOutPath(genSource,genCfgTbl));
        ret.put("javaKeywords", JavaKeyword.me);
        ret.put("getterTypeMap",getterTypeMap);
        LogKit.info("查询tbl配置源数据结束");
        return ret;
    }
    public void genCodeFile(String action,String jsName,Map<String,Object> data){
        initEngine();
        switch (action){
            case TYPE_ALL:
                genJavaCode(data);
                genVuejsCode(data,jsName);
                break;
            case TYPE_JAVA:
                genJavaCode(data);
                break;
            case TYPE_JS:
                genVuejsCode(data,jsName);
                break;
        }
    }
    public List<FileInfo> getGenJavaCode(Integer tblId){
        GenCfgTbl genCfgTbl=GenCfgTbl.dao.findById(tblId);
        GenSource genSource=GenSource.dao.findById(genCfgTbl.getGsId());
        String path=buildOutPath(genSource,genCfgTbl)+"/java/"+genCfgTbl.getModelName()+"/";
        File file=new File(path);
        List<FileInfo> ret=new ArrayList<>();
        if(file.exists()){
            File[] files=file.listFiles();
            for (File f:files){
                if(f.getName().contains(TXT_FILE_SUFFIX))
                    ret.add(new FileInfo(f.getName(),path+f.getName(),FileUtil.readString(path+f.getName(), Charset.defaultCharset())));
            }
        }
        return ret;
    }
    public List<FileInfo> getGenVuejsCode(Integer tblId){
        GenCfgTbl genCfgTbl=GenCfgTbl.dao.findById(tblId);
        GenSource genSource=GenSource.dao.findById(genCfgTbl.getGsId());
        String path=buildOutPath(genSource,genCfgTbl)+"/vuejs/"+genCfgTbl.getModelName()+"/";
        File file=new File(path);
        List<FileInfo> ret=new ArrayList<>();
        if(file.exists()){
            File[] files=file.listFiles();
            for (File f:files){
                if(f.getName().contains(TXT_FILE_SUFFIX))
                    ret.add(new FileInfo(f.getName(),path+f.getName(),FileUtil.readString(path+f.getName(), Charset.defaultCharset())));
            }
        }
        return ret;
    }
    private String buildOutPath(Integer tblId){
        GenCfgTbl genCfgTbl=GenCfgTbl.dao.findById(tblId);
        GenSource genSource=GenSource.dao.findById(genCfgTbl.getGsId());
        return GEN_CODE_OUT_PATH+genSource.getName()+ "-"+genCfgTbl.getTbl()+"/"+DateKit.dateToStr(new Date(),DateKit.yyyyMMdd);
    }
    private String buildOutPath(GenSource genSource,GenCfgTbl genCfgTbl){
        return GEN_CODE_OUT_PATH+genSource.getName()+ "-"+genCfgTbl.getTbl()+"/"+DateKit.dateToStr(new Date(),DateKit.yyyyMMdd);
    }
    private void genJavaCode(Map<String,Object> data){
        LogKit.info("开始生成Java代码");
        GenCfgTbl genCfgTbl=(GenCfgTbl)data.get("tbl");
        String modelName=genCfgTbl.getModelName();
        String className=genCfgTbl.getClassName();
        String outDir=(String)data.get("outDir")+"/java/"+modelName+"/";
        String model_java=className+JAVA_FILE_SUFFIX;
        String baseModel_java="Base"+className+JAVA_FILE_SUFFIX;
        String service_java=className+"Service"+JAVA_FILE_SUFFIX;
        String controller_java=className+"Controller"+JAVA_FILE_SUFFIX;
        String validator_java=className+"Validator"+JAVA_FILE_SUFFIX;
        String model_txt=className+TXT_FILE_SUFFIX;
        String baseModel_txt="Base"+className+TXT_FILE_SUFFIX;
        String service_txt=className+"Service"+TXT_FILE_SUFFIX;
        String controller_txt=className+"Controller"+TXT_FILE_SUFFIX;
        String validator_txt=className+"Validator"+TXT_FILE_SUFFIX;
        File file= FileUtil.file(outDir);
        if(!file.exists())file.mkdirs();
        File[] files=file.listFiles();
        //删除之前生成的Java文件
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        com.jfinal.template.Template template=engine.getTemplate(modeltemplate);
        engine.getTemplate(modeltemplate).render(data,new File(outDir+"/"+model_java));
        engine.getTemplate(modeltemplate).render(data,new File(outDir+"/"+model_txt));
        engine.getTemplate(baseModeltemplate).render(data,new File(outDir+"/"+baseModel_java));
        engine.getTemplate(baseModeltemplate).render(data,new File(outDir+"/"+baseModel_txt));
        engine.getTemplate(srvtemplate).render(data,new File(outDir+"/"+service_java));
        engine.getTemplate(srvtemplate).render(data,new File(outDir+"/"+service_txt));
        engine.getTemplate(ctrtemplate).render(data,new File(outDir+"/"+controller_java));
        engine.getTemplate(ctrtemplate).render(data,new File(outDir+"/"+controller_txt));
        engine.getTemplate(validatortemplate).render(data,new File(outDir+"/"+validator_java));
        engine.getTemplate(validatortemplate).render(data,new File(outDir+"/"+validator_txt));
        File zipFile=new File(outDir+"javaCode.zip");
        files=file.listFiles();
        List<File> javaFiles=new ArrayList<>();
        for (File file1:files){
            if(file1.getName().contains(JAVA_FILE_SUFFIX)){
                javaFiles.add(file1);
            }
        }
        ZipUtil.zip(zipFile,false,javaFiles.toArray(new File[javaFiles.size()]));
        LogKit.info("生成java代码完成");
    }

    private void genVuejsCode(Map<String,Object> data,String jsName){
        String vueMaintemplate = "/com/sc/ap/gen/tmpl/vuejs/"+jsName+"/main.txt";
        String vueStoretemplate = "/com/sc/ap/gen/tmpl/vuejs/"+jsName+"/store.txt";
        LogKit.info("开始生成Vuejs代码");
        GenCfgTbl genCfgTbl=(GenCfgTbl)data.get("tbl");
        String modelName=genCfgTbl.getModelName();
        String outDir=(String)data.get("outDir")+"/js/"+modelName+"/";
        String main="main"+VUE_FILE_SUFFIX;
        String store="store"+JS_FILE_SUFFIX;
        String main_txt="main"+TXT_FILE_SUFFIX;
        String store_txt="store"+TXT_FILE_SUFFIX;
        File file= FileUtil.file(outDir);
        if(!file.exists())file.mkdirs();
        File[] files=file.listFiles();
        //删除之前生成的JS文件
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        engine.getTemplate(vueMaintemplate).render(data,new File(outDir+"/"+main+""));
        engine.getTemplate(vueStoretemplate).render(data,new File(outDir+"/"+store+""));
        engine.getTemplate(vueMaintemplate).render(data,new File(outDir+"/"+main_txt+""));
        engine.getTemplate(vueStoretemplate).render(data,new File(outDir+"/"+store_txt+""));
        File zipFile=new File(outDir+"vuejsCode.zip");
        files=file.listFiles();
        List<File> javaFiles=new ArrayList<>();
        for (File file1:files){
            if(!file1.getName().contains(TXT_FILE_SUFFIX)){
                javaFiles.add(file1);
            }
        }
        ZipUtil.zip(zipFile,false,javaFiles.toArray(new File[javaFiles.size()]));
        LogKit.info("生成vuejs代码完成");
    }

    class FileInfo{
        public FileInfo(String name,String path,String soruce){
            this.name=name;
            this.path=path;
            this.source=soruce;
        }
        private String name;
        private String path;
        private String source;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
