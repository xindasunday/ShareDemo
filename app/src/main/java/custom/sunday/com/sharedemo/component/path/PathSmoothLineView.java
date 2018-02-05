package custom.sunday.com.sharedemo.component.path;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import custom.sunday.com.sharedemo.R;

/**
 * Created by zhongfei.sun on 2017/8/11.
 * 此view需要一个确定的高度
 */

public class PathSmoothLineView extends View {

    //这里所有的尺寸单位都是dp
    public static final int DEFAULT_TEXT_SIZE = 12;
    public static final int DEFAULT_PATH_HEIGHT = 40;
    public static final int DEFAULT_PATH_PADDING_LEFT = 0;
    public static final int DEFAULT_PATH_PADDING_RIGHT = 74;

    public static final int DEFAULT_X_AXIS_PADDING_LEFT = 4;
    public static final int DEFAULT_X_AXIS_PADDING_RIGHT = 0;
    public static final int DEFAULT_Y_AXIS_PADDING_LEFT = 4;
    public static final int DEFAULT_Y_AXIS_PADDING_RIGHT = 0;

    public static final int DEFAULT_CYCLE_POINT_MARGIN = 15;
    public static final int DEFAULT_CYCLE_RADIUS = 9;
    public static final int DEFAULT_POINT_CYCLE_RADIUS = 3;

    public static final int DEFAULT_MAX_Y = 1000;
    public static final int DEFAULT_MIN_Y = 0;

    public static final int DEFAULT_CYCLE_TEXT_SIZE_SMALL = 8;
    public static final int DEFAULT_CYCLE_TEXT_SIZE_MID = 10;
    public static final int DEFAULT_CYCLE_TEXT_SIZE_LARGE = 12;

    public static final int DEFAULT_END_POINT_STRING_MARGIN_LEFT = 3;
    public static final int DEFAULT_END_POINT_STRING_MARGIN_TOP = 10;

    private Paint mPaint;
    private Path mPath;
    private Paint mTextPaint;
    private Paint mCycleStrokePaint;
    private Paint mCycleTextPaint;
    private PathValue mPathValue;
    private List<Point> mPointList;
    private Point mLastPoint;
    private int mMaxY = 0;
    private int mMinY = 0;
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private int mPathHeight;
    private int mPathPaddingLeft;
    private int mPathPaddingRight;
    private int mXaxisPaddingLeft;
    private int mXaxisPaddingRight;
    private int mYaxisPaddingLeft;
    private int mYaxisPaddingRight;

    private int mCyclePointMargin;
    private int mCycleRadius;

    private int mEndPointCycleRadius;

    public PathSmoothLineView(Context context) {
        this(context, null);
    }


