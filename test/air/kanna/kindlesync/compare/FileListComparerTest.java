package air.kanna.kindlesync.compare;

import java.io.File;
import java.util.List;

import air.kanna.kindlesync.scan.PathScanner;

public class FileListComparerTest {

    public static void main(String[] args) {
        File file1 = new File("D:\\temp\\dssss\\SyosetuSpider001");
        File file2 = new File("D:\\temp\\SyosetuSpider002");
        PathScanner scanner = new PathScanner();
        FileListComparer comparer = new FileListComparer();
        
        List<File> list1 = scanner.scan(file1);
        List<File> list2 = scanner.scan(file2);
        ((FileListComparer)comparer).setBasePath(file1);
        ((FileListComparer)comparer).setDestPath(file2);
        
        List<OperationItem<File>> result = comparer.getCompareResult(list1, list2);
        
        for(OperationItem item : result) {
            System.out.println(item.toString());
        }
    }
}
