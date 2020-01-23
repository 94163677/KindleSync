package air.kanna.kindlesync.compare;

public class OperationItem<T> {

    private T item;
    private T orgItem;
    private Operation operation;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        if(operation == null) {
            sb.append("NULL -> ");
        }else {
            sb.append(operation.getOperationCode()).append(" -> ");
        }
        if(item == null) {
            sb.append("NULL");
        }else {
            sb.append(item.toString());
        }
        
        return sb.toString();
    }
    
    public T getItem() {
        return item;
    }
    public void setItem(T item) {
        this.item = item;
    }
    public T getOrgItem() {
        return orgItem;
    }

    public void setOrgItem(T orgItem) {
        this.orgItem = orgItem;
    }

    public Operation getOperation() {
        return operation;
    }
    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
