package air.kanna.kindlesync.compare;

import java.io.File;

public class FileOperationItem {

    private File file;
    private FileOperation operation;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        if(operation == null) {
            sb.append("NULL -> ");
        }else {
            sb.append(operation.getOperationCode()).append(" -> ");
        }
        if(file == null) {
            sb.append("NULL");
        }else {
            sb.append(file.getAbsolutePath());
        }
        
        return sb.toString();
    }
    
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public FileOperation getOperation() {
        return operation;
    }
    public void setOperation(FileOperation operation) {
        this.operation = operation;
    }
}
