package air.kanna.kindlesync.scan.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import air.kanna.kindlesync.util.Nullable;

public abstract class BaseKeysScanFilter implements ScanFilter {

    List<String> keys;
    boolean defaultAccept;
    boolean isAcceptTrue;
    
    public BaseKeysScanFilter(String key) {
        if(!Nullable.isNull(key)) {
            String[] includes = key.split(";");
            if(includes == null || includes.length <= 0) {
                includes = new String[] {key};
            }
            this.keys = Arrays.asList(includes);
        }else {
            keys = new ArrayList<>();
        }
    }
    
    @Override
    public boolean accept(File file) {
        if(keys == null || keys.size() <= 0) {
            return true;
        }
        if(file == null) {
            return false;
        }
        
        boolean isAccept = false;
        for(String key: keys) {
            
            isAccept = acceptKey(key, file);
            if(isAcceptTrue) {
                if(isAccept) {
                    return true;
                }
            }else {
                if(isAccept) {
                    return false;
                }
            }
        }
        
        return defaultAccept;
    }
    
    abstract boolean acceptKey(String key, File file);
}
