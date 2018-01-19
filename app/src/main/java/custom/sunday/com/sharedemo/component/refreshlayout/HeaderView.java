package custom.sunday.com.sharedemo.component.refreshlayout;

import android.view.View;

/**
 * Created by zhongfei.sun on 2018/1/19.
 */

public interface HeaderView {
    void begin();

    void end();

    void progress(float progress);

    void loading();

    void normal();

    View getView();


}
