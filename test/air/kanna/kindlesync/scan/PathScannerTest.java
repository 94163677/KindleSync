package air.kanna.kindlesync.scan;

import java.io.File;
import java.util.Collections;
import java.util.List;

import air.kanna.kindlesync.scan.filter.FileNameExcludeFilter;

public class PathScannerTest {

    public static void main(String[] args) {
//        String basePath = "D:\\GitSource\\fortune";//财富库
//        String basePath = "D:\\GitSource\\assist";//帮助库
//        String basePath = "D:\\GitSource\\fortune\\02标准过程库";//标准过程库
        String basePath = "D:\\GitSource\\fortune\\03项目归档\\2019年结项的项目\\华发新媒体管理平台";
        
        File path = new File(basePath);
        PathScanner scanner = new PathScanner();
        
        FileNameExcludeFilter filter01 = new FileNameExcludeFilter(".git");
        scanner.getFilters().add(filter01);
        
        List<File> list = scanner.scan(path);
        
        Collections.sort(list, (File a, File b) -> a.getAbsolutePath().compareTo(b.getAbsolutePath()));

        for(File file : list) {
            if(file.isDirectory()) {
                continue;
            }
            String filePath = file.getAbsolutePath().substring(basePath.length() + 1);
            System.out.println(filePath.replaceAll("\\\\", "\t"));
        }
    }
}
