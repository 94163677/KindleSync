package air.kanna.kindlesync.compare;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileListComparer {

    public List<FileOperationItem> getCompareResult(
            File basePath, File destPath,
            List<File> baseFileList, List<File> destFileList){
        List<FileOperationItem> result = new ArrayList<>();
        
        if(baseFileList == null) {
            baseFileList = new ArrayList<>();
        }
        if(destFileList == null) {
            destFileList = new ArrayList<>();
        }
        
        int index = -1;
        final int basePathLength = basePath.getAbsolutePath().length();
        final int destPathLength = destPath.getAbsolutePath().length();
        
        FileOperationItem oper = null;
        Comparator<File> comparator = (File a, File b) -> a.getAbsolutePath().compareTo(b.getAbsolutePath());
        
        Collections.sort(baseFileList, comparator);
        Collections.sort(destFileList, comparator);
        
        for(File file : baseFileList) {
            index = Collections.binarySearch(destFileList, file, 
                    (File a, File b) -> a.getAbsolutePath().substring(destPathLength).compareTo(b.getAbsolutePath().substring(basePathLength)));
            if(index < 0) {
                oper = new FileOperationItem();
                oper.setFile(file);
                oper.setOperation(FileOperation.ADD);
                result.add(oper);
                continue;
            }
            
            if(file.isFile() && file.lastModified() > destFileList.get(index).lastModified()) {
                oper = new FileOperationItem();
                oper.setFile(file);
                oper.setOperation(FileOperation.REP);
                result.add(oper);
            }
        }
        
        for(File file : destFileList) {
            index = Collections.binarySearch(baseFileList, file, 
                    (File a, File b) -> a.getAbsolutePath().substring(basePathLength).compareTo(b.getAbsolutePath().substring(destPathLength)));
            if(index < 0) {
                oper = new FileOperationItem();
                oper.setFile(file);
                oper.setOperation(FileOperation.DEL);
                result.add(oper);
                continue;
            }
        }
        
        return result;
    }
    
}
