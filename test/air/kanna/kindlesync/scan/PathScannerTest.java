package air.kanna.kindlesync.scan;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class PathScannerTest {

    public static void main(String[] args) {
        File path = new File("D:\\eclipse-workspace");
        PathScanner scanner = new PathScanner();
        
        List<File> list = scanner.scan(path);
        
        Collections.sort(list, (File a, File b) -> a.getAbsolutePath().compareTo(b.getAbsolutePath()));

        for(File file : list) {
            System.out.println(file.getAbsolutePath());
        }
    }
}
