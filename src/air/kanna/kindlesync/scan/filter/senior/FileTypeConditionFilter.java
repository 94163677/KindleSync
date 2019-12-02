package air.kanna.kindlesync.scan.filter.senior;

import java.io.File;

import air.kanna.kindlesync.scan.filter.FileTypeFilter;

public class FileTypeConditionFilter 
		extends FileTypeFilter 
		implements FileConditionFilter{

	private FileScanCondition condition;
	
	public FileTypeConditionFilter(FileScanCondition condition){
		super();
		if(condition == null){
			throw new NullPointerException("FileScanCondition is null");
		}
		this.condition = condition;
	}
	
	@Override
    public boolean accept(File file) {
		boolean isAccept = super.accept(file);
		if(!isAccept){
			return false;
		}
		return conditionAccept(condition, file);
	}
	
	@Override
	public boolean isContinueScanRejectPath(){
		return condition.isAcceptRejectContinue();
	}
}