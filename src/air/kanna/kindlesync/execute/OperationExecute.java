package air.kanna.kindlesync.execute;

import java.io.File;
import java.util.List;

import air.kanna.kindlesync.compare.FileOperationItem;

public interface OperationExecute {
    List<String> execute(File base, File dest, List<FileOperationItem> operation);
}
