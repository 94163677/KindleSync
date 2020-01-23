package air.kanna.kindlesync.compare;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileListComparer implements ListComparer<File>{
    
    private File basePath;
    private File destPath;
    
    @Override
    public List<OperationItem<File>> getCompareResult(
            List<File> baseFileList, List<File> destFileList){
        List<OperationItem<File>> result = new ArrayList<>();
        
        if(baseFileList == null) {
            baseFileList = new ArrayList<>();
        }
        if(destFileList == null) {
            destFileList = new ArrayList<>();
        }
        
        int index = -1;
        final int basePathLength = basePath.getAbsolutePath().length();
        final int destPathLength = destPath.getAbsolutePath().length();
        
        OperationItem<File> oper = null;
        Comparator<File> comparator = (File a, File b) -> a.getAbsolutePath().compareTo(b.getAbsolutePath());
        
        Collections.sort(baseFileList, comparator);
        Collections.sort(destFileList, comparator);
        
        for(File file : baseFileList) {
            index = Collections.binarySearch(destFileList, file, 
                    (File a, File b) -> a.getAbsolutePath().substring(destPathLength).compareTo(b.getAbsolutePath().substring(basePathLength)));
            if(index < 0) {
                oper = new OperationItem<>();
                oper.setItem(file);
                oper.setOperation(Operation.ADD);
                result.add(oper);
                continue;
            }
            if(!file.isFile()) {
                continue;
            }
            if(file.length() != destFileList.get(index).length()) {
                oper = new OperationItem<>();
                oper.setItem(file);
                oper.setOrgItem(destFileList.get(index));
                oper.setOperation(Operation.REP);
                result.add(oper);
            }
        /*    if(file.lastModified() > destFileList.get(index).lastModified()) {
                oper = new FileOperationItem();
                oper.setFile(file);
                oper.setOperation(FileOperation.REP);
                result.add(oper);
            }*/
        }
        
        for(File file : destFileList) {
            index = Collections.binarySearch(baseFileList, file, 
                    (File a, File b) -> a.getAbsolutePath().substring(basePathLength).compareTo(b.getAbsolutePath().substring(destPathLength)));
            if(index < 0) {
                oper = new OperationItem<>();
                oper.setItem(file);
                oper.setOperation(Operation.DEL);
                result.add(oper);
                continue;
            }
        }
        
        return result;
    }

    public File getBasePath() {
        return basePath;
    }

    public File getDestPath() {
        return destPath;
    }

    public void setBasePath(File basePath) {
        this.basePath = basePath;
    }

    public void setDestPath(File destPath) {
        this.destPath = destPath;
    }
    
    
    
}
