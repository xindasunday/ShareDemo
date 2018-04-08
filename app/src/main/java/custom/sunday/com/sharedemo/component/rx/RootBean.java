package custom.sunday.com.sharedemo.component.rx;

/**
 * Created by zhongfei.sun on 2017/10/16.
 */

public class RootBean<T> {
    private String error;
    private String logID;
    private String desc;
    private T result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
