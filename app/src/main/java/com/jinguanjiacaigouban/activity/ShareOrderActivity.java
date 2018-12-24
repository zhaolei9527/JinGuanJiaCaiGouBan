package com.jinguanjiacaigouban.activity;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
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
    @BindView(R.id.tv_zhouqi)
    TextView tvZhouqi;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.tv_FD)
    TextView tvFD;
    private Dialog dialog;
    public static String strBH;
    private OrderGoodsListAdapter adapter;
    private SakuraLinearLayoutManager line;

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
        rvOrderGoods.setCanloadMore(false);
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
        getEditData(strBH, String.valueOf(SpUtil.get(context, "MC", "")), "查单");
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

    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(c);
        } else {
            c.drawColor(Color.WHITE);
        }
        // Draw view to canvas
        v.draw(c);
        return b;
    }


    public void getEditData(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_dd_edit = DBService.doConnection("pro_dd_edit", key[0], key[1], key[2]);
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

                            strBH = proDdEditBeans.get(0).getCol1();
                            tvOrder.setText("订单编号" + proDdEditBeans.get(0).getCol1());
                            tvOrderTime.setText("订单时间：" + proDdEditBeans.get(0).getCol2());
                            tvCGR.setText("采购员：" + proDdEditBeans.get(0).getCol3());
                            tvGHS.setText("供货商：" + proDdEditBeans.get(0).getCol4());
                            tvZhouqi.setText(proDdEditBeans.get(0).getCol5());
                            tvYue.setText(proDdEditBeans.get(0).getCol6() + "月");
                            tvTian.setText(proDdEditBeans.get(0).getCol7() + "天");

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

                    final String pro_dd_pm = DBService.doConnection("pro_dd_pm", key[0]);

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

                    final String pro_dd_fd = DBService.doConnection("pro_dd_fd", key[0]);

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
                            StringBuffer stringBuffer = new StringBuffer();
                            for (int i = 0; i < proDdFdFdBeans.size(); i++) {
                                if (stringBuffer.length() == 0) {
                                    if (!TextUtils.isEmpty(proDdFdFdBeans.get(i).getMC())) {
                                        stringBuffer.append("分店（" + proDdFdFdBeans.size() + "):" + proDdFdFdBeans.get(i).getMC());
                                    } else {
                                        stringBuffer.append("分店（" + proDdFdFdBeans.size() + "):" + proDdFdFdBeans.get(i).getCol1());
                                    }
                                } else {
                                    if (!TextUtils.isEmpty(proDdFdFdBeans.get(i).getMC())) {
                                        stringBuffer.append(" " + proDdFdFdBeans.get(i).getMC());
                                    } else {
                                        stringBuffer.append(" " + proDdFdFdBeans.get(i).getCol1());
                                    }
                                }
                            }

                            tvFD.setText(stringBuffer.toString());
                            
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
