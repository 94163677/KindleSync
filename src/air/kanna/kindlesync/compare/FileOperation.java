package air.kanna.kindlesync.compare;

public enum FileOperation {

    ADD("ADD"), DEL("DELETE"), REP("REPLACE");
    
    private String operCode;
    
    private FileOperation(String code) {
        operCode = code;
    }
    
    public String getOperationCode() {
        return operCode;
    }
}
