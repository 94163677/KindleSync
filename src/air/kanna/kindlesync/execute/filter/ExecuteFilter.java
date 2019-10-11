package air.kanna.kindlesync.execute.filter;

import air.kanna.kindlesync.compare.FileOperationItem;
import air.kanna.kindlesync.filter.ObjectFilter;

public interface ExecuteFilter extends ObjectFilter<FileOperationItem>{
    boolean accept(FileOperationItem item);
}
