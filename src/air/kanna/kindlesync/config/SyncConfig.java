package air.kanna.kindlesync.config;

public class SyncConfig {

    private String basePath;
    private String descPath;
    
    private boolean isScanFile = true;
    private boolean isScanDir = true;
    
    private String includeStr;
    private String excludeStr;
    private boolean isIncludeRegex = false;
    private boolean isExcludeRegex = false;
    
    private boolean isExcludeNullDir = true;
    private boolean isUnExecuteDelete = true;
    
    
    public String getBasePath() {
        return basePath;
    }
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
    public String getDescPath() {
        return descPath;
    }
    public void setDescPath(String descPath) {
        this.descPath = descPath;
    }
    public boolean isScanFile() {
        return isScanFile;
    }
    public void setScanFile(boolean isScanFile) {
        this.isScanFile = isScanFile;
    }
    public boolean isScanDir() {
        return isScanDir;
    }
    public void setScanDir(boolean isScanDir) {
        this.isScanDir = isScanDir;
    }
    public String getIncludeStr() {
        return includeStr;
    }
    public void setIncludeStr(String includeStr) {
        this.includeStr = includeStr;
    }
    public String getExcludeStr() {
        return excludeStr;
    }
    public void setExcludeStr(String excludeStr) {
        this.excludeStr = excludeStr;
    }
    public boolean isIncludeRegex() {
        return isIncludeRegex;
    }
    public void setIncludeRegex(boolean isIncludeRegex) {
        this.isIncludeRegex = isIncludeRegex;
    }
    public boolean isExcludeRegex() {
        return isExcludeRegex;
    }
    public void setExcludeRegex(boolean isExcludeRegex) {
        this.isExcludeRegex = isExcludeRegex;
    }
    public boolean isExcludeNullDir() {
        return isExcludeNullDir;
    }
    public void setExcludeNullDir(boolean isExcludeNullDir) {
        this.isExcludeNullDir = isExcludeNullDir;
    }
    public boolean isUnExecuteDelete() {
        return isUnExecuteDelete;
    }
    public void setUnExecuteDelete(boolean isUnExecuteDelete) {
        this.isUnExecuteDelete = isUnExecuteDelete;
    }
}
