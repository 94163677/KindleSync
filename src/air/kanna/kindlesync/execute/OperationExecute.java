package air.kanna.kindlesync.execute;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import air.kanna.kindlesync.compare.FileOperationItem;
import air.kanna.kindlesync.filter.MutiScanFilter;

public class OperationExecute extends MutiScanFilter<FileOperationItem>{
    private static final Logger logger = Logger.getLogger(OperationExecute.class);
    
    public OperationExecute() {
        setMode(ONE_REJECT);
    }
    
    public List<String> execute(File base, File dest, List<FileOperationItem> operation){
        if(base == null || dest == null) {
            throw new NullPointerException("base dir or dest dir is null");
        }
        
        List<String> result = new ArrayList<>();
        String check = null;
        if(operation == null || operation.size() <= 0) {
            return result;
        }
        
        String basePath = base.getAbsolutePath();
        String destPath = dest.getAbsolutePath();
        
        for(FileOperationItem item : operation) {
            check = checkItem(item);
            if(check != null) {
                result.add(check);
                continue;
            }
            if(!accept(item)) {
                result.add("SKIP");
                continue;
            }
            switch(item.getOperation()) {
                case DEL: check = executeDel(item); break;
                case ADD: check = executeAdd(basePath, destPath, item); break;
                case REP: check = executeRep(basePath, destPath, item); break;
                default: result.add("UNKNOW OPERATION: " + item.getOperation().getOperationCode());
            }
            result.add(check);
        }
        
        return result;
    }
    
    private String executeDel(FileOperationItem item) {
        try {
            if(!item.getFile().delete()) {
                return "delete faild";
            }
        }catch(Exception e) {
            logger.error("delete file error: " + item.getFile().getAbsolutePath(), e);
            return e.getMessage();
        }
        return null;
    }
    
    private String executeAdd(String basePath, String destPath, FileOperationItem item) {
        try {
            String execPath = item.getFile().getAbsolutePath();
            
            execPath = execPath.substring(basePath.length());
            execPath = destPath + execPath;
            
            File addFile = new File(execPath);
            
            if(item.getFile().isDirectory()) {
                if(!addFile.mkdirs()) {
                    return "cannot create path: " + execPath;
                }
            }
            if(item.getFile().isFile()) {
                Files.copy(item.getFile().toPath(), addFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
            }
        }catch(Exception e) {
            logger.error("add file error: " + item.getFile().getAbsolutePath(), e);
            return e.getMessage();
        }
        return null;
    }
    
    private String executeRep(String basePath, String destPath, FileOperationItem item) {
        try {
            String execPath = item.getFile().getAbsolutePath();
            
            execPath = execPath.substring(basePath.length());
            execPath = destPath + execPath;
            
            File addFile = new File(execPath);
            
            if(item.getFile().isDirectory()) {
                return "Directory cannot replace";
            }
            if(item.getFile().isFile()) {
                Files.copy(item.getFile().toPath(), addFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }catch(Exception e) {
            logger.error("replace file error: " + item.getFile().getAbsolutePath(), e);
            return e.getMessage();
        }
        return null;
    }
    
    private String checkItem(FileOperationItem item) {
        if(item == null) {
            return "NULL";
        }
        if(item.getFile() == null) {
            return "NULL FILE";
        }
        if(item.getOperation() == null) {
            return "NULL OPERATION";
        }
        if(!item.getFile().exists()) {
            return "FILE NOT EXISTS";
        }
        return null;
    }
}
