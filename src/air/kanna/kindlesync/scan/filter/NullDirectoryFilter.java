package air.kanna.kindlesync.scan.filter;

import java.io.File;

public class NullDirectoryFilter implements ScanFilter {

    @Override
    public boolean accept(File file) {
        if(!file.isDirectory()) {
            return true;
        }
        File[] files = file.listFiles();
        if(files == null || files.length <= 0) {
            return false;
        }
        return true;
    }

}
