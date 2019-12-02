package air.kanna.kindlesync.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import air.kanna.kindlesync.filter.MutiScanFilter;

public class PathScanner extends MutiScanFilter<File> implements FileScanner{
    
    private boolean isAddBasePath = true;
    private boolean isScanDirectoryWhenUnaccept = false;
    
    public PathScanner() {
        setMode(ONE_REJECT);
    }
    
    @Override
    public List<File> scan(File basePath){
        List<File> fileList = new ArrayList<>();
        scan(fileList, basePath, 0);
        return fileList;
    }
    
    private void scan(List<File> fileList, File basePath, int deep) {
        if(basePath == null) {
            return;
        }
        
        if(deep == 0) {
            if(isAddBasePath) {
                fileList.add(basePath);
                if(!accept(basePath)) {
                    return;
                }
            }
        }else {
            if(accept(basePath)) {
                fileList.add(basePath);
            }else {
                if(!isScanDirectoryWhenUnaccept) {
                    return;
                }
            }
        }
        
        if(basePath.isDirectory()) {
            File[] files = basePath.listFiles();
            if(files == null || files.length <= 0){
            	return;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i] == null) {
                    continue;
                }
                if(files[i].getName().equals(".")
                        || files[i].getName().equals("..")) {
                    continue;
                }
                scan(fileList, files[i], deep + 1);
            }
        }
    }

    public boolean isAddBasePath() {
        return isAddBasePath;
    }

    public boolean isScanDirectoryWhenUnaccept() {
        return isScanDirectoryWhenUnaccept;
    }

    public void setAddBasePath(boolean isAddBasePath) {
        this.isAddBasePath = isAddBasePath;
    }

    public void setScanDirectoryWhenUnaccept(boolean isScanDirectoryWhenUnaccept) {
        this.isScanDirectoryWhenUnaccept = isScanDirectoryWhenUnaccept;
    }
}
