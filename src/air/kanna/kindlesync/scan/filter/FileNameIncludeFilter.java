package air.kanna.kindlesync.scan.filter;

import java.io.File;

public class FileNameIncludeFilter extends BaseKeysScanFilter{
    
    public FileNameIncludeFilter(String include) {
        super(include);
        defaultAccept = false;
        isAcceptTrue = true;
    }
    
    @Override
    boolean acceptKey(String key, File file) {
        return file.getName().contains(key);
    }
}
