package air.kanna.kindlesync.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import air.kanna.kindlesync.filter.MutiScanFilter;

public class PathScanner extends MutiScanFilter<File>{
    
    public PathScanner() {
        setMode(ONE_REJECT);
    }
    
    public List<File> scan(File basePath){
        List<File> fileList = new ArrayList<>();
        scan(fileList, basePath);
        return fileList;
    }
    
    private void scan(List<File> fileList, File basePath) {
        if(basePath == null) {
            return;
        }
        if(!accept(basePath)) {
            return;
        }
        fileList.add(basePath);
        if(basePath.isDirectory()) {
            File[] files = basePath.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i] == null) {
                    continue;
                }
                if(files[i].getName().equals(".")
                        || files[i].getName().equals("..")) {
                    continue;
                }
                scan(fileList, files[i]);
            }
        }
    }
}
