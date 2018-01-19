package custom.sunday.com.sharedemo.common.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import custom.sunday.com.sharedemo.R;
import custom.sunday.com.sharedemo.common.util.CommonUtils;

/***
 * 此View从别处而来...
 * */
public class SwitchView extends RelativeLayout implements View.OnClickListener {

    public static final int SWITCH_ON = 10;
    public static final int SWITCH_OFF = 11;
    private static final long ANIMATOR_DURING = 200;
    protected ImageView mSwitchBgImageView;
    protected ImageView mSwitchRoundImageView;
    protected int mSwitchStatus = SWITCH_OFF;
    private boolean isAnimating = false;
    private int defaultBgW = CommonUtils.dip2px(getContext(), 44);
    private int defaultBgH = CommonUtils.dip2px(getContext(), 24);
    private int defaultRoundW = CommonUtils.dip2px(getContext(), 20);
    /**
     * 开关状态变化监听
     */
    private OnSwitchStatusChangeListener mListener;
    /**
     * 开关点击回调
     */
    private OnSwitchClickListener mSwitchClickListener;


    public SwitchView(Context context) {
        super(context);
        init();
    }

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 控件初始化
     */
    protected void init() {
        View.inflate(getContext(), R.layout.view_switchview, this);
        mSwitchBgImageView = (ImageView) findViewById(R.id.iv_switch_bg);
        mSwitchRoundImageView = (ImageView) findViewById(R.id.iv_switch_round);
        mSwitchBgImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!isEnabled()) {
            return;
        }
        if (isAnimating) {
            return;
        }
        isAnimating = true;
        if (mSwitchClickListener != null) {
            mSwitchClickListener.onSwitchClick(SwitchView.this);
        }
        LayoutParams layoutParams = (LayoutParams) mSwitchRoundImageView.getLayoutParams();
        if (layoutParams.leftMargin > 0) {
            layoutParams.leftMargin = 0;
            mSwitchRoundImageView.setLayoutParams(layoutParams);
        }
        changeSwitchStatus(true);
    }

    public void initSwitchStatus(boolean isOn){
        initSwitchStatus(isOn ? SWITCH_ON : SWITCH_OFF);
    }

    /**
     * 初始化开关状态
     *
     * @param status
     */
    public void initSwitchStatus(int status) {
        if (status == mSwitchStatus) {
            return;
        }
        mSwitchStatus = status;
//        RelativeLayout.LayoutParams layoutParams = (LayoutParams) mSwitchRoundImageView.getLayoutParams();
//        mSwitchRoundImageView.setLayoutParams(layoutParams);
        mSwitchRoundImageView.setX(CommonUtils.dip2px(getContext(), 20));
        changeSwitchBgColor();
//        changeSwitchStatus(true);
    }

    /**
     * 如果界面构建结束初始化请调用该函数
     *
     * @param status
     */
    public void initSwStAfterResume(int status) {
        if (status == mSwitchStatus) {
            return;
        }
        mSwitchStatus = status;
        int bgWidth = mSwitchBgImageView.getWidth();
        int roundWidth = mSwitchRoundImageView.getWidth();
        LayoutParams layoutParams = (LayoutParams) mSwitchRoundImageView.getLayoutParams();
        int leftMargin = layoutParams.leftMargin;
        int rightSpace = bgWidth - roundWidth-leftMargin;
        mSwitchRoundImageView.setX(rightSpace);
        changeSwitchBgColor();
    }

    /**
     * 改变开关状态
     */
    private void changeSwitchStatus(boolean animated) {
        int bgWidth = mSwitchBgImageView.getWidth();
        int bgHeight = mSwitchBgImageView.getHeight();
        int roundHeight = mSwitchRoundImageView.getHeight();
        if (bgWidth == 0) {
            bgWidth = defaultBgW;
            bgHeight = defaultBgH;
            roundHeight = defaultRoundW;
        }
        int margin = (bgHeight - roundHeight) / 2;
        if (!animated) {
            Log.e("开关", bgWidth + " " + bgHeight + " " + roundHeight);
            mSwitchRoundImageView.setLeft((mSwitchStatus == SWITCH_OFF) ? bgWidth - margin - roundHeight : margin);
            return;
        }
        float from = (mSwitchStatus == SWITCH_OFF) ? margin : bgWidth - margin - roundHeight;
        float to = (mSwitchStatus == SWITCH_OFF) ? bgWidth - margin - roundHeight : margin;
        mSwitchStatus = (mSwitchStatus == SWITCH_ON) ? SWITCH_OFF : SWITCH_ON;
        ObjectAnimator animator = ObjectAnimator.ofFloat(mSwitchRoundImageView, "translationX", from, to)
                .setDuration(ANIMATOR_DURING);
        animator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                changeSwitchBgColor();
                if (mListener != null) {
                    mListener.onSwitchStatusChange(SwitchView.this, mSwitchStatus);
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    /**
     * 开关是否处于开启状态
     *
     * @return
     */
    public boolean isOn() {
        return mSwitchStatus == SWITCH_ON;
    }

    /**
     * 改变开关的状态
     *
     * @param status
     */
    public void setSwitchStatus(int status) {
        if (status != mSwitchStatus) {
            LayoutParams layoutParams = (LayoutParams) mSwitchRoundImageView.getLayoutParams();
            if (layoutParams.leftMargin > 0) {
                layoutParams.leftMargin = 0;
                mSwitchRoundImageView.setLayoutParams(layoutParams);
            }
            changeSwitchStatus(true);
        }
    }

    /**
     * 改变开关背景的资源
     */
    protected void changeSwitchBgColor() {
        if (!isEnabled()) {
            mSwitchRoundImageView.setImageResource(R.mipmap.switch_round_disable);
            mSwitchBgImageView.setImageResource(R.mipmap.switch_bg_disable);
            return;
        }


        if (mSwitchStatus == SWITCH_OFF) {
            mSwitchRoundImageView.setImageResource(R.mipmap.switch_round_disable);
            mSwitchBgImageView.setImageResource(R.mipmap.switch_bg_close);
        } else {
            mSwitchRoundImageView.setImageResource(R.mipmap.switch_round_enable);
            mSwitchBgImageView.setImageResource(R.mipmap.switch_bg_open);
        }
    }

    public void setOnSwitchStatusChangeListener(OnSwitchStatusChangeListener listener) {
        mListener = listener;
    }

    public void setOnSwitchClickListener(OnSwitchClickListener switchClickListener) {
        mSwitchClickListener = switchClickListener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            mSwitchRoundImageView.setImageResource(mSwitchStatus == SWITCH_OFF ? R.mipmap.switch_round_disable : R.mipmap.switch_round_enable);
            mSwitchBgImageView.setImageResource(mSwitchStatus == SWITCH_OFF ? R.mipmap.switch_bg_close : R.mipmap.switch_bg_open);
        } else {
            mSwitchRoundImageView.setImageResource(R.mipmap.switch_round_disable);
            mSwitchBgImageView.setImageResource(R.mipmap.switch_bg_disable);
        }
    }

    public interface OnSwitchStatusChangeListener {
        /**
         * 开关状态变化监听回调函数
         *
         * @param switchView   开关控件
         * @param switchStatus 开关状态  SWITCH_ON为打开状态  SWITCH_OFF为关闭状态
         */
        void onSwitchStatusChange(SwitchView switchView, int switchStatus);
    }

    public interface OnSwitchClickListener {
        void onSwitchClick(SwitchView view);
    }
}
