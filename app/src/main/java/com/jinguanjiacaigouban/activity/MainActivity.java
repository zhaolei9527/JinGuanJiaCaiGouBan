package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.db.DBOpenHelper;
import com.jinguanjiacaigouban.utils.DateUtils;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.UrlUtils;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_Link_name)
    TextView tvLinkName;
    @BindView(R.id.tv_LoginTime)
    TextView tvLoginTime;
    @BindView(R.id.tv_UserName)
    TextView tvUserName;
    @BindView(R.id.ll_kaidan)
    LinearLayout llKaidan;
    @BindView(R.id.ll_chadan)
    LinearLayout llChadan;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.ll_zidian)
    LinearLayout llZidian;
    @BindView(R.id.ll_gonghuoshang)
    LinearLayout llGonghuoshang;
    @BindView(R.id.ll_fendian)
    LinearLayout llFendian;
    private String mc;
    private String tym;
    private String link;
    private Runnable rTime;
    private Dialog dialog;

    @Override
    protected int setthislayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initview() {
        dialog = Utils.showLoadingDialog(context);
        mc = getIntent().getStringExtra("MC");
        tym = getIntent().getStringExtra("TYM");
        link = getIntent().getStringExtra("link");
    }

    @Override
    protected void initListener() {
        flBack.setOnClickListener(this);
        llChadan.setOnClickListener(this);
        llFendian.setOnClickListener(this);
        llGonghuoshang.setOnClickListener(this);
        llKaidan.setOnClickListener(this);
        llSetting.setOnClickListener(this);
        llZidian.setOnClickListener(this);
        tvLinkName.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tvUserName.setText(mc);
        tvLinkName.setText(link);
        rTime = new Runnable() {
            @Override
            public void run() {
                long l = System.currentTimeMillis();
                String millon = DateUtils.getMillon(l);
                tvLoginTime.setText(millon);
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(rTime, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(rTime);
    }

    private boolean mIsExit;

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                EasyToast.showShort(context, "再按一次退出");
                mIsExit = true;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                break;
            case R.id.tv_Link_name:
                doTest();
                break;
            case R.id.ll_chadan:

                break;
            case R.id.ll_kaidan:

                break;
            case R.id.ll_fendian:

                break;
            case R.id.ll_gonghuoshang:
                startActivity(new Intent(context, GongHuoShangActivity.class));
                break;
            case R.id.ll_zidian:
                startActivity(new Intent(context, GoodShopActivity.class));
                break;
            case R.id.ll_setting:
                dialog.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        CommomDialog.showMessage(context, "已是最新版本");
                    }
                }, 1200);
                break;
            default:
                break;
        }
    }

    /**
     * 调用登录储存过程
     * 存储过程（pro_login），传入帐号、密码及设备ID
     */
    private void doTest() {
        dialog.show();
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String URl = (String) SpUtil.get(context, "URl#" + link, "");
                    Log.e("LoginActivity", URl);
                    String[] split = URl.split("#");
                    UrlUtils.checkUrl(split[0], split[1], split[2], split[3], split[4]);
                    java.sql.Connection conn = DBOpenHelper.getConn();
                    if (conn.isClosed()) {
                        dialog.dismiss();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                            }
                        });
                    } else {
                        dialog.dismiss();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                EasyToast.showShort(context, "链接正常");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }

}
