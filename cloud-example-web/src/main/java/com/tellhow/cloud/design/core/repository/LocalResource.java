//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tellhow.cloud.design.core.repository;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tellhow.cloud.design.core.entity.DevComposeDesignConfig;
import com.tellhow.cloud.design.core.enums.ResourceTypeEnum;
import com.tellhow.cloud.design.core.vo.FormFieldConfigVO;
import com.tellhow.cloud.design.core.vo.FormModelVO;
import com.tellhow.cloud.design.core.vo.ModelPathFieldVO;
import com.tellhow.cloud.design.core.vo.TabsModelVO;
import com.tellhow.cloud.design.core.vo.ViewModelVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

@Component
public class LocalResource {
    private static final Logger log = LoggerFactory.getLogger(LocalResource.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalResource.class);
    private Map<String, FormModelVO> formMap = new ConcurrentHashMap();
    private Map<String, ModelPathFieldVO> formModelPathFieldMap = new ConcurrentHashMap();
    private Map<String, ViewModelVO> viewMap = new ConcurrentHashMap();
    private Map<String, TabsModelVO> tabsMap = new ConcurrentHashMap();
    private Map<String, DevComposeDesignConfig> composeDesignMap = new ConcurrentHashMap();
    private static final String SUBTABLE = "subTable";
    private String jsonBasePath = "";

    public LocalResource() {
    }

    public FormModelVO getOnlineFormConfigVO(String code) {
        return (FormModelVO)this.formMap.get(code);
    }

    public ViewModelVO getOnlineViewConfigVO(String code) {
        return (ViewModelVO)this.viewMap.get(code);
    }

    public TabsModelVO getOnlineTabsConfigVO(String code) {
        return (TabsModelVO)this.tabsMap.get(code);
    }

    public DevComposeDesignConfig getOnlineComposeDesign(String code) {
        return (DevComposeDesignConfig)this.composeDesignMap.get(code);
    }

    @PostConstruct
    public void getHomePath() {
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        if (applicationHome.getSource() == null) {
            this.jsonBasePath = ".";
        } else {
            this.jsonBasePath = applicationHome.getSource().getParentFile().toString();
        }
        if (log.isDebugEnabled()) {
            log.debug("获取本地的Json文件路径为：" + this.jsonBasePath);
        }

    }

    public void cleanFormView(String viewCode, String formCode) {
        if (StrUtil.isNotEmpty(viewCode) && this.viewMap.containsKey(viewCode)) {
            this.viewMap.remove(viewCode);
        }

        if (StrUtil.isNotEmpty(formCode) && this.formMap.containsKey(formCode)) {
            this.formMap.remove(formCode);
        }

        if (StrUtil.isEmpty(viewCode) && StrUtil.isEmpty(formCode)) {
            this.viewMap.clear();
            this.formMap.clear();
            this.tabsMap.clear();
            this.composeDesignMap.clear();
        }

        this.formModelPathFieldMap = new ConcurrentHashMap();
        this.initLocalFormResource();
    }

    public boolean isLoadForm(String appCode, String moduleCode, String formCode) {
        if (null == this.formMap.get(formCode)) {
            String name = "runtime" + File.separator + appCode + File.separator + moduleCode + File.separator + ResourceTypeEnum.FORM.getType() + File.separator + formCode + ".form.json";
            String formModel = this.getOutFileContent(name);
            if (StringUtils.isEmpty(formModel)) {
                return false;
            }

            FormModelVO formConfigVO = (FormModelVO)JSON.parseObject(formModel, FormModelVO.class);
            this.formMap.put(formCode, formConfigVO);
            List<FormFieldConfigVO> fields = formConfigVO.getFields();
            List<Map> maps = (List)fields.stream().filter((n) -> {
                return n.getControlType().equalsIgnoreCase("subTable");
            }).map((n) -> {
                return n.getExtendJson();
            }).collect(Collectors.toList());
            Iterator var9 = maps.iterator();

            while(var9.hasNext()) {
                Map map = (Map)var9.next();
                this.isLoadForm(appCode, moduleCode, (String)map.get("formCode"));
            }
        }

        return true;
    }

