package air.kanna.kindlesync.execute;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import air.kanna.kindlesync.compare.FileOperation;
import air.kanna.kindlesync.compare.FileOperationItem;

public class OperationExecuteTest {

    public static void main(String[] args) {
        File base = new File("D:\\temp\\dssss\\SyosetuSpider001");
        File dest = new File("D:\\temp\\SyosetuSpider002");
        File execAdd = new File("D:\\temp\\dssss\\SyosetuSpider001\\src\\log4j.properties");
        File execRep = new File("D:\\temp\\dssss\\SyosetuSpider001\\.classpath");
        File execDel = new File("D:\\temp\\SyosetuSpider002\\.project");
        
        List<FileOperationItem> list = new ArrayList<>();
        FileOperationItem item = null;
        OperationExecute exec = new OperationExecute();
        
        item = new FileOperationItem();
        item.setOperation(FileOperation.ADD);
        item.setFile(execAdd);
        list.add(item);
        
        item = new FileOperationItem();
        item.setOperation(FileOperation.REP);
        item.setFile(execRep);
        list.add(item);
        
        item = new FileOperationItem();
        item.setOperation(FileOperation.DEL);
        item.setFile(execDel);
        list.add(item);
        
        List<String> result = exec.execute(base, dest, list);
        for(String res : result) {
            System.out.println("result: " + res);
        }
    }
    
}
