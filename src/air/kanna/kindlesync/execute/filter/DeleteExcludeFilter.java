package air.kanna.kindlesync.execute.filter;

import air.kanna.kindlesync.compare.FileOperation;
import air.kanna.kindlesync.compare.FileOperationItem;

public class DeleteExcludeFilter implements ExecuteFilter {

    @Override
    public boolean accept(FileOperationItem item) {
        if(item == null || item.getOperation() == null) {
            return false;
        }
        if(item.getOperation() == FileOperation.DEL) {
            return false;
        }
        return true;
    }
}
