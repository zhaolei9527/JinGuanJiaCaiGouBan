package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.adapter.FenDianListAdapter;
import com.jinguanjiacaigouban.bean.proFdlxSelectBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.JinGuanJiaRecycleView;
import com.jinguanjiacaigouban.view.ProgressView;
import com.jinguanjiacaigouban.view.SakuraLinearLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.jinguanjiacaigouban.activity
 *
 * @author 赵磊
 * @date 2018/12/7
 * 功能描述：
 */
public class FenDianActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.ll_Add)
    LinearLayout llAdd;
    @BindView(R.id.tv_cont)
    TextView tvCont;
    @BindView(R.id.rv_fendian_list)
    JinGuanJiaRecycleView rvFendianList;
    public boolean isCheck;
    private Dialog dialog;
    private SakuraLinearLayoutManager line;
    private FenDianListAdapter adapter;

    @Override
    protected int setthislayout() {
        return R.layout.activity_fendian_list_layout;
    }

    @Override
    protected void initview() {
        line = new SakuraLinearLayoutManager(context);
        line.setOrientation(LinearLayoutManager.VERTICAL);
        rvFendianList.setLayoutManager(line);
        rvFendianList.setItemAnimator(new DefaultItemAnimator());
        ProgressView progressView = new ProgressView(context);
        progressView.setIndicatorId(ProgressView.BallRotate);
        progressView.setIndicatorColor(getResources().getColor(R.color.colorAccent));
        rvFendianList.setFootLoadingView(progressView);
        rvFendianList.loadMoreComplete();
        rvFendianList.setCanloadMore(false);
    }

    @Override
    protected void initListener() {
        flBack.setOnClickListener(this);
        llAdd.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        isCheck = getIntent().getBooleanExtra("ischeck", false);
        dialog = Utils.showLoadingDialog(context);
        dialog.show();
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
                finish();
                break;
            case R.id.ll_Add:
                startActivity(new Intent(context, FenDianEditActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_fdlx_select = DBService.doConnection("pro_fdlx_select", String.valueOf(SpUtil.get(context, "MC", "")), "");

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_fdlx_select)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proFdlxSelectBean> proFdlxSelectBeans = proFdlxSelectBean.arrayproFdlxSelectBeanFromData(pro_fdlx_select);


                    if (!TextUtils.isEmpty(proFdlxSelectBeans.get(0).getErr())) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, proFdlxSelectBeans.get(0).getErr());
                                return;
                            }
                        });
                    }


                    adapter = new FenDianListAdapter(FenDianActivity.this, proFdlxSelectBeans, tvCont);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            rvFendianList.setAdapter(adapter);
                            tvCont.setText("总计：" + proFdlxSelectBeans.size());
                        }
                    });

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

}
