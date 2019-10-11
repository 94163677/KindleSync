package air.kanna.kindlesync.execute;

import air.kanna.kindlesync.ProcessListener;
import air.kanna.kindlesync.compare.FileOperationItem;
import air.kanna.kindlesync.filter.MutiScanFilter;

public abstract class BaseExecuteWithFilterAndListener 
    extends MutiScanFilter<FileOperationItem>
    implements OperationExecute {

    protected ProcessListener listener;
    
    BaseExecuteWithFilterAndListener(){
        setMode(ONE_REJECT);
        listener = new NullProcessListener();
    }

    public ProcessListener getListener() {
        return listener;
    }

    public void setListener(ProcessListener listener) {
        this.listener = listener;
        if(this.listener == null) {
            this.listener = new NullProcessListener();
        }
    }
    
    private class NullProcessListener implements ProcessListener{
        @Override
        public void setMax(int max) {
        }

        @Override
        public void next(String message) {
        }

        @Override
        public void setPosition(int current, String message) {
        }

        @Override
        public void finish(String message) {
        }
    }
}