    public PathSmoothLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathSmoothLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.PathSmoothLineView);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PATH_HEIGHT, displayMetrics);
        mPathHeight = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_path_height, height);
        if (mPathHeight < height) {
            mPathHeight = height;
        }
        mPathPaddingLeft = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_path_padding_left, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PATH_PADDING_LEFT, displayMetrics));
        mPathPaddingRight = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_path_padding_right, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PATH_PADDING_RIGHT, displayMetrics));
        mXaxisPaddingLeft = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_axis_x_padding_left, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_X_AXIS_PADDING_LEFT, displayMetrics));
        mXaxisPaddingRight = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_axis_x_padding_right, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_X_AXIS_PADDING_RIGHT, displayMetrics));
        mYaxisPaddingLeft = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_axis_y_padding_left, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_Y_AXIS_PADDING_LEFT, displayMetrics));
        mYaxisPaddingRight = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_axis_y_padding_right, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_Y_AXIS_PADDING_RIGHT, displayMetrics));
        mCycleRadius = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_cycle_radius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CYCLE_RADIUS, displayMetrics));
        mCyclePointMargin = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_cycle_point_margin, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CYCLE_POINT_MARGIN, displayMetrics));
        mEndPointCycleRadius = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_point_cycle_radius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_POINT_CYCLE_RADIUS, displayMetrics));
        mTextSize = mTypedArray.getDimensionPixelOffset(R.styleable.PathSmoothLineView_text_size, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_SIZE, displayMetrics));

        mTypedArray.recycle();
        init();
    }

    private void init() {

        int color = getResources().getColor(R.color.colorPrimary);
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        int widthDp = 1;
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, getResources().getDisplayMetrics()));
        mPaint.setAntiAlias(true);
        mPath = new Path();

        mTextPaint = new Paint();
        mTextPaint.setColor(color);
        mTextPaint.setTextSize(mTextSize);

        mCycleStrokePaint = new Paint();
        mCycleStrokePaint.setStyle(Paint.Style.STROKE);
        mCycleStrokePaint.setColor(color);
        mCycleStrokePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, getResources().getDisplayMetrics()));
        mCycleStrokePaint.setAntiAlias(true);

        mCycleTextPaint = new Paint();
        mCycleTextPaint.setColor(color);
        mCycleTextPaint.setAntiAlias(true);
    }

    public void initData() {
        PathValue pathValue = new PathValue();
        pathValue.title = "水质详情";
        pathValue.startTime = "";
        pathValue.endTime = "";
        List<Integer> list = new ArrayList<>();
        list.add(227);
//        list.add(1);
//        list.add(55);
//        list.add(56);
//        list.add(33);
//        list.add(56);
        pathValue.quality = list;
        setValue(pathValue);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (mPathValue != null) {
            drawLine3(canvas);
            drawXAxis(canvas);
            drawYAxis(canvas);
            drawEnd(canvas);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
    }

    private void drawLine3(Canvas canvas) {
        if (mPointList == null || mPointList.size() == 0) {
            calcPoint(mPathValue.quality);
        }

        int size = mPointList.size();
        Point point = mPointList.get(0);
        mPath.reset();
        mPath.moveTo(point.x, point.y);
        float preX = point.x;
        float preY = point.y;
        for (int i = 1; i < size; i++) {
            point = mPointList.get(i);
            float controlX = (preX + point.x) / 2;
            mPath.cubicTo(controlX, preY, controlX, point.y, point.x, point.y);
            preX = point.x;
            preY = point.y;
        }
        canvas.drawPath(mPath, mPaint);
    }

    public void drawXAxis(Canvas canvas) {
        float baseY = getHeight();
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float top = baseY - metrics.bottom;
        canvas.drawText(mPathValue.startTime, mXaxisPaddingLeft, top, mTextPaint);
        canvas.drawText(mPathValue.endTime, mLastPoint.x - mTextPaint.measureText(mPathValue.endTime) / 2, top, mTextPaint);
    }

    public void drawYAxis(Canvas canvas) {
        float baseY = getHeight() / 2 - mPathHeight / 2;
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float top = baseY - metrics.bottom;
        final int padding = 5;
        canvas.drawText(String.valueOf(mMaxY), mYaxisPaddingLeft, top - padding, mTextPaint);
        canvas.drawText(String.valueOf(mMinY), mYaxisPaddingLeft, top + mPathHeight - metrics.top + padding, mTextPaint);
    }

    private void drawEnd(Canvas canvas) {
        drawEndString(canvas);
        drawEndPointCycle(canvas);
        drawQualityCycle(canvas);
    }

    public void drawEndString(Canvas canvas) {
        String text = mPathValue.title;
        int size = mPointList.size();
        Point lastPoint = mPointList.get(size - 1);
        int marginTop = DEFAULT_END_POINT_STRING_MARGIN_LEFT;
        int marginLeft = DEFAULT_END_POINT_STRING_MARGIN_LEFT;
        float x = lastPoint.x +
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, marginLeft, getResources().getDisplayMetrics())
                + 15;
        float y = lastPoint.y + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, marginTop, getResources().getDisplayMetrics());
        canvas.drawText(text, x, y, mTextPaint);
    }

    public void drawEndPointCycle(Canvas canvas) {
        float x = mLastPoint.x;
        float y = mLastPoint.y;
        canvas.drawCircle(x, y, mEndPointCycleRadius, mTextPaint);
    }

    public void drawQualityCycle(Canvas canvas) {
        float x = mLastPoint.x;
        float y = mLastPoint.y - mCyclePointMargin;
        canvas.drawCircle(x, y, mCycleRadius, mCycleStrokePaint);

        String text = String.valueOf(mPathValue.quality.get(mPathValue.quality.size() - 1));
        int size;
        if (text.length() == 1) {
            size = DEFAULT_CYCLE_TEXT_SIZE_LARGE;
        } else if (text.length() == 2) {
            size = DEFAULT_CYCLE_TEXT_SIZE_MID;
        } else {
            size = DEFAULT_CYCLE_TEXT_SIZE_SMALL;
        }
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getResources().getDisplayMetrics());
        mCycleTextPaint.setTextSize(textSize);

        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float top = y + metrics.bottom;
        canvas.drawText(text, x - mCycleTextPaint.measureText(text) / 2, top, mCycleTextPaint);
    }

    public void setValue(PathValue pathValue) {
        //两点成一线
        if (pathValue.quality.size() == 1) {
            Integer value = pathValue.quality.get(0);
            pathValue.quality.add(new Integer(value));
        }
        mPointList = null;
        mPathValue = pathValue;
        postInvalidate();

    }


    /**
     * 计算水质每个点的x,y坐标
     **/
    private void calcPoint(List<Integer> list) {
        int maxY = DEFAULT_MIN_Y;
        int minY = DEFAULT_MAX_Y;
        List<Point> points = mPointList = new ArrayList<>();
        if (list == null || list.size() == 0) {
            mMaxY = mMinY = 0;
        } else {
            for (int quality : list) {
                if (quality > maxY) {
                    maxY = quality;
                }

                if (quality < minY) {
                    minY = quality;
                }
            }

            mMaxY = maxY;
            mMinY = minY;

            int size = list.size();
            float pecX = getWidth();
            if (size > 1) {
                pecX = (float) (getWidth() - mPathPaddingRight - mPathPaddingLeft) / (float) (list.size() - 1);
            }

            //计算水质点高低点和像素点高低点的比例
            float disQuality = maxY - minY;
            final int startY = getHeight() / 2 - mPathHeight / 2;
            float disPixel = mPathHeight;
            float x;
            float y;
            float scale;
            if (disQuality == 0) {
                for (int i = 0; i < size; i++) {
                    x = mPathPaddingLeft + (pecX * i);
                    y = (startY + disPixel * 0.5f);//线段居中
                    points.add(new Point(x, y));
                }
            } else {
                for (int i = 0; i < size; i++) {
                    x = mPathPaddingLeft + (pecX * i);
                    scale = (maxY - list.get(i)) / disQuality;
                    y = (startY + disPixel * scale);
                    points.add(new Point(x, y));
                }
            }

            mLastPoint = points.get(size - 1);
        }


    }

    private static class Point {
        float x;
        float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class PathValue {
        String startTime;
        String endTime;
        List<Integer> quality;
        String title;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public List<Integer> getQuality() {
            return quality;
        }

        public void setQuality(List<Integer> quality) {
            this.quality = quality;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
