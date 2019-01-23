package com.jinguanjiacaigouban.activity;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.widget.Toast;

import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.view.LargeImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.jinguanjiacaigouban.activity
 *
 * @author 赵磊
 * @date 2018/12/5
 * 功能描述：
 */
public class HDImageViewActivity extends BaseActivity {

    @BindView(R.id.image)
    LargeImageView image;

    @Override
    protected int setthislayout() {
        return R.layout.activity_hdimage_layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initview() {

        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int screenW = this.getWindowManager().getDefaultDisplay().getWidth();
        int screenH = this.getWindowManager().getDefaultDisplay().getHeight()
                - statusBarHeight;
        if (GoodShopEditActivity.bitmap != null) {
            image.imageInit(GoodShopEditActivity.bitmap, screenW, screenH, statusBarHeight,
                    new LargeImageView.ICustomMethod() {
                        @Override
                        public void customMethod(Boolean currentStatus) {
                            // 当图片处于放大或缩小状态时，控制标题是否显示
                        }
                    });
        } else {
            Toast.makeText(HDImageViewActivity.this, "图片加载失败，请稍候再试！", Toast.LENGTH_SHORT)
                    .show();
        }


    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                image.mouseDown(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                image.mousePointDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                image.mouseMove(event);
                break;
            case MotionEvent.ACTION_UP:
                image.mouseUp();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}