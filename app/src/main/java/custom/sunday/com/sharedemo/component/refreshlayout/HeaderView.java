package custom.sunday.com.sharedemo.component.refreshlayout;

import android.view.View;

/**
 * Created by zhongfei.sun on 2018/1/19.
 */

public interface HeaderView {
    void begin();

    void progress(float progress);

    void loading();

    //初始化和结束后调用
    void normal();

    View getView();

    //刷新成功后暂停几秒用于显示动画效果后，执行Runnable
    void showPause(boolean success);

    boolean isPauseTime();

    int getDelayMillsTime();
}
