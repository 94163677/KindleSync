package air.kanna.kindlesync.filter;

import java.util.ArrayList;
import java.util.List;

public class MutiScanFilter<T> implements ObjectFilter<T> {

    public static final int ONE_ACCEPT = 1;
    public static final int ONE_REJECT = 2;
    
    private List<ObjectFilter<T>> filters = new ArrayList<>();
    private int mode = ONE_REJECT;
    
    @Override
    public boolean accept(T object) {
        if(filters == null) {
            filters = new ArrayList<>();
        }
        if(mode == ONE_REJECT) {
            for(ObjectFilter<T> filter : filters) {
                if(!filter.accept(object)) {
                    return false;
                }
            }
            return true;
        }else {
            for(ObjectFilter<T> filter : filters) {
                if(filter.accept(object)) {
                    return true;
                }
            }
            return false;
        }
    }

    
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        switch(mode) {
            case ONE_ACCEPT:;
            case ONE_REJECT: this.mode = mode;break;
            default: throw new java.lang.IllegalArgumentException("UNKNOW MODE: " + mode);
        }
    }

    public List<ObjectFilter<T>> getFilters() {
        return filters;
    }

    public void setFilters(List<ObjectFilter<T>> filters) {
        this.filters = filters;
    }
}