    public boolean updateLoadForm(String path, String appCode, String moduleCode, String formCode, List<Map> anchorDataList) throws IOException {
        try {
            this.isLoadForm(appCode, moduleCode, formCode);
            FormModelVO formModelVO = this.getOnlineFormConfigVO(formCode);
            Assert.notNull(formModelVO, "表单不存在不允许更新");
            String configJson = formModelVO.getFormConfig().getConfigJson();
            JSONObject configJsonObject = JSONObject.parseObject(configJson);
            configJsonObject.put("anchorData", JSON.toJSONString(anchorDataList));
            formModelVO.getFormConfig().setConfigJson(configJsonObject.toJSONString());
            String name = "runtime" + File.separator + appCode + File.separator + moduleCode + File.separator + ResourceTypeEnum.FORM.getType() + File.separator + formCode + ".form.json";
            File formFile = new File(path + File.separator + name);
            if (formFile.exists()) {
                FileOutputStream fos = new FileOutputStream(formFile);

                try {
                    IoUtil.write(fos, StandardCharsets.UTF_8, false, new Object[]{JSON.toJSONString(formModelVO, new SerializerFeature[]{SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat})});
                    this.formMap.put(formCode, formModelVO);
                } finally {
                    if (Collections.singletonList(fos).get(0) != null) {
                        fos.close();
                    }

                }
            }

            return true;
        } catch (Throwable var16) {
            throw var16;
        }
    }

    public boolean isLoadView(String appCode, String moduleCode, String viewCode) {
        if (null == this.viewMap.get(viewCode)) {
            String name = "runtime" + File.separator + appCode + File.separator + moduleCode + File.separator + ResourceTypeEnum.VIEW.getType() + File.separator + viewCode + ".view.json";
            String viewModel = this.getOutFileContent(name);
            if (StringUtils.isEmpty(viewModel)) {
                return false;
            }

            ViewModelVO viewModelVO = (ViewModelVO)JSON.parseObject(viewModel, ViewModelVO.class);
            this.viewMap.put(viewCode, viewModelVO);
        }

        return true;
    }

    public boolean isLoadTabs(String appCode, String moduleCode, String tabsCode) {
        if (null == this.tabsMap.get(tabsCode)) {
            String name = "runtime" + File.separator + appCode + File.separator + moduleCode + File.separator + ResourceTypeEnum.TABS.getType() + File.separator + tabsCode + ".tabs.json";
            String tabsModel = this.getOutFileContent(name);
            if (StringUtils.isEmpty(tabsModel)) {
                return false;
            }

            TabsModelVO tabsModelVO = (TabsModelVO)JSON.parseObject(tabsModel, TabsModelVO.class);
            this.tabsMap.put(tabsCode, tabsModelVO);
        }

        return true;
    }

    public boolean isLoadComposeDesign(String appCode, String moduleCode, String composeCode) {
        if (null == this.composeDesignMap.get(composeCode)) {
            String name = "runtime" + File.separator + appCode + File.separator + moduleCode + File.separator + ResourceTypeEnum.COMPOSE.getType() + File.separator + composeCode + ".compose.json";
            String composeModel = this.getOutFileContent(name);
            if (StringUtils.isEmpty(composeModel)) {
                return false;
            }

            DevComposeDesignConfig designConfig = (DevComposeDesignConfig)JSON.parseObject(composeModel, DevComposeDesignConfig.class);
            this.composeDesignMap.put(composeCode, designConfig);
        }

        return true;
    }

