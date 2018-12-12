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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.adapter.FenDianEditListAdapter;
import com.jinguanjiacaigouban.bean.proCsAddBean;
import com.jinguanjiacaigouban.bean.proCsInsertBean;
import com.jinguanjiacaigouban.bean.proFdlxEditBean;
import com.jinguanjiacaigouban.bean.proFdlxFdBean;
import com.jinguanjiacaigouban.bean.proPymBean;
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
 * @date 2018/12/7
 * 功能描述：
 */
public class FenDianEditActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_Save_Fendian)
    LinearLayout llSaveFendian;
    @BindView(R.id.tv_BH)
    TextView tvBH;
    @BindView(R.id.et_MC)
    EditText etMC;
    @BindView(R.id.et_JM)
    EditText etJM;
    @BindView(R.id.rv_fendian_list)
    JinGuanJiaRecycleView rvFendianList;
    @BindView(R.id.tv_cont)
    TextView tvCont;
    @BindView(R.id.tv_ADD)
    TextView tvADD;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.rl_add_fendian)
    RelativeLayout rlAddFendian;
    private String type;
    private String strBH;
    private Dialog dialog;
    private LinearLayoutManager line;
    private FenDianEditListAdapter adapter;

    @Override
    protected int setthislayout() {
        return R.layout.activcity_fendian_edit;
    }

    @Override
    protected void initview() {
        dialog = Utils.showLoadingDialog(context);
        type = getIntent().getStringExtra("type");
        strBH = getIntent().getStringExtra("strBH");

        if (!TextUtils.isEmpty(type)) {
            tvTitle.setText("分店_修改");
        } else {
            tvTitle.setText("分店_新增");
        }

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

    boolean input = false;

    @Override
    protected void initListener() {
        flBack.setOnClickListener(this);
        llSaveFendian.setOnClickListener(this);
        rlAddFendian.setOnClickListener(this);

        etMC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (input) {
                    if (!TextUtils.isEmpty(editable.toString().trim())) {
                        dialog.show();
                        getproPymData(editable.toString().trim(), etJM);
                    }
                }
            }
        });

    }


    @Override
    protected void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(strBH)) {
            dialog.show();
            getData(strBH);
        } else {
            dialog.show();
            getproCsAdd();
            input = true;
        }
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
            case R.id.ll_Save_Fendian:
                if (checkMessage()) {
                    if (!TextUtils.isEmpty(type)) {
                        getProCsData("pro_fdlx_update"
                                , String.valueOf(SpUtil.get(context, "MC", ""))
                                , (String) SpUtil.get(context, "androidIMEI", "")
                                , UrlUtils.BBH
                                , tvBH.getText().toString()
                                , etMC.getText().toString()
                                , etJM.getText().toString()
                        );
                    } else {
                        getProCsData("pro_fdlx_insert"
                                , String.valueOf(SpUtil.get(context, "MC", ""))
                                , (String) SpUtil.get(context, "androidIMEI", "")
                                , UrlUtils.BBH
                                , tvBH.getText().toString()
                                , etMC.getText().toString()
                                , etJM.getText().toString()
                        );
                    }
                }
                break;
            case R.id.rl_add_fendian:
                startActivity(new Intent(context, AddFenDianActivity.class)
                        .putExtra("strBH", strBH)
                        .putExtra("MC", etMC.getText().toString())
                );
                break;
            default:
                break;
        }
    }

    /*
     *校验保存必须录入信息
     */
    private boolean checkMessage() {

        String JM = etJM.getText().toString().trim();
        String Mc = etMC.getText().toString().trim();

        if (TextUtils.isEmpty(JM)) {
            CommomDialog.showMessage(context, "请输入简码");
            return false;
        }

        if (TextUtils.isEmpty(Mc)) {
            CommomDialog.showMessage(context, "请输入名称信息");
            return false;
        }

        return true;
    }


    public void getProCsData(final String host, final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_edit = DBService.doConnection(host, key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_cs_edit)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }
                    final List<proCsInsertBean> proCsInsertBeans = proCsInsertBean.arrayproCsInsertBeanFromData(pro_cs_edit);
                    if (TextUtils.isEmpty(proCsInsertBeans.get(0).getErr().toString().trim())) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                EasyToast.showShort(context, "操作成功");
                                rlAddFendian.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                CommomDialog.showMessage(context, proCsInsertBeans.get(0).getErr().toString().trim());
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


    public void getproPymData(final String key, final EditText et) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    final String pro_pym = DBService.doConnection("pro_pym", key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_pym)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            List<proPymBean> proPymBeans = proPymBean.arrayproPymBeanFromData(pro_pym);
                            if (TextUtils.isEmpty(proPymBeans.get(0).getErr())) {
                                et.setText(proPymBeans.get(0).getPYM());
                            } else {
                                CommomDialog.showMessage(context, proPymBeans.get(0).getErr());
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

    public void getData(final String key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    String pro_fdlx_edit = DBService.doConnection("pro_fdlx_edit", key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_fdlx_edit)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }
                    final List<proFdlxEditBean> proFdlxEditBeans = proFdlxEditBean.arrayproFdlxEditBeanFromData(pro_fdlx_edit);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(proFdlxEditBeans.get(0).getErr())) {
                                if ("1".equals(proFdlxEditBeans.get(0).getCol0())) {
                                    etMC.setFocusable(false);
                                    etMC.setEnabled(false);
                                    etJM.setEnabled(false);
                                    etJM.setFocusable(false);
                                }

                                tvBH.setText(proFdlxEditBeans.get(0).getCol1());
                                etMC.setText(proFdlxEditBeans.get(0).getCol2());
                                etJM.setText(proFdlxEditBeans.get(0).getCol3());
                            } else {
                                CommomDialog.showMessage(context, proFdlxEditBeans.get(0).getErr());
                            }
                        }
                    });

                    input = true;

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

        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(2) {
            @Override
            public void doSth() {
                try {
                    String pro_fdlx_fd = DBService.doConnection("pro_fdlx_fd", String.valueOf(SpUtil.get(context, "MC", "")), key);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_fdlx_fd)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }


                    final List<proFdlxFdBean> proFdlxSelectBeans = proFdlxFdBean.arrayproFdlxFdBeanFromData(pro_fdlx_fd);

                    adapter = new FenDianEditListAdapter(FenDianEditActivity.this, proFdlxSelectBeans);

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

    private void getproCsAdd() {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    String pro_cs_add = DBService.doConnection("pro_fdlx_add");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_cs_add)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proCsAddBean> proCsAddBeans = proCsAddBean.arrayproCsAddBeanFromData(pro_cs_add);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(proCsAddBeans.get(0).getErr())) {
                                tvBH.setText(proCsAddBeans.get(0).getBH());
                                strBH = proCsAddBeans.get(0).getBH();
                                rlAddFendian.setVisibility(View.GONE);
                            } else {
                                CommomDialog.showMessage(context, proCsAddBeans.get(0).getErr());
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
