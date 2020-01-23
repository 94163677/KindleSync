package air.kanna.kindlesync.execute.filter;

import air.kanna.kindlesync.compare.Operation;
import air.kanna.kindlesync.compare.OperationItem;

public class DeleteExcludeFilter implements ExecuteFilter {

    @Override
    public boolean accept(OperationItem item) {
        if(item == null || item.getOperation() == null) {
            return false;
        }
        if(item.getOperation() == Operation.DEL) {
            return false;
        }
        return true;
    }
}
