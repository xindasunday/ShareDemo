package custom.sunday.com.sharedemo.component.rx;

import java.util.List;

/**
 * Created by zhongfei.sun on 2017/11/20.
 */

public class PageData<T> {
    private int pageTotal;
    private int pageNo;
    private List<T> pageData;
    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }
    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    public int getPageNo() {
        return pageNo;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }
    public List<T> getPageData() {
        return pageData;
    }
}
