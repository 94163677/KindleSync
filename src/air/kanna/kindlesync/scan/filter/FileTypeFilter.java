package air.kanna.kindlesync.scan.filter;

import java.io.File;

public class FileTypeFilter implements ScanFilter {
	public static final int TYPE_ALL = 0b111;
	public static final int TYPE_FILE = 0b1;
    public static final int TYPE_DIRECTORY = 0b10;
    public static final int TYPE_HIDE = 0b100;
    
    private int acceptType = 0;
    
    @Override
    public boolean accept(File file) {
    	if(acceptType == TYPE_ALL){
    		return true;
    	}
        if((acceptType & TYPE_FILE) > 0 && file.isFile()) {
            return true;
        }
        if((acceptType & TYPE_DIRECTORY) > 0 && file.isDirectory()) {
            return true;
        }
        if((acceptType & TYPE_HIDE) > 0 && file.isDirectory()) {
            return true;
        }
        return false;
    }

    public void addType(int type) {
        acceptType |= type;
    }
    
    public void delType(int type) {
        acceptType &= (~type);
    }
}
