package air.kanna.kindlesync.scan.filter.senior;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import air.kanna.kindlesync.util.Nullable;

public interface FileConditionFilter {

	boolean isContinueScanRejectPath();
	
	default boolean conditionAccept(FileScanCondition condition, File file) {
		if(file == null){
			return false;
		}
		if(condition == null){
			return true;
		}
		if(Nullable.isNull(condition.getMatchName())){
			return true;
		}
		
		String fileName = file.getName();
		if(condition.isRegex()){
			Pattern p = Pattern.compile(condition.getMatchName());
			Matcher m = p.matcher(fileName);
			return m.find();
		}else{
			if(condition.isIgnoreCase()){
				fileName = fileName.toLowerCase();
				return fileName.indexOf(condition.getMatchName().toLowerCase()) >= 0;
			}else{
				return fileName.indexOf(condition.getMatchName()) >= 0;
			}
		}
	}
}
