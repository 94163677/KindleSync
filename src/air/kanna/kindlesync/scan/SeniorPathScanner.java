package air.kanna.kindlesync.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import air.kanna.kindlesync.filter.ObjectFilter;
import air.kanna.kindlesync.filter.SeniorObjectFilter;
import air.kanna.kindlesync.scan.filter.senior.FileConditionFilter;

public class SeniorPathScanner extends SeniorObjectFilter<File> implements FileScanner {

	private boolean scanContinue = false;
	
	@Override
	public List<File> scan(File basePath) {
		List<File> fileList = new ArrayList<>();
        scan(fileList, basePath, 0);
        return fileList;
	}
	
	private void scan(List<File> fileList, File basePath, int deep) {
        if(basePath == null) {
            return;
        }
        
        scanContinue = false;
        if(accept(basePath)) {
        	fileList.add(basePath);
        }else{
        	if(!scanContinue){
        		return;
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
	
	@Override
	protected boolean acceptNot(File file){
		for(ObjectFilter<File> notFilter : acceptNotFilters){
			if(notFilter.accept(file)){
				if(!scanContinue){
					if(notFilter instanceof FileConditionFilter){
						scanContinue = ((FileConditionFilter)notFilter).isContinueScanRejectPath();
					}
				}
				return false;
			}
		}
		return true;
	}

}
