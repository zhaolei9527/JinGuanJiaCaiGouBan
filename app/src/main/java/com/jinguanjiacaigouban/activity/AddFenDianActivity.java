package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.adapter.AddFenDianListAdapter;
import com.jinguanjiacaigouban.bean.proFdFdlxBean;
import com.jinguanjiacaigouban.bean.proFdlxFdInsertBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.UrlUtils;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.JinGuanJiaRecycleView;
import com.jinguanjiacaigouban.view.ProgressView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.jinguanjiacaigouban.activity
 *
 * @author 赵磊
 * @date 2018/12/8
 * 功能描述：
 */
public class AddFenDianActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_Save_Fendian)
    LinearLayout llSaveFendian;
    @BindView(R.id.tv_FD_Type)
    TextView tvFDType;
    @BindView(R.id.cb_CheckAll)
    CheckBox cbCheckAll;
    @BindView(R.id.tv_cont)
    TextView tvCont;
    @BindView(R.id.rl_add_fendian)
    RelativeLayout rlAddFendian;
    @BindView(R.id.rv_fendian_list)
    JinGuanJiaRecycleView rvFendianList;
    private String strBH;
    private String MC;
    private Dialog dialog;
    private AddFenDianListAdapter adapter;
    private LinearLayoutManager line;

    @Override
    protected int setthislayout() {
        return R.layout.activcity_add_fendian_edit;
    }

    @Override
    protected void initview() {
        dialog = Utils.showLoadingDialog(context);
        strBH = getIntent().getStringExtra("strBH");
        MC = getIntent().getStringExtra("MC");
        tvFDType.setText("分店类型：" + MC);
        line = new LinearLayoutManager(context);
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
        llSaveFendian.setOnClickListener(this);
        cbCheckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for (int i = 0; i < adapter.getDatas().size(); i++) {
                    if (b) {
                        adapter.getDatas().get(i).setErr("1");
                    } else {
                        adapter.getDatas().get(i).setErr("0");
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {
        dialog.show();
        getData(strBH);
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
                onBackPressed();
                break;
            case R.id.ll_Save_Fendian:
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < adapter.getDatas().size(); i++) {
                    if (adapter.getDatas().get(i).getErr().equals("1")) {
                        if (i == 0) {
                            stringBuffer.append(adapter.getDatas().get(i).getMC());
                        } else {
                            stringBuffer.append("+" + adapter.getDatas().get(i).getMC());
                        }
                    }
                }
                if (TextUtils.isEmpty(stringBuffer.toString())) {
                    CommomDialog.showMessage(context, "请先选择录入信息");
                    return;
                }
                Log.e("AddFenDianActivity", stringBuffer.toString());
                dialog.show();
                getProFdlxFdInsertData(strBH, stringBuffer.toString());
                break;
            default:
                break;
        }
    }

    public void getData(final String key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_fd_fdlx = DBService.doConnection("pro_fd_fdlx", String.valueOf(SpUtil.get(context, "MC", "")), key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_fd_fdlx)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proFdFdlxBean> proFdFdlxBeans = proFdFdlxBean.arrayproFdFdlxBeanFromData(pro_fd_fdlx);

                    adapter = new AddFenDianListAdapter(AddFenDianActivity.this, proFdFdlxBeans);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            rvFendianList.setAdapter(adapter);
                            tvCont.setText("总计：" + proFdFdlxBeans.size());
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


    public void getProFdlxFdInsertData(final String strBH, final String key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_fdlx_fd_insert = DBService.doConnection("pro_fdlx_fd_insert", String.valueOf(SpUtil.get(context, "MC", ""))
                            , (String) SpUtil.get(context, "androidIMEI", "")
                            , UrlUtils.BBH, strBH, key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_fdlx_fd_insert)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proFdlxFdInsertBean> proFdlxFdInsertBeans = proFdlxFdInsertBean.arrayproFdlxFdInsertBeanFromData(pro_fdlx_fd_insert);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(proFdlxFdInsertBeans.get(0).getErr())) {
                                EasyToast.showShort(context, "添加成功");
                                onBackPressed();
                            } else {
                                CommomDialog.showMessage(context, proFdlxFdInsertBeans.get(0).getErr());
                            }
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
