package custom.sunday.com.sharedemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import custom.sunday.com.sharedemo.R;
import custom.sunday.com.sharedemo.base.BaseActivity;
import custom.sunday.com.sharedemo.component.path.PathSmoothLineView;

/**
 * Created by zhongfei.sun on 2018/1/17.
 */

public class PathActivity extends BaseActivity {
    private PathSmoothLineView mPathSmoothLineView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        mPathSmoothLineView = (PathSmoothLineView) findViewById(R.id.path_smooth_line);
        mPathSmoothLineView.setValue(createSmoothData());
    }

    public PathSmoothLineView.PathValue createSmoothData(){
        PathSmoothLineView.PathValue pathValue = new PathSmoothLineView.PathValue();
        pathValue.setStartTime("0110");
        pathValue.setEndTime("0120");
        List<Integer> integers = new ArrayList<>();
        integers.add(89);
        integers.add(11);
        integers.add(90);
        pathValue.setQuality(integers);
        pathValue.setTitle("平滑曲线Demo");
        return pathValue;
    }

}
