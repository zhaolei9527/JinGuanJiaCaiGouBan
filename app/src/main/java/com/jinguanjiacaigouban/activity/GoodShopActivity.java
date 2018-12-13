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
import android.widget.TextView;

import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.adapter.GoodShopListAdapter;
import com.jinguanjiacaigouban.bean.proPmSelectBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
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
 * @date 2018/11/20
 * 功能描述：
 */
public class GoodShopActivity extends BaseActivity {

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
    @BindView(R.id.et_search_honghuoshang)
    EditText etSearchHonghuoshang;
    @BindView(R.id.img_selelte_gonghuoshang)
    ImageView imgSelelteGonghuoshang;
    private Dialog dialog;
    private SakuraLinearLayoutManager line;
    private GoodShopListAdapter adapter;
    public boolean isCheck;
    public boolean isCheckGongHuoShang = true;
    private String ghs;

    @Override
    protected int setthislayout() {
        return R.layout.activity_goodshoplist_layout;
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
                if (!TextUtils.isEmpty(editable.toString())) {
                    /**
                     *检索输入监听
                     */
                    getData(editable.toString());
                }
            }
        });

        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, GoodShopEditActivity.class));
            }
        });

        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgSelelteGonghuoshang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheckGongHuoShang = true;
                startActivityForResult(new Intent(context, GongHuoShangActivity.class).putExtra("ischeck", true), 200);
            }
        });

        etSearchHonghuoshang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    /**
                     *检索输入监听
                     */
                    getData(editable.toString());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isCheckGongHuoShang) {
            etSearchHonghuoshang.setText("");
            etSearch.setText("");
            getData("");
        } else {
            getData(etSearchHonghuoshang.getText().toString());
            isCheckGongHuoShang = false;
        }
    }

    @Override
    protected void initData() {
        isCheck = getIntent().getBooleanExtra("ischeck", false);
        ghs = getIntent().getStringExtra("GHS");

        if (!TextUtils.isEmpty(ghs)) {
            etSearchHonghuoshang.setText(ghs);
        }

        dialog = Utils.showLoadingDialog(context);
        dialog.show();
    }

    public void getData(final String key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_cs_select = DBService.doConnection("pro_pm_select", key);

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

                    final List<proPmSelectBean> proPmSelectBeans = proPmSelectBean.arrayproPmSelectBeanFromData(pro_cs_select);
                    adapter = new GoodShopListAdapter(GoodShopActivity.this, proPmSelectBeans);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            rvGonghuoshangList.setAdapter(adapter);
                            tvCont.setText("总计：" + proPmSelectBeans.size());
                        }
                    });
                    Utils.showSoundWAV(context,R.raw.susses);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            String gonghuoshang = data.getStringExtra("gonghuoshang");
            etSearchHonghuoshang.setText(gonghuoshang);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