    private String getFileContent(String filePath) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            byte[] data = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            String input = new String(data, StandardCharsets.UTF_8);
            return input;
        } catch (IOException var5) {
            LOGGER.error("classpath 加载设计模型失败,加载路径[{}]:", filePath);
            return null;
        }
    }

    public String getOutFileContent(String filePath) {
        InputStream inputStream = null;
        BufferedReader br = null;

        String var5;
        try {
            String fileDir = this.jsonBasePath + File.separator + filePath;
            if (log.isDebugEnabled()) {
                log.debug("读取本地json:filePath: {}", fileDir);
            }

            inputStream = new FileInputStream(fileDir);
            StringBuffer sb = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            String var7 = sb.toString();
            return var7;
        } catch (IOException var11) {
            var5 = this.getFileContent(filePath);
        } finally {
            IoUtil.close(br);
            IoUtil.close(inputStream);
        }

        return var5;
    }

    @PostConstruct
    public void initLocalFormResource() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] resources;
        int i;
        try {
            String resourcePath = "classpath*:/runtime/*/*/" + ResourceTypeEnum.FORM.getType() + "/*" + ".form.json";
            resources = resolver.getResources(resourcePath);
            if (log.isDebugEnabled()) {
                log.debug("加载jar包中所有的表单页面路径，加载路径为: [{}], 加载的个数为：[{}]个", resourcePath, resources.length);
            }

            for(i = 0; i < resources.length; ++i) {
                this.addToFormModelPathFieldMap(resources[i], "/");
            }
        } catch (IOException var6) {
            LOGGER.error("加载本地资源失败,jar包目录下没有资源", var6);
        }

        FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();
        resolver = new PathMatchingResourcePatternResolver(fileSystemResourceLoader);

        try {
            this.getHomePath();
            resources = resolver.getResources("file:" + this.jsonBasePath + File.separator + "runtime" + File.separator + "*" + File.separator + "*" + File.separator + ResourceTypeEnum.FORM.getType() + File.separator + "*" + ".form.json");

            for(i = 0; i < resources.length; ++i) {
                this.addToFormModelPathFieldMap(resources[i], File.separator);
            }
        } catch (IOException var5) {
            LOGGER.warn("加载本地外部资源失败,没有外部资源", var5);
        }

        if (log.isDebugEnabled()) {
            log.debug("最终加载JSON文件的路径为个数为：[{}]", this.formModelPathFieldMap.size());
            this.formModelPathFieldMap.forEach((key, value) -> {
                log.debug("加载的问题JSON文件表单CODE为[{}], 应用CODE为[{}], 模块CODE为[{}], 服务CODE为[{}] ", new Object[]{key, value.getAppCode(), value.getModuleCode(), value.getServiceName()});
            });
        }

    }

    private void addToFormModelPathFieldMap(Resource resource, String separator) throws IOException {
        String path = resource.getURI().toString();
        String code = this.getValueFromString(path, path.indexOf(".form.json"), separator);
        String formPath = path.substring(0, path.lastIndexOf(separator));
        String modulePath = formPath.substring(0, formPath.lastIndexOf(separator + ResourceTypeEnum.FORM.getType()));
        String moduleCode = this.getValueFromString(modulePath, 0, separator);
        String appPath = modulePath.substring(0, modulePath.lastIndexOf(separator));
        String appCode = this.getValueFromString(appPath, 0, separator);
        ModelPathFieldVO modelPathFieldVO = new ModelPathFieldVO();
        modelPathFieldVO.setAppCode(appCode);
        modelPathFieldVO.setModuleCode(moduleCode);
        this.formModelPathFieldMap.put(code, modelPathFieldVO);
    }

    private String getValueFromString(String path, int endIndex, String separator) {
        return endIndex == 0 ? path.substring(path.lastIndexOf(separator) + 1) : path.substring(path.lastIndexOf(separator) + 1, endIndex);
    }

    public ModelPathFieldVO getModelPathFiledByCode(String code) {
        return (ModelPathFieldVO)this.formModelPathFieldMap.get(code);
    }

    public Boolean deleteResource(String appCode, String moduleCode, String modelCode, String type) {
        this.cleanFormView("", "");
        String filePath = this.getFilePath(appCode, moduleCode, modelCode, type);
        File file = new File(filePath);
        return file.delete();
    }

    public <T> Boolean updateResource(String appCode, String moduleCode, String modelCode, String type, Object o) {
        this.cleanFormView("", "");
        String filePath = this.getFilePath(appCode, moduleCode, modelCode, type);
        FileWriter writer = null;

        Boolean var9;
        try {
            writer = new FileWriter(filePath);
            writer.write(JSON.toJSONString(o, new SerializerFeature[]{SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat}));
            return Boolean.TRUE;
        } catch (Exception var13) {
            LOGGER.error("更新资源失败,路径[{}]:", filePath);
            var9 = false;
        } finally {
            IoUtil.close(writer);
        }

        return var9;
    }

    private String getFilePath(String appCode, String moduleCode, String modelCode, String type) {
        return this.jsonBasePath + File.separator + "runtime" + File.separator + appCode + File.separator + moduleCode + File.separator + type + File.separator + modelCode + ".form.json".replace(ResourceTypeEnum.FORM.getType(), type);
    }
}
