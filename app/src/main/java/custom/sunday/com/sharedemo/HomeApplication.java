package custom.sunday.com.sharedemo;

import android.app.Application;

/**
 * Created by zhongfei.sun on 2018/1/11.
 */

public class HomeApplication extends Application {
    private static HomeApplication INSTANCE;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }


    public static HomeApplication getInstance(){
        return INSTANCE;
    }

}
