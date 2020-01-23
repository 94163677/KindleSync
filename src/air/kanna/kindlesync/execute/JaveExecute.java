package air.kanna.kindlesync.execute;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import air.kanna.kindlesync.compare.OperationItem;

public class JaveExecute 
        extends BaseExecuteWithFilterAndListener{
    
    private static final Logger logger = Logger.getLogger(JaveExecute.class);
    
    public JaveExecute() {
        super();
    }
    
    @Override
    public List<String> execute(File base, File dest, List<OperationItem> operation){
        if(base == null || dest == null) {
            throw new NullPointerException("base dir or dest dir is null");
        }
        
        List<String> result = new ArrayList<>();
        String check = null;
        if(operation == null || operation.size() <= 0) {
            return result;
        }
        
        listener.setMax(operation.size());
        listener.setPosition(0, null);
        
        String basePath = base.getAbsolutePath();
        String destPath = dest.getAbsolutePath();
        
        for(OperationItem item : operation) {
            check = checkItem(item);
            if(check != null) {
                result.add(check);
                listener.next(null);
                continue;
            }
            if(!accept(item)) {
                result.add("SKIP");
                listener.next(null);
                continue;
            }
            switch(item.getOperation()) {
                case DEL: check = executeDel(item); break;
                case ADD: check = executeAdd(basePath, destPath, item); break;
                case REP: check = executeRep(basePath, destPath, item); break;
                default: result.add("UNKNOW OPERATION: " + item.getOperation().getOperationCode());
            }
            result.add(check);
            listener.next(null);
        }
        
        listener.finish(null);
        return result;
    }
    
    private String executeDel(OperationItem item) {
        try {
            if(!((File)item.getItem()).delete()) {
                return "delete faild";
            }
        }catch(Exception e) {
            logger.error("delete file error: " + ((File)item.getItem()).getAbsolutePath(), e);
            return e.getMessage();
        }
        return null;
    }
    
    private String executeAdd(String basePath, String destPath, OperationItem item) {
        try {
            String execPath = ((File)item.getItem()).getAbsolutePath();
            
            execPath = execPath.substring(basePath.length());
            execPath = destPath + execPath;
            
            File addFile = new File(execPath);
            
            if(((File)item.getItem()).isDirectory()) {
                if(!addFile.mkdirs()) {
                    return "cannot create path: " + execPath;
                }
            }
            if(((File)item.getItem()).isFile()) {
                Files.copy(((File)item.getItem()).toPath(), addFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
            }
        }catch(Exception e) {
            logger.error("add file error: " + ((File)item.getItem()).getAbsolutePath(), e);
            return e.getMessage();
        }
        return null;
    }
    
    private String executeRep(String basePath, String destPath, OperationItem item) {
        try {
            String execPath = ((File)item.getItem()).getAbsolutePath();
            
            execPath = execPath.substring(basePath.length());
            execPath = destPath + execPath;
            
            File addFile = new File(execPath);
            
            if(((File)item.getItem()).isDirectory()) {
                return "Directory cannot replace";
            }
            if(((File)item.getItem()).isFile()) {
                Files.copy(((File)item.getItem()).toPath(), addFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }catch(Exception e) {
            logger.error("replace file error: " + ((File)item.getItem()).getAbsolutePath(), e);
            return e.getMessage();
        }
        return null;
    }
    
    private String checkItem(OperationItem item) {
        if(item == null) {
            return "NULL";
        }
        if(((File)item.getItem()) == null) {
            return "NULL FILE";
        }
        if(item.getOperation() == null) {
            return "NULL OPERATION";
        }
        if(!((File)item.getItem()).exists()) {
            return "FILE NOT EXISTS";
        }
        return null;
    }
}
