package custom.sunday.com.sharedemo.component.rx;

import io.reactivex.disposables.Disposable;


/**
 * Created by zhongfei.sun on 2017/10/26.
 */

public interface RxLife {

    void showLoading();

    void hideLoading();

    void showNetworkErrorTips();

    void showErrorTips(int resId);

    void addDisposable(Disposable disposable);

    void removeDisposable(Disposable disposable);

    void disposableAll();
}
