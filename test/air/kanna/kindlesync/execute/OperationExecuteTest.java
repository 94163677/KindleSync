package air.kanna.kindlesync.execute;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import air.kanna.kindlesync.compare.Operation;
import air.kanna.kindlesync.compare.OperationItem;

public class OperationExecuteTest {

    public static void main(String[] args) {
        File base = new File("D:\\temp\\dssss\\SyosetuSpider001");
        File dest = new File("D:\\temp\\SyosetuSpider002");
        File execAdd = new File("D:\\temp\\dssss\\SyosetuSpider001\\src\\log4j.properties");
        File execRep = new File("D:\\temp\\dssss\\SyosetuSpider001\\.classpath");
        File execDel = new File("D:\\temp\\SyosetuSpider002\\.project");
        
        List<OperationItem> list = new ArrayList<>();
        OperationItem<File> item = null;
        JaveExecute exec = new JaveExecute();
        
        item = new OperationItem<>();
        item.setOperation(Operation.ADD);
        item.setItem(execAdd);
        list.add(item);
        
        item = new OperationItem<>();
        item.setOperation(Operation.REP);
        item.setItem(execRep);
        list.add(item);
        
        item = new OperationItem<>();
        item.setOperation(Operation.DEL);
        item.setItem(execDel);
        list.add(item);
        
        List<String> result = exec.execute(base, dest, list);
        for(String res : result) {
            System.out.println("result: " + res);
        }
    }
    
}
