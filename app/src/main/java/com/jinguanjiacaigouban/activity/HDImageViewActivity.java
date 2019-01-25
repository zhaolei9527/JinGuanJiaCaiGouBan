package com.jinguanjiacaigouban.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.jinguanjiacaigouban.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * com.jinguanjiacaigouban.activity
 *
 * @author 赵磊
 * @date 2018/12/5
 * 功能描述：
 */
public class HDImageViewActivity extends BaseActivity {

    @BindView(R.id.photoview)
    PhotoView image;
    private PhotoViewAttacher attacher;

    @Override
    protected int setthislayout() {
        return R.layout.activity_hdimage_layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initview() {

        if (GoodShopEditActivity.bitmap != null) {
            image.setImageDrawable(GoodShopEditActivity.bd);
            attacher = new PhotoViewAttacher(image);
            attacher.setRotatable(true);
            attacher.setToRightAngle(true);
        } else {
            Toast.makeText(HDImageViewActivity.this, "图片加载失败，请稍候再试！", Toast.LENGTH_SHORT)
                    .show();
        }


    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onDestroy() {
        attacher.cleanup();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}