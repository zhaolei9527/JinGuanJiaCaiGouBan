package com.jinguanjiacaigouban.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jinguanjiacaigouban.R;

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
    ImageView image;

    @Override
    protected int setthislayout() {
        return R.layout.activity_hdimage_layout;
    }

    @Override
    protected void initview() {
        image.setBackgroundDrawable(GoodShopEditActivity.bd);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initListener() {

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