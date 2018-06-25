package com.sc.ap.gen;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.XML;
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
import com.sc.ap.gen.directive.ColLabelResolve;
import com.sc.ap.gen.directive.ColNoteResolve;
import com.sc.ap.kits.AppKit;
import com.sc.ap.kits.DateKit;
import com.sc.ap.model.GenCfgCol;
import com.sc.ap.model.GenCfgTbl;
import com.sc.ap.model.GenSource;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
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
    protected String queryModeltemplate = "/com/sc/ap/gen/tmpl/java/queryModel.txt";

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
        this.engine.addDirective("genColNoteData", ColNoteResolve.class);
        this.engine.addDirective("genColLabel", ColLabelResolve.class);
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
    public List<String> genCodeFile(String action,String jsName,Map<String,Object> data){
        initEngine();
        List<String> ret=new ArrayList<>();
        switch (action){
            case TYPE_ALL:
                ret.add(genJavaCode(data));
                ret.add(genVuejsCode(data,jsName));
                break;
            case TYPE_JAVA:
                ret.add(genJavaCode(data));
                break;
            case TYPE_JS:
                ret.add(genVuejsCode(data,jsName));
                break;
        }
        return ret;
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
    private String genJavaCode(Map<String,Object> data){
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
        String query_java=className+"Query"+JAVA_FILE_SUFFIX;
        String model_txt=className+TXT_FILE_SUFFIX;
        String baseModel_txt="Base"+className+TXT_FILE_SUFFIX;
        String service_txt=className+"Service"+TXT_FILE_SUFFIX;
        String controller_txt=className+"Controller"+TXT_FILE_SUFFIX;
        String validator_txt=className+"Validator"+TXT_FILE_SUFFIX;
        String query_txt=className+"Query"+TXT_FILE_SUFFIX;
        File file= FileUtil.file(outDir);
        if(!file.exists())file.mkdirs();
        File[] files=file.listFiles();
        //删除之前生成的Java文件
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }

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
        engine.getTemplate(queryModeltemplate).render(data,new File(outDir+"/"+query_java));
            engine.getTemplate(queryModeltemplate).render(data,new File(outDir+"/"+query_txt));
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


        return outDir.substring(outDir.indexOf(AppKit.getGenPath()),outDir.length())+"javaCode.zip";
    }

    private String genVuejsCode(Map<String,Object> data,String jsName){
        String vueMaintemplate = "/com/sc/ap/gen/tmpl/vuejs/"+jsName+"/main.txt";
        String vueStoretemplate = "/com/sc/ap/gen/tmpl/vuejs/"+jsName+"/store.txt";
        LogKit.info("开始生成Vuejs代码");
        GenCfgTbl genCfgTbl=(GenCfgTbl)data.get("tbl");
        String modelName=genCfgTbl.getModelName();
        String outDir=(String)data.get("outDir")+"/js/"+modelName+"/";
        String main=modelName+VUE_FILE_SUFFIX;
        String store=modelName+JS_FILE_SUFFIX;
        String main_txt=modelName+TXT_FILE_SUFFIX;
        String store_txt=modelName+TXT_FILE_SUFFIX;
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
        return outDir.substring(outDir.indexOf(AppKit.getGenPath()),outDir.length())+"vuejsCode.zip";
    }

    public String genProject(Map<String,String> param){
        initEngine();
        String templateName=param.get("templateName");
        String templateRoot="/com/sc/ap/gen/tmpl/"+templateName+"/";
        //判断项目模板目录是否存在
        if(!FileUtil.exist(PathKit.getRootClassPath()+"/"+templateRoot))throw new CoreException("模板目录不存在");
        //创建导出的目录
        String now=DateKit.dateToStr(new Date(),DateKit.yyyyMMdd);
        String zipName=templateName+"/"+now +"/"+param.get("projectName")+".zip";
        String outDirPath=GEN_CODE_OUT_PATH+templateName+"/"+now +"/";
        File outDir=null;
        if(!FileUtil.exist(outDirPath)){outDir=FileUtil.file(outDirPath);outDir.mkdirs();}
        else FileUtil.del(new File(outDirPath));
        LogKit.info("创建导出的目录成功");
        
        //创建项目文件夹
        String projectPath=outDirPath+param.get("projectName")+"/";
        File project=null;
        if(!FileUtil.exist(projectPath)){project=FileUtil.file(projectPath);project.mkdirs();}
        LogKit.info("创建项目文件夹成功");
        //创建web目录
        String p=createWebDir(projectPath,param,templateRoot+"base/pom.txt");
        LogKit.info("创建web文件夹成功");
        //创建src目录
        p=createSrcDir(p,param,templateRoot+"base/web/pom.txt");
        LogKit.info("创建src文件夹成功");
        //创建main目录
        String mainPath=p+"main/";
        File mainFile=null;
        if(!FileUtil.exist(mainPath))mainFile=FileUtil.file(mainPath);
        LogKit.info("创建main文件夹成功");
        //创建java目录
        String javaPath=mainPath+"java/";
        File javaFile=null;
        if(!FileUtil.exist(javaPath))javaFile=FileUtil.file(javaPath);
        LogKit.info("创建java文件夹成功");
        //创建group，artifact 包文件夹
        String groupId=param.get("groupId");
        String artifactId=param.get("artifactId");
        if(!groupId.contains(StrUtil.DOT))throw new CoreException("groupId 格式不正确，格式应为：xxx.xxxx");
        String[] strings=groupId.split("\\.");
        if(strings.length!=2)throw new CoreException("groupId 格式不正确，格式应为:xxx.xxxx");
        String groupFirstPath=javaPath+strings[0]+"/";
        File groupFirstFile=null;
        if(!FileUtil.exist(groupFirstPath))groupFirstFile=FileUtil.file(groupFirstPath);groupFirstFile.mkdirs();
        String groupSecondPath=groupFirstPath+strings[1]+"/";
        File groupSecondFile=null;
        if(!FileUtil.exist(groupSecondPath))groupSecondFile=FileUtil.file(groupSecondPath);groupSecondFile.mkdirs();
        String artifactPath=groupSecondPath+artifactId+"/";
        File artifactFile=null;
        if(!FileUtil.exist(artifactPath))artifactFile=FileUtil.file(artifactPath);artifactFile.mkdirs();
        LogKit.info("创建java根文件夹成功");
        //创建controller
        String packagePath=templateRoot+"base/web/src/main/java/com/sc/ap/";
        createController(artifactPath,param,packagePath+"controller/");
        LogKit.info("创建controller文件夹成功");
        //创建service
        createService(artifactPath,param,packagePath+"service/");
        LogKit.info("创建service文件夹成功");
        //创建validator
        createValidator(artifactPath,param,packagePath+"validator/");
        LogKit.info("创建validator文件夹成功");
        //创建 interceptors
        createInterceptors(artifactPath,param,packagePath+"interceptors/");
        LogKit.info("创建interceptors文件夹成功");
        //创建Core
        createCore(artifactPath,param,packagePath+"core/");
        LogKit.info("创建core文件夹成功");
        //创建query
        createQuery(artifactPath,param,packagePath+"query/");
        LogKit.info("创建query文件夹成功");
        //创建kits
        createKits(artifactPath,param,packagePath+"kits/");
        LogKit.info("创建kits文件夹成功");
        //创建model
        createModel(artifactPath,param,packagePath+"model/");
        LogKit.info("创建model文件夹成功");
        //创建根目录下的java文件
        File[] files=FileUtil.ls(PathKit.getRootClassPath()+packagePath);
        File file=null;
        for (int i = 0; i < files.length; i++) {
            file=files[i];
            if(file.isFile()){
                String fileName=file.getName();
                fileName=fileName.substring(0,fileName.indexOf("."));
                engine.getTemplate(packagePath+file.getName()).render(param,new File(artifactPath+fileName+JAVA_FILE_SUFFIX));
            }
        }
        LogKit.info("创建java根目录下其他文件成功");
       

        try {
            //创建res内容
            createRes(mainPath,param,templateRoot+"base/web/src/main/res/");
            //创建webapp目录
            createWebapp(mainPath,param,templateRoot+"base/web/src/main/webapp/WEB-INF/");
        } catch (IOException e) {
            throw new CoreException("生成项目失败:"+e.getMessage());
        }
        ZipUtil.zip(projectPath,outDirPath+param.get("projectName")+".zip",true);
        return zipName;
    }

    private void createWebapp(String pPath,Map<String,String> param,String tPath) throws IOException {
        //创建webapp文件夹
        String webappPath=pPath+"webapp/WEB-INF/";
        File webappFile=null;
        if(!FileUtil.exist(webappPath))webappFile=FileUtil.file(webappPath);
        webappFile.mkdirs();
        LogKit.info("创建webapp文件夹成功");
        File file=FileUtil.file(PathKit.getRootClassPath()+tPath);
        File[] files=file.listFiles();
        File tmp=null;
        if(files!=null){
            for (int i = 0; i < files.length; i++) {
                tmp = files[i];
                String fileName = tmp.getName();
                File targetDir=null;
                if(tmp.isDirectory()){
                    targetDir=new File(webappPath+tmp.getName()+"/");
                    targetDir.mkdirs();
                    FileUtil.copyContent(tmp,targetDir,true);
                }else{
                    engine.getTemplate(tPath+fileName).render(param,webappPath+fileName.substring(0,fileName.indexOf("."))+ XML_FILE_SUFFIX);
                }
            }
        }
        LogKit.info("创建webapp文件夹内容成功");
    }

    
    private void createRes(String pPath,Map<String,String> param,String tPath) throws IOException {
        //创建res文件夹
        String resPath=pPath+"res/";
        File resFile=null;
        if(!FileUtil.exist(resPath))resFile=FileUtil.file(resPath);
        resFile.mkdirs();
        LogKit.info("创建res文件夹成功");
        
        File file=FileUtil.file(PathKit.getRootClassPath()+tPath);
        File[] files=file.listFiles();
        File tmp=null;
        if(files!=null){
            for (int i = 0; i < files.length; i++) {
                tmp=files[i];
                String fileName=tmp.getName();
                if(tmp.isDirectory()){
                    File[] files1=tmp.listFiles();
                    String fileName1=null;
                    String s=null;
                    for (int j = 0; j < files1.length; j++) {
                        fileName1=files1[j].getName();
                        s=fileName1.substring(0,fileName1.indexOf("."));
                        FileUtil.file(resPath+fileName+"/").mkdirs();
                        if(fileName1.contains(".txt")){
                            engine.getTemplate(tPath+fileName+"/"+fileName1).render(param,resPath+fileName+"/"+s+PROPERTIES_FILE_SUFFIX);
                        }else{
                            FileUtil.copy(files1[j].getCanonicalPath(),resPath+fileName+"/"+fileName1,true);
                        }
                    }
                }else{
                    if(fileName.contains(PROPERTIES_FILE_SUFFIX)){
                        FileUtil.copy(tmp.getCanonicalPath(),resPath+"/"+fileName,true);
                    }else{

                        if(fileName.contains("ehcache")){
                            engine.getTemplate(tPath+fileName).render(param,resPath+fileName.substring(0,fileName.indexOf("."))+XML_FILE_SUFFIX);
                        }
                        else{
                            engine.getTemplate(tPath+fileName).render(param,resPath+fileName.substring(0,fileName.indexOf("."))+PROPERTIES_FILE_SUFFIX);
                        }

                    }
                }
            }
        }
        LogKit.info("创建res文件夹内容成功");
    }
    
    private String createWebDir(String pPath,Map<String,String> param,String tPath){
        String webPath=pPath+"web/";
        File web=null;
        if(!FileUtil.exist(webPath))
            web=FileUtil.file(webPath);
        web.mkdirs();
        engine.getTemplate(tPath).render(param,new File(pPath+"pom.xml"));
        return webPath;
    }
    private String createSrcDir(String pPath,Map<String,String> param,String tPath){
        String srcPath=pPath+"src/";
        File src=null;
        if(!FileUtil.exist(srcPath))
            src=FileUtil.file(srcPath);
        src.mkdirs();
        engine.getTemplate(tPath).render(param,new File(pPath+"pom.xml"));
        return srcPath;
    }

    private void createKits(String pPath,Map<String,String> param,String tPath){
        String kitsPath=pPath+"kits/";
        File kitsFile=null;
        if(!FileUtil.exist(kitsPath))
            kitsFile=FileUtil.file(kitsPath);
        kitsFile.mkdirs();
        File[] templateFiles=FileUtil.ls(PathKit.getRootClassPath()+tPath);
        File[] files=null;
        File tmp=null;
        String outFileName=null;
        if(templateFiles!=null){
            for (int i = 0; i < templateFiles.length; i++) {
                tmp=templateFiles[i];
                FileUtil.file(kitsPath+tmp.getName()+"/").mkdirs();
                if(tmp.isDirectory()){
                    files=tmp.listFiles();
                    if(files!=null){
                        for (int j = 0; j < files.length; j++) {
                            outFileName=files[j].getName();
                            outFileName=outFileName.substring(0,outFileName.indexOf("."));
                            engine.getTemplate(tPath+tmp.getName()+"/"+files[j].getName()).render(param,new File(kitsPath+tmp.getName()+"/"+outFileName+JAVA_FILE_SUFFIX));
                        }
                    }
                }else{
                    tmp=templateFiles[i];
                    outFileName=tmp.getName();
                    outFileName=outFileName.substring(0,outFileName.indexOf("."));
                    engine.getTemplate(tPath+tmp.getName()).render(param,new File(kitsPath+outFileName+"JAVA_FILE_SUFFIX"));
                }
            }
        }
    }

    private void createModel(String pPath,Map<String,String> param,String tPath){
        String modelPath=pPath+"model/";
        File modelFile=null;
        if(!FileUtil.exist(modelPath))modelFile=FileUtil.file(modelPath);modelFile.mkdirs();
        File[] templateFiles=FileUtil.ls(PathKit.getRootClassPath()+tPath);
        File[] files=null;
        File tmp=null;
        String outFileName=null;
        if(templateFiles!=null){
            for (int i = 0; i < templateFiles.length; i++) {
                tmp=templateFiles[i];
                FileUtil.file(modelPath+tmp.getName()+"/").mkdirs();
                if(tmp.isDirectory()){
                    files=tmp.listFiles();
                    if(files!=null){
                        for (int j = 0; j < files.length; j++) {
                            outFileName=files[j].getName();
                            outFileName=outFileName.substring(0,outFileName.indexOf("."));
                            engine.getTemplate(tPath+tmp.getName()+"/"+files[j].getName()).render(param,new File(modelPath+tmp.getName()+"/"+outFileName+JAVA_FILE_SUFFIX));
                        }
                    }
                }else{
                    tmp=templateFiles[i];
                    outFileName=tmp.getName();
                    outFileName=outFileName.substring(0,outFileName.indexOf("."));
                    engine.getTemplate(tPath+tmp.getName()).render(param,new File(modelPath+outFileName+"JAVA_FILE_SUFFIX"));
                }
            }
        }
    }
    
    private void createController(String pPath,Map<String,String> param,String tPath){
        String controllerPath=pPath+"controller/";
        File controllerFile=null;
        if(!FileUtil.exist(controllerPath))controllerFile=FileUtil.file(controllerPath);controllerFile.mkdirs();
        File[] templateFiles=FileUtil.ls(PathKit.getRootClassPath()+tPath);
        File[] files=null;
        File tmp=null;
        if(templateFiles!=null){
            for (int i = 0; i < templateFiles.length; i++) {
                tmp=templateFiles[i];
                FileUtil.file(controllerPath+tmp.getName()+"/").mkdirs();
                if(tmp.isDirectory()){
                    files=tmp.listFiles();
                    if(files!=null){
                        String outFileName=null;
                        for (int j = 0; j < files.length; j++) {
                            outFileName=files[j].getName();
                            outFileName=outFileName.substring(0,outFileName.indexOf("."));
                            engine.getTemplate(tPath+tmp.getName()+"/"+files[j].getName()).render(param,new File(controllerPath+tmp.getName()+"/"+outFileName+JAVA_FILE_SUFFIX));
                        }
                    }
                }
            }
        }
    }
    private void createService(String pPath,Map<String,String> param,String tPath){
        String servicePath=pPath+"service/";
        File serviceFile=null;
        if(!FileUtil.exist(servicePath))serviceFile=FileUtil.file(servicePath);serviceFile.mkdirs();
        File[] templateFiles=FileUtil.ls(PathKit.getRootClassPath()+tPath);
        File[] files=null;
        File tmp=null;
        if(templateFiles!=null){
            for (int i = 0; i < templateFiles.length; i++) {
                tmp=templateFiles[i];
                FileUtil.file(servicePath+tmp.getName()+"/").mkdirs();
                if(tmp.isDirectory()){
                    files=tmp.listFiles();
                    if(files!=null){
                        String outFileName=null;
                        for (int j = 0; j < files.length; j++) {
                            outFileName=files[j].getName();
                            outFileName=outFileName.substring(0,outFileName.indexOf("."));
                            engine.getTemplate(tPath+tmp.getName()+"/"+files[j].getName()).render(param,new File(servicePath+tmp.getName()+"/"+outFileName+JAVA_FILE_SUFFIX));
                        }
                    }
                }
            }
        }
    }
    private void createValidator(String pPath,Map<String,String> param,String tPath){
        String validatorPath=pPath+"validator/";
        File validatorFile=null;
        if(!FileUtil.exist(validatorPath))validatorFile=FileUtil.file(validatorPath);validatorFile.mkdirs();
        File[] templateFiles=FileUtil.ls(PathKit.getRootClassPath()+tPath);
        File[] files=null;
        File tmp=null;
        if(templateFiles!=null){
            for (int i = 0; i < templateFiles.length; i++) {
                tmp=templateFiles[i];
                FileUtil.file(validatorPath+tmp.getName()+"/").mkdirs();
                if(tmp.isDirectory()){
                    files=tmp.listFiles();
                    if(files!=null){
                        String outFileName=null;
                        for (int j = 0; j < files.length; j++) {
                            outFileName=files[j].getName();
                            outFileName=outFileName.substring(0,outFileName.indexOf("."));
                            engine.getTemplate(tPath+tmp.getName()+"/"+files[j].getName()).render(param,new File(validatorPath+tmp.getName()+"/"+outFileName+JAVA_FILE_SUFFIX));
                        }
                    }
                }
            }
        }
    }
    private void createCore(String pPath,Map<String,String> param,String tPath){
        String corePath=pPath+"core/";
        File coreFile=null;
        if(!FileUtil.exist(corePath))coreFile=FileUtil.file(corePath);coreFile.mkdirs();
        File[] templateFiles=FileUtil.ls(PathKit.getRootClassPath()+tPath);
        File[] files=null;
        File tmp=null;
        if(templateFiles!=null){
            for (int i = 0; i < templateFiles.length; i++) {
                tmp=templateFiles[i];
                String outFileName=tmp.getName();
                outFileName=outFileName.substring(0,outFileName.indexOf("."));
                engine.getTemplate(tPath+tmp.getName()).render(param,new File(corePath+outFileName+JAVA_FILE_SUFFIX));
            }
        }
    }
    private void createInterceptors(String pPath,Map<String,String> param,String tPath){
        String interceptorPath=pPath+"interceptors/";
        File interceptorFile=null;
        if(!FileUtil.exist(interceptorPath))interceptorFile=FileUtil.file(interceptorPath);interceptorFile.mkdirs();
        File[] templateFiles=FileUtil.ls(PathKit.getRootClassPath()+tPath);
        File[] files=null;
        File tmp=null;
        if(templateFiles!=null){
            for (int i = 0; i < templateFiles.length; i++) {
                tmp=templateFiles[i];
                String outFileName=tmp.getName();
                outFileName=outFileName.substring(0,outFileName.indexOf("."));
                engine.getTemplate(tPath+tmp.getName()).render(param,new File(interceptorPath+outFileName+JAVA_FILE_SUFFIX));
            }
        }
    }
    private void createQuery(String pPath,Map<String,String> param,String tPath){
        String queryPath=pPath+"query/";
        File queryFile=null;
        if(!FileUtil.exist(queryPath))queryFile=FileUtil.file(queryPath);queryFile.mkdirs();
        File[] templateFiles=FileUtil.ls(PathKit.getRootClassPath()+tPath);
        File[] files=null;
        File tmp=null;
        if(templateFiles!=null){
            for (int i = 0; i < templateFiles.length; i++) {
                tmp=templateFiles[i];
                String outFileName=tmp.getName();
                outFileName=outFileName.substring(0,outFileName.indexOf("."));
                engine.getTemplate(tPath+tmp.getName()).render(param,new File(queryPath+outFileName+JAVA_FILE_SUFFIX));
            }
        }
    }
    private void createTask(String pPath,Map<String,String> param,String tPath){
        String taskPath=pPath+"task/";
        File taskFile=null;
        if(!FileUtil.exist(taskPath))taskFile=FileUtil.file(taskPath);taskFile.mkdirs();
        File[] templateFiles=FileUtil.ls(PathKit.getRootClassPath()+tPath);
        File[] files=null;
        File tmp=null;
        if(templateFiles!=null){
            for (int i = 0; i < templateFiles.length; i++) {
                tmp=templateFiles[i];
                String outFileName=tmp.getName();
                outFileName=outFileName.substring(0,outFileName.indexOf("."));
                engine.getTemplate(tPath+tmp.getName()).render(param,new File(taskPath+outFileName+JAVA_FILE_SUFFIX));
            }
        }
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
