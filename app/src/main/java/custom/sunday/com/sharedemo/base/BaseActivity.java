package custom.sunday.com.sharedemo.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import custom.sunday.com.sharedemo.component.rx.RxLife;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhongfei.sun on 2018/1/17.
 */

public class BaseActivity extends Activity implements RxLife {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNetworkErrorTips() {

    }

    @Override
    public void showErrorTips(int resId) {

    }

    @Override
    public void addDisposable(Disposable disposable) {

    }

    @Override
    public void removeDisposable(Disposable disposable) {

    }

    @Override
    public void disposableAll() {

    }
}
