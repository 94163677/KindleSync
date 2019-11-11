package air.kanna.kindlesync.config.impl;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;

import air.kanna.kindlesync.config.SyncConfig;
import air.kanna.kindlesync.config.SyncConfigService;
import air.kanna.kindlesync.util.Nullable;

public class SyncConfigServicePropertiesImpl 
        extends BaseFileConfigService<SyncConfig>
        implements SyncConfigService{
    private static final Logger logger = Logger.getLogger(SyncConfigServicePropertiesImpl.class);

    public SyncConfigServicePropertiesImpl(File propFile) {
        super(propFile);
    }

    @Override
    protected SyncConfig prop2Config(Properties prop) {
        if(prop == null || prop.size() <= 0) {
            return null;
        }
        SyncConfig config = new SyncConfig();
        
        String temp = prop.getProperty("basePath");
        if(!Nullable.isNull(temp)) {
            config.setBasePath(temp);
        }
        
        temp = prop.getProperty("descPath");
        if(!Nullable.isNull(temp)) {
            config.setDescPath(temp);
        }
        
        temp = prop.getProperty("isScanFile");
        if(!Nullable.isNull(temp)) {
            if("true".equalsIgnoreCase(temp)) {
                config.setScanFile(true);
            }else {
                config.setScanFile(false);
            }
        }
        
        temp = prop.getProperty("isScanDir");
        if(!Nullable.isNull(temp)) {
            if("true".equalsIgnoreCase(temp)) {
                config.setScanDir(true);
            }else {
                config.setScanDir(false);
            }
        }
        
        temp = prop.getProperty("includeStr");
        if(!Nullable.isNull(temp)) {
            config.setIncludeStr(temp);
        }
        
        temp = prop.getProperty("excludeStr");
        if(!Nullable.isNull(temp)) {
            config.setExcludeStr(temp);
        }
        
        temp = prop.getProperty("isIncludeRegex");
        if(!Nullable.isNull(temp)) {
            if("true".equalsIgnoreCase(temp)) {
                config.setIncludeRegex(true);
            }else {
                config.setIncludeRegex(false);
            }
        }
        
        temp = prop.getProperty("isExcludeRegex");
        if(!Nullable.isNull(temp)) {
            if("true".equalsIgnoreCase(temp)) {
                config.setExcludeRegex(true);
            }else {
                config.setExcludeRegex(false);
            }
        }
        
        temp = prop.getProperty("isExcludeNullDir");
        if(!Nullable.isNull(temp)) {
            if("true".equalsIgnoreCase(temp)) {
                config.setExcludeNullDir(true);
            }else {
                config.setExcludeNullDir(false);
            }
        }
        
        temp = prop.getProperty("isUnExecuteDelete");
        if(!Nullable.isNull(temp)) {
            if("true".equalsIgnoreCase(temp)) {
                config.setUnExecuteDelete(true);
            }else {
                config.setUnExecuteDelete(false);
            }
        }
        
        return config;
    }
    
    @Override
    protected Properties config2Prop(SyncConfig config) {
        if(config == null) {
            return null;
        }
        Properties prop = new Properties();
        
        prop.put("basePath", config.getBasePath());
        prop.put("descPath", config.getDescPath());
        prop.put("isScanFile", "" + config.isScanFile());
        prop.put("isScanDir", "" + config.isScanDir());
        prop.put("includeStr", config.getIncludeStr());
        prop.put("excludeStr", config.getExcludeStr());
        prop.put("isIncludeRegex", "" + config.isIncludeRegex());
        prop.put("isExcludeRegex", "" + config.isExcludeRegex());
        prop.put("isExcludeNullDir", "" + config.isExcludeNullDir());
        prop.put("isUnExecuteDelete", "" + config.isUnExecuteDelete());
        
        return prop;
    }
}
