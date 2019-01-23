package com.jinguanjiacaigouban.activity;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.adapter.OrderFenDianListAdapter;
import com.jinguanjiacaigouban.adapter.OrderGoodsListAdapter;
import com.jinguanjiacaigouban.bean.proDdEditBean;
import com.jinguanjiacaigouban.bean.proDdFdFdBean;
import com.jinguanjiacaigouban.bean.proDdPmBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.JinGuanJiaRecycleView;
import com.jinguanjiacaigouban.view.ProgressView;
import com.jinguanjiacaigouban.view.SakuraLinearLayoutManager;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author 赵磊
 * @date 2018/12/24
 * 功能描述：
 */
public class ShareOrderActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.rl_finsh)
    FrameLayout rlBack;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.tv_GHS)
    TextView tvGHS;
    @BindView(R.id.tv_CGR)
    TextView tvCGR;
    @BindView(R.id.tv_yue)
    TextView tvYue;
    @BindView(R.id.tv_tian)
    TextView tvTian;
    @BindView(R.id.rv_order)
    JinGuanJiaRecycleView rvOrderGoods;
    @BindView(R.id.img_wechat)
    ImageView imgWechat;
    @BindView(R.id.img_qq)
    ImageView imgQq;
    @BindView(R.id.ll_share)
    ScrollView llShare;
    @BindView(R.id.tv_FD)
    TextView tvFD;
    @BindView(R.id.tv_LJMC)
    TextView tvLJMC;
    @BindView(R.id.tv_BZ)
    TextView tvBZ;
    @BindView(R.id.rv_order_fendian)
    JinGuanJiaRecycleView rvOrderFendian;
    private Dialog dialog;
    public static String strBH;
    private OrderGoodsListAdapter adapter;
    private SakuraLinearLayoutManager line;
    private GridLayoutManager line2;
    private OrderFenDianListAdapter orderFenDianListAdapter;

    @Override
    protected int setthislayout() {
        return R.layout.activity_shareorder_layout;
    }

    @Override
    protected void initview() {

        line = new SakuraLinearLayoutManager(context);
        line.setOrientation(LinearLayoutManager.VERTICAL);
        rvOrderGoods.setLayoutManager(line);
        rvOrderGoods.setItemAnimator(new DefaultItemAnimator());
        ProgressView progressView = new ProgressView(context);
        progressView.setIndicatorId(ProgressView.BallRotate);
        progressView.setIndicatorColor(getResources().getColor(R.color.colorAccent));
        rvOrderGoods.setFootLoadingView(progressView);
        rvOrderGoods.loadMoreComplete();


        line2 = new GridLayoutManager(context, 3);
        line2.setOrientation(LinearLayoutManager.VERTICAL);
        rvOrderFendian.setLayoutManager(line2);
        rvOrderFendian.setItemAnimator(new DefaultItemAnimator());
        ProgressView progressView2 = new ProgressView(context);
        progressView2.setIndicatorId(ProgressView.BallRotate);
        progressView2.setIndicatorColor(getResources().getColor(R.color.colorAccent));
        rvOrderFendian.setFootLoadingView(progressView2);
        rvOrderFendian.loadMoreComplete();

    }

    @Override
    protected void initListener() {
        rlBack.setOnClickListener(this);
        imgWechat.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        dialog = Utils.showLoadingDialog(context);
        strBH = getIntent().getStringExtra("strBH");
        getEditData(strBH);
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
            case R.id.rl_finsh:
                finish();
                break;
            case R.id.img_wechat:
                showShare();
                break;
            case R.id.img_qq:

                break;
            default:
                break;
        }
    }

    private void showShare() {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        //微信分享网页的参数严格对照列表中微信分享网页的参数要求
        sp.setImageData(getBitmapFromView(llShare));
        sp.setShareType(Platform.SHARE_IMAGE);
        Log.d("ShareSDK", sp.toMap().toString());
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d("ShareSDK", "onComplete ---->  分享成功");

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("ShareSDK", "onError ---->  分享失败" + throwable.getStackTrace().toString());
                Log.d("ShareSDK", "onError ---->  分享失败" + throwable.getMessage());
                throwable.getMessage();
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d("ShareSDK", "onCancel ---->  分享取消");
            }
        });
// 执行图文分享
        wechat.share(sp);
    }

    public static Bitmap getBitmapFromView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    public void getEditData(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_dd_edit = DBService.doConnection("pro_dd_fx1", key[0]);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_dd_edit)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proDdEditBean> proDdEditBeans = proDdEditBean.arrayproDdEditBeanFromData(pro_dd_edit);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (TextUtils.isEmpty(proDdEditBeans.get(0).getCol1())) {
                                tvOrder.setVisibility(View.GONE);
                            }
                            if (TextUtils.isEmpty(proDdEditBeans.get(0).getCol2())) {
                                tvOrderTime.setVisibility(View.GONE);
                            }
                            if (TextUtils.isEmpty(proDdEditBeans.get(0).getCol3())) {
                                tvCGR.setVisibility(View.GONE);
                            }
                            if (TextUtils.isEmpty(proDdEditBeans.get(0).getCol4())) {
                                tvGHS.setVisibility(View.GONE);
                            }
                            if (TextUtils.isEmpty(proDdEditBeans.get(0).getCol6())) {
                                tvYue.setVisibility(View.GONE);
                            }
                            if (TextUtils.isEmpty(proDdEditBeans.get(0).getCol7())) {
                                tvTian.setVisibility(View.GONE);
                            }
                            if (TextUtils.isEmpty(proDdEditBeans.get(0).getCol8())) {
                                tvBZ.setVisibility(View.GONE);
                            }

                            strBH = proDdEditBeans.get(0).getCol1();
                            tvLJMC.setText(String.valueOf(SpUtil.get(context, "lastURL", "")) + "-采购单");
                            tvOrder.setText(proDdEditBeans.get(0).getCol1());
                            tvOrderTime.setText(proDdEditBeans.get(0).getCol2());
                            tvCGR.setText(proDdEditBeans.get(0).getCol3());
                            tvGHS.setText(proDdEditBeans.get(0).getCol4());
                            tvYue.setText(proDdEditBeans.get(0).getCol6() + "" + proDdEditBeans.get(0).getCol5());
                            tvTian.setText(proDdEditBeans.get(0).getCol7());
                            tvBZ.setText(proDdEditBeans.get(0).getCol8());
                        }
                    });

                    proDdPm(strBH);
                    proDdFd(strBH);

                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }

    private void proDdPm(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    final String pro_dd_pm = DBService.doConnection("pro_dd_fx2", key[0]);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_dd_pm)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("ShareOrderActivity", pro_dd_pm);

                            List<proDdPmBean> proDdPmBeans = proDdPmBean.arrayproDdPmBeanFromData(pro_dd_pm);
                            adapter = new OrderGoodsListAdapter(ShareOrderActivity.this, proDdPmBeans);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    rvOrderGoods.setAdapter(adapter);
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }

    private void proDdFd(final String... key) {

        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    final String pro_dd_fd = DBService.doConnection("pro_dd_fx3", key[0]);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_dd_fd)) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<proDdFdFdBean> proDdFdFdBeans = proDdFdFdBean.arrayproDdFdFdBeanFromData(pro_dd_fd);

                            orderFenDianListAdapter = new OrderFenDianListAdapter(ShareOrderActivity.this, proDdFdFdBeans, tvFD);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    rvOrderFendian.setAdapter(orderFenDianListAdapter);
                                }
                            });

                            tvFD.setText("分店（" + proDdFdFdBeans.size() + "）");

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }

}
