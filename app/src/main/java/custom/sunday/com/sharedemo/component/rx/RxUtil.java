package custom.sunday.com.sharedemo.component.rx;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import custom.sunday.com.sharedemo.HomeApplication;
import custom.sunday.com.sharedemo.R;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/8/14.
 */

public class RxUtil {

    /**
    * 线程切换，用一个方法代替,免去手抖打错线程
    * */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * RxJava2.0中不允许发送空指针，所以需要在这里判断如果为空，则发送empty
     * 后台格式固定为RootBean
     * private String error; //错误码
     * private String logID; //调试ID
     * private String desc;  //详细描述
     * private T result;     //我们关心的数据
     * 我们只对数据感兴趣，所以使用此方法转换Observable只拿result
     * */
    public static <T> ObservableTransformer<RootBean<T>, T> handleResult() {
        return new ObservableTransformer<RootBean<T>, T>() {
            @Override
            public Observable<T> apply(Observable<RootBean<T>> rootBeanObservable) {
                return rootBeanObservable.flatMap(new Function<RootBean<T>, Observable<T>>() {
                    @Override
                    public Observable<T> apply(RootBean<T> rootBean) {
                        if (rootBean.getError().equals(HttpCode.SUCCESS)) {
                            if (rootBean.getResult() == null) {
                                return Observable.empty();
                            } else {
                                return Observable.just(rootBean.getResult());
                            }
                        } else {
                            return Observable.error(new NormalException(
                                    rootBean.getError(),
                                    rootBean.getDesc(),
                                    rootBean.getLogID()));
                        }
                    }
                });
            }
        };
    }

    /**
     * PageData
     * private int pageTotal;
     * private int pageNo;
     * private List<T> pageData;
     * 同样的，在APP中分页数据很常见，所以封装了PageData,直接将数据变换为List
     * */

    public static <T> ObservableTransformer<RootBean<PageData<T>>, List<T>> handlePageResult() {
        return new ObservableTransformer<RootBean<PageData<T>>, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(Observable<RootBean<PageData<T>>> observable) {
                return observable.flatMap(new Function<RootBean<PageData<T>>, ObservableSource<List<T>>>() {
                    @Override
                    public ObservableSource<List<T>> apply(RootBean<PageData<T>> rootBean) throws Exception {
                        if (rootBean.getError().equals(HttpCode.SUCCESS)) {
                            if (rootBean.getResult() == null) {
                                return Observable.empty();
                            } else {
                                return Observable.just(rootBean.getResult().getPageData());
                            }
                        } else {
                            return Observable.error(new NormalException(
                                    rootBean.getError(),
                                    rootBean.getDesc(),
                                    rootBean.getLogID()));
                        }
                    }
                });
            }
        };
    }

    /**
     * 这里有一个重要的接口RxLife，此RxLife由baseActivity或者baseFragment实现，
     * 用于记录Disposable来实现界面和请求的生命周期同步,当在网络请求中时，离开界面，此时自动取消订阅
     * 为什么要实现Disposable接口？是因为取消订阅后我们不能收到通知，所以实现Disposable后，将自身传入，
     * 取消订阅时会回调dispose，用于界面更新
     * **/
    public abstract static class SimpleObserver<T> implements Observer<T>, Disposable {
        private String tag;
        private RxLife mRxLife;
        private boolean haveLoading = true;
        private Disposable disposable;
        private int resNetDisable = R.string.net_disable_tip;
        private int resNetNotWork = R.string.net_not_work_tip;

        public SimpleObserver(RxLife rxLife) {
            mRxLife = rxLife;
        }

        /**
         * @param tag
         * 调试异常结果，传入TAG,可以过滤多余信息
         * */
        public SimpleObserver(RxLife rxLife, String tag) {
            mRxLife = rxLife;
            this.tag = tag;
        }

        /**
         * @param haveLoading
         *
         * **/
        public SimpleObserver(RxLife rxLife, boolean haveLoading) {
            mRxLife = rxLife;
            this.haveLoading = haveLoading;
        }

        /**
         * 获取网络中断提示语,如果界面需要单独的提示语，则覆写此方法
         */
        protected int getResNetDisable() {
            return resNetDisable;
        }

        /**
         * 获取网络异常提示语，如果界面需要单独的提示语，则覆写此方法
         */
        protected int getResNetNotWork() {
            return resNetNotWork;
        }

        /**
         * 隐藏网络异常提示,如果不需要网络异常提示，则覆写此方法改为true
         * @return
         */
        public boolean isHideErrorTip() {
            return false;
        }

        @Override
        public void onSubscribe(Disposable disposable) {
            this.disposable = disposable;
            if (mRxLife != null) {
                mRxLife.addDisposable(this);
                if (haveLoading) {
                    mRxLife.showLoading();
                }
            }

        }

        @Override
        public void onComplete() {
            if (mRxLife != null) {
                mRxLife.removeDisposable(this);
                if (haveLoading) {
                    mRxLife.hideLoading();
                }
            } else {
                disposable.dispose();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            if (mRxLife != null) {
                mRxLife.removeDisposable(this);
                if (haveLoading) {
                    mRxLife.hideLoading();
                }
            } else {
                disposable.dispose();
            }

            //look HttpCode.java
            String errorMsg = e.getMessage();
            if (e instanceof NormalException) {
                if (errorMsg.equals(HttpCode.RELOGIN)) {
                    //HomeApplication.getInstance().logout();
                } else if (errorMsg.equals(HttpCode.SERVICE_ERROR)) {
                    if (mRxLife != null && !isHideErrorTip()) {
                        mRxLife.showErrorTips(resNetNotWork);
                    }
                }
            } else {
                if (mRxLife != null && !isHideErrorTip()) {
//                    if (!NetworkUtil.getInstance(HomeApplication.getInstance().getApplicationContext()).isNetworkConnected()) {
//                        mRxLife.showErrorTips(getResNetDisable());     //网络断开提示
//                    } else {
//                        mRxLife.showErrorTips(getResNetNotWork());        //网络无法访问提示
//                    }
                }
            }
            onErrorMsg(errorMsg);
        }

        protected void onErrorMsg(String msg) {
            if (tag != null) {
                Log.e("RxUtil", "tag = " + tag + "   onErrorMsg | = " + msg);
            } else {
                Log.e("RxUtil", "onErrorMsg | = " + msg);
            }
        }

        @Override
        public void dispose() {
            disposable.dispose();
        }

        @Override
        public boolean isDisposed() {
            return disposable.isDisposed();
        }

        @Override
        public void onNext(T t) {
            onResult(t);
        }

        abstract public void onResult(T t);
    }


    public static class NormalException extends Exception {
        private String desc;
        private String logId;

        public NormalException(String msg) {
            super(msg);
        }

        public NormalException(String msg, String desc, String logId) {
            this(msg);
            this.desc = desc;
            this.logId = logId;
        }

        public String getLogId() {
            return logId;
        }
    }

}
