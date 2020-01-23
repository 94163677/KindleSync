package air.kanna.kindlesync.compare;

import java.util.List;

public interface ListComparer<T> {

    List<OperationItem<T>> getCompareResult(
            List<T> baseFileList, List<T> destFileList);
}
