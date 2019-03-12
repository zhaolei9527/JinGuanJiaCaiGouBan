package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.adapter.GongHuoShangListAdapter;
import com.jinguanjiacaigouban.bean.proCsSelectBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.JinGuanJiaRecycleView;
import com.jinguanjiacaigouban.view.ProgressView;
import com.jinguanjiacaigouban.view.SakuraLinearLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.jinguanjiacaigouban.activity
 *
 * @author 赵磊
 * @date 2018/11/20
 * 功能描述：
 */
public class GongHuoShangActivity extends BaseActivity {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.ll_Add)
    LinearLayout llAdd;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_cont)
    TextView tvCont;
    @BindView(R.id.rv_gonghuoshang_list)
    JinGuanJiaRecycleView rvGonghuoshangList;

    private Dialog dialog;
    private SakuraLinearLayoutManager line;
    private GongHuoShangListAdapter adapter;
    public boolean isCheck;
    public static String mc;

    @Override
    protected int setthislayout() {
        return R.layout.activity_gonghuoshanglist_layout;
    }

    @Override
    protected void initview() {
        line = new SakuraLinearLayoutManager(context);
        line.setOrientation(LinearLayoutManager.VERTICAL);
        rvGonghuoshangList.setLayoutManager(line);
        rvGonghuoshangList.setItemAnimator(new DefaultItemAnimator());
        ProgressView progressView = new ProgressView(context);
        progressView.setIndicatorId(ProgressView.BallRotate);
        progressView.setIndicatorColor(getResources().getColor(R.color.colorAccent));
        rvGonghuoshangList.setFootLoadingView(progressView);
        rvGonghuoshangList.loadMoreComplete();
        rvGonghuoshangList.setCanloadMore(false);
        mc = "";
    }

    @Override
    protected void initListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getData(editable.toString());
            }
        });

        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context, GongHuoShangEditActivity.class), 800);
            }
        });


        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
        } else if (resultCode == 800) {
            mc = data.getStringExtra("MC");
            EasyToast.showShort(context, mc);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(etSearch.getText().toString().trim());
    }

    @Override
    protected void initData() {
        isCheck = getIntent().getBooleanExtra("ischeck", false);
        dialog = Utils.showLoadingDialog(context);
        dialog.show();
    }

    public void getData(final String key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    String pro_cs_select = DBService.doConnection("pro_cs_select", key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_cs_select)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final ArrayList<proCsSelectBean> proCsSelectBeans = (ArrayList<proCsSelectBean>) proCsSelectBean.arrayproCsSelectBeanFromData(pro_cs_select);

                    adapter = new GongHuoShangListAdapter(GongHuoShangActivity.this, proCsSelectBeans, tvCont);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (proCsSelectBeans.isEmpty()) {
                                rvGonghuoshangList.setVisibility(View.GONE);
                                return;
                            } else {
                                rvGonghuoshangList.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    if (proCsSelectBeans.isEmpty()) {
                        return;
                    }

                    if (!TextUtils.isEmpty(proCsSelectBeans.get(0).getErr())) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, proCsSelectBeans.get(0).getErr());
                                rvGonghuoshangList.setVisibility(View.GONE);
                                return;
                            }
                        });
                    } else {
                        rvGonghuoshangList.setVisibility(View.VISIBLE);


                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                rvGonghuoshangList.setAdapter(adapter);
                                tvCont.setText("总计：" + proCsSelectBeans.size());
                                if (!TextUtils.isEmpty(GongHuoShangActivity.mc)) {
                                    for (int i = 0; i < proCsSelectBeans.size(); i++) {
                                        if (proCsSelectBeans.get(i).getMC().equals(GongHuoShangActivity.mc)) {
                                            proCsSelectBeans.get(i).setErr("1");
                                            rvGonghuoshangList.scrollToPosition(i);
                                            EasyToast.showShort(context, proCsSelectBeans.get(i).getMC());
                                        }
                                    }
                                }
                            }
                        });
                    }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
