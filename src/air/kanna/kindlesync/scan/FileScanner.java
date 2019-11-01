package air.kanna.kindlesync.scan;

import java.io.File;
import java.util.List;

public interface FileScanner {
    List<File> scan(File basePath);
}
