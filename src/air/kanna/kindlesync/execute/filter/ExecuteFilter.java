package air.kanna.kindlesync.execute.filter;

import air.kanna.kindlesync.compare.OperationItem;
import air.kanna.kindlesync.filter.ObjectFilter;

public interface ExecuteFilter extends ObjectFilter<OperationItem>{
    boolean accept(OperationItem item);
}
