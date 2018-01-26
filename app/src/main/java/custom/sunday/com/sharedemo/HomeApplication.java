package custom.sunday.com.sharedemo;

import android.app.Application;

import custom.sunday.com.sharedemo.component.objectbox.data.MyObjectBox;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * Created by zhongfei.sun on 2018/1/11.
 */

public class HomeApplication extends Application {
    private static HomeApplication INSTANCE;
    private BoxStore mBoxStore;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        mBoxStore = MyObjectBox.builder().androidContext(this).build();
        //adb forward tcp:8090 tcp:8090
        //http://localhost:8090/index.html
        if(BuildConfig.DEBUG) {
            new AndroidObjectBrowser(mBoxStore).start(this);
        }
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }



    public static HomeApplication getInstance(){
        return INSTANCE;
    }

}
