package air.kanna.kindlesync.execute;

import java.io.File;
import java.util.List;

import air.kanna.kindlesync.compare.OperationItem;

public interface OperationExecute {
    List<String> execute(File base, File dest, List<OperationItem> operation);
}
