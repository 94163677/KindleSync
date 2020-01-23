package air.kanna.kindlesync.compare;

public enum Operation {

    ADD("ADD"), DEL("DELETE"), REP("REPLACE");
    
    private String operCode;
    
    private Operation(String code) {
        operCode = code;
    }
    
    public String getOperationCode() {
        return operCode;
    }
}
