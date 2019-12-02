package air.kanna.kindlesync.scan.filter.senior;

import java.io.File;

import air.kanna.kindlesync.scan.filter.ScanFilter;

public class FileConditionFilterImpl 
		implements ScanFilter, FileConditionFilter {

	private FileScanCondition condition;
	
	public FileConditionFilterImpl(FileScanCondition condition){
		if(condition == null){
			throw new NullPointerException("FileScanCondition is null");
		}
		this.condition = condition;
	}
	
	@Override
	public boolean accept(File file) {
		return conditionAccept(condition, file);
	}
	
	@Override
	public boolean isContinueScanRejectPath(){
		return condition.isAcceptRejectContinue();
	}
	

}
