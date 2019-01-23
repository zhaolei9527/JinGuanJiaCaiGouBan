package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.jinguanjiacaigouban.App;
import com.jinguanjiacaigouban.R;
import com.jinguanjiacaigouban.adapter.OrderFenDianListAdapter;
import com.jinguanjiacaigouban.adapter.OrderGoodsListAdapter;
import com.jinguanjiacaigouban.bean.proDdEditBean;
import com.jinguanjiacaigouban.bean.proDdFdFdBean;
import com.jinguanjiacaigouban.bean.proDdInsertBean;
import com.jinguanjiacaigouban.bean.proDdPmBean;
import com.jinguanjiacaigouban.bean.proFdlxFdBean;
import com.jinguanjiacaigouban.bean.proYgBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.DateUtils;
import com.jinguanjiacaigouban.utils.EasyToast;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.UrlUtils;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
import com.jinguanjiacaigouban.view.JinGuanJiaRecycleView;
import com.jinguanjiacaigouban.view.ProgressView;
import com.jinguanjiacaigouban.view.SpinerPopWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.jinguanjiacaigouban.activity
 *
 * @author 赵磊
 * @date 2018/12/12
 * 功能描述：
 */
public class OrderEditActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_search_start_time)
    TextView tvSearchStartTime;
    @BindView(R.id.ll_start_time)
    LinearLayout llStartTime;
    @BindView(R.id.et_search_CGY)
    EditText etSearchCGY;
    @BindView(R.id.img_selelte_CGY)
    ImageView imgSelelteCGY;
    @BindView(R.id.et_search_honghuoshang)
    EditText etSearchHonghuoshang;
    @BindView(R.id.img_selelte_gonghuoshang)
    ImageView imgSelelteGonghuoshang;
    @BindView(R.id.cb_yuejie)
    CheckBox cbYuejie;
    @BindView(R.id.cb_xianjie)
    CheckBox cbXianjie;
    @BindView(R.id.et_yue)
    EditText etYue;
    @BindView(R.id.et_tian)
    EditText etTian;
    @BindView(R.id.et_beizhu)
    EditText etBeizhu;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_BH)
    TextView tvBH;
    @BindView(R.id.rv_order_goods)
    JinGuanJiaRecycleView rvOrderGoods;
    @BindView(R.id.rv_order_fendian)
    JinGuanJiaRecycleView rvOrderFendian;
    @BindView(R.id.v_no)
    View vNo;
    @BindView(R.id.et_fd)
    EditText etFd;
    @BindView(R.id.ll_pro_fl1)
    FrameLayout llProFl1;
    @BindView(R.id.img_add_fd)
    ImageView imgAddFd;
    @BindView(R.id.cb_Check_FD)
    CheckBox cbCheckFD;
    @BindView(R.id.btn_add_good)
    Button btnAddGood;
    @BindView(R.id.ll_qiandan)
    LinearLayout llQiandan;
    @BindView(R.id.ll_houdan)
    LinearLayout llHoudan;
    @BindView(R.id.ll_xindan)
    LinearLayout llXindan;
    @BindView(R.id.tv_cont)
    TextView tvCont;
    @BindView(R.id.tv_fd_cont)
    TextView tvFdCont;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    private TimePickerView pvTime;
    private ArrayList<String> proYgList = new ArrayList<>();
    private Dialog dialog;
    SpinerPopWindow<String> stringSpinerPopWindow;
    public static String strBH;
    private String strFKFS;
    private String intFKZQ;
    private String intSHTS;
    private LinearLayoutManager line;
    private GridLayoutManager line2;
    private OrderGoodsListAdapter adapter;
    private OrderFenDianListAdapter orderFenDianListAdapter;
    private ArrayList<String> proFDList = new ArrayList<>();
    private ArrayList<String> proFDBHList;

    @Override
    protected int setthislayout() {
        return R.layout.activity_order_edit_layout;
    }

    @Override
    protected void initview() {
        dialog = Utils.showLoadingDialog(context);

        line = new LinearLayoutManager(context);
        line.setOrientation(LinearLayoutManager.VERTICAL);
        rvOrderGoods.setLayoutManager(line);
        rvOrderGoods.setItemAnimator(new DefaultItemAnimator());
        ProgressView progressView = new ProgressView(context);
        progressView.setIndicatorId(ProgressView.BallRotate);
        progressView.setIndicatorColor(getResources().getColor(R.color.colorAccent));
        rvOrderGoods.setFootLoadingView(progressView);
        rvOrderGoods.loadMoreComplete();
        rvOrderGoods.setCanloadMore(false);

        line2 = new GridLayoutManager(context, 3);
        line2.setOrientation(LinearLayoutManager.VERTICAL);
        rvOrderFendian.setLayoutManager(line2);
        rvOrderFendian.setItemAnimator(new DefaultItemAnimator());
        ProgressView progressView2 = new ProgressView(context);
        progressView2.setIndicatorId(ProgressView.BallRotate);
        progressView2.setIndicatorColor(getResources().getColor(R.color.colorAccent));
        rvOrderFendian.setFootLoadingView(progressView2);
        rvOrderFendian.loadMoreComplete();
        rvOrderFendian.setCanloadMore(false);

    }

    @Override
    protected void initListener() {
        flBack.setOnClickListener(this);
        llStartTime.setOnClickListener(this);
        imgSelelteCGY.setOnClickListener(this);
        imgSelelteGonghuoshang.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        llProFl1.setOnClickListener(this);
        imgAddFd.setOnClickListener(this);
        cbCheckFD.setOnClickListener(this);
        btnAddGood.setOnClickListener(this);

        llQiandan.setOnClickListener(this);
        llHoudan.setOnClickListener(this);
        llXindan.setOnClickListener(this);
        llShare.setOnClickListener(this);

        strFKFS = "月结";

        cbXianjie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbYuejie.setChecked(false);
                    strFKFS = "现付";
                }
            }
        });

        cbYuejie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbXianjie.setChecked(false);
                    strFKFS = "月结";
                }
            }
        });

        cbCheckFD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cbCheckFD.setChecked(true);
            }
        });

        etSearchCGY.setText(String.valueOf(SpUtil.get(context, "MC", "")));

    }

    @Override
    protected void initData() {
        dialog.show();
        strBH = getIntent().getStringExtra("strBH");

        String type = getIntent().getStringExtra("type");

        if (!TextUtils.isEmpty(type)) {
            getEditData(strBH, String.valueOf(SpUtil.get(context, "MC", "")), "前单");
        } else {
            getEditData(strBH, String.valueOf(SpUtil.get(context, "MC", "")), "查单");
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
            case R.id.ll_share:
                startActivity(new Intent(context, ShareOrderActivity.class)
                        .putExtra("strBH", strBH));
                break;
            case R.id.ll_xindan:
                startActivity(new Intent(context, OrderAddActivity.class));
                finish();
                break;
            case R.id.ll_houdan:
                getEditData(strBH, String.valueOf(SpUtil.get(context, "MC", "")), "后单");
                break;
            case R.id.ll_qiandan:
                getEditData(strBH, String.valueOf(SpUtil.get(context, "MC", "")), "前单");
                break;
            case R.id.btn_add_good:
                startActivityForResult(new Intent(context, GoodShopActivity.class)
                                .putExtra("ischeck", true)
                                .putExtra("oid", tvBH.getText().toString())
                                .putExtra("GHS", etSearchHonghuoshang.getText().toString())
                                .putExtra("BH", tvBH.getText().toString())
                        , 205);
                break;
            case R.id.cb_Check_FD:

                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < orderFenDianListAdapter.getDatas().size(); i++) {
                    if (TextUtils.isEmpty(orderFenDianListAdapter.getDatas().get(i).getErr())) {
                        if (!TextUtils.isEmpty(orderFenDianListAdapter.getDatas().get(i).getMC())) {
                            if (i == 0) {
                                stringBuffer.append(orderFenDianListAdapter.getDatas().get(i).getMC());
                            } else {
                                stringBuffer.append("+" + orderFenDianListAdapter.getDatas().get(i).getMC());
                            }
                        } else {
                            if (i == 0) {
                                stringBuffer.append(orderFenDianListAdapter.getDatas().get(i).getCol1());
                            } else {
                                stringBuffer.append("+" + orderFenDianListAdapter.getDatas().get(i).getCol1());
                            }
                        }
                    }
                }

                getProDdFdInsert(String.valueOf(SpUtil.get(context, "MC", ""))
                        , (String) SpUtil.get(context, "androidIMEI", "")
                        , UrlUtils.BBH
                        , strBH
                        , stringBuffer.toString()
                );

                break;
            case R.id.img_add_fd:
                startActivityForResult(new Intent(context, FenDianActivity.class).putExtra("ischeck", true), 204);
                break;
            case R.id.ll_pro_fl1:
                if (proFDList.isEmpty()) {
                    dialog.show();
                    getproFdlx();
                } else {
                    showSponer(proFDList, etFd, "1");
                }
                break;
            case R.id.fl_back:
                setResult(800, new Intent().putExtra("MC", tvBH.getText().toString()));
                OrderSearchActivity.bh = tvBH.getText().toString();
                Log.e("OrderEditActivity", "失去焦点" + tvBH.getText().toString());
                finish();
                break;
            case R.id.img_selelte_CGY:
                if (proYgList.isEmpty()) {
                    dialog.show();
                    getDataFCGY();
                } else {
                    showSponer(proYgList, etSearchCGY, "");
                }
                break;
            case R.id.img_selelte_gonghuoshang:
                startActivityForResult(new Intent(context, GongHuoShangActivity.class).putExtra("ischeck", true), 203);
                break;
            case R.id.ll_start_time:
                initTimePicker(getString(R.string.Thestarttime), "start");
                pvTime.show();
                break;
            case R.id.btn_submit:

                if (TextUtils.isEmpty(tvSearchStartTime.getText())) {
                    CommomDialog.showMessage(context, "请完善录入数据");
                    return;
                }

                if (TextUtils.isEmpty(etSearchCGY.getText())) {
                    CommomDialog.showMessage(context, "请完善录入数据");
                    return;
                }

                if (TextUtils.isEmpty(etSearchHonghuoshang.getText())) {
                    CommomDialog.showMessage(context, "请完善录入数据");
                    return;
                }

                if (TextUtils.isEmpty(etYue.getText())) {
                    CommomDialog.showMessage(context, "请完善录入数据");
                    return;
                }

                if (TextUtils.isEmpty(etTian.getText())) {
                    CommomDialog.showMessage(context, "请完善录入数据");
                    return;
                }

                dialog.show();

                getproDdInsert(
                        String.valueOf(SpUtil.get(context, "MC", ""))
                        , (String) SpUtil.get(context, "androidIMEI", "")
                        , UrlUtils.BBH
                        , strBH
                        , tvSearchStartTime.getText().toString()
                        , etSearchCGY.getText().toString()
                        , etSearchHonghuoshang.getText().toString()
                        , strFKFS
                        , etYue.getText().toString()
                        , etTian.getText().toString()
                        , etBeizhu.getText().toString()
                );

                break;
            default:
                break;
        }
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

                            if (!TextUtils.isEmpty(proDdEditBeans.get(0).getErr())) {
                                CommomDialog.showMessage(context, proDdEditBeans.get(0).getErr());
                                return;
                            }

                            if (TextUtils.isEmpty(proDdEditBeans.get(0).getCol0())) {
                                tvBH.setText(proDdEditBeans.get(0).getCol1());
                                strBH = proDdEditBeans.get(0).getCol1();

                                proDdPm(strBH);
                                proDdFd(strBH);

                                tvSearchStartTime.setText(proDdEditBeans.get(0).getCol2());
                                etSearchCGY.setText(proDdEditBeans.get(0).getCol3());
                                etSearchHonghuoshang.setText(proDdEditBeans.get(0).getCol4());

                                if ("现付".equals(proDdEditBeans.get(0).getCol5())) {
                                    cbXianjie.setChecked(true);
                                } else if ("月结".equals(proDdEditBeans.get(0).getCol5())) {
                                    cbYuejie.setChecked(true);
                                }

                                etYue.setText(proDdEditBeans.get(0).getCol6());
                                etTian.setText(proDdEditBeans.get(0).getCol7());
                                etBeizhu.setText(proDdEditBeans.get(0).getCol8());

                                vNo.setVisibility(View.GONE);
                            } else {
                                vNo.setVisibility(View.VISIBLE);
                            }

                            etFd.setText("");
                            Utils.showSoundWAV(context, R.raw.susses);

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


    public void getDataFCGY() {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_yg = DBService.doConnection("pro_yg");

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_yg)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    List<proYgBean> proYgBeans = proYgBean.arrayproYgBeanFromData(pro_yg);

                    proYgList = new ArrayList<>();

                    for (int i = 0; i < proYgBeans.size(); i++) {
                        proYgList.add(proYgBeans.get(i).getMC());
                    }

                    showSponer(proYgList, etSearchCGY, "");
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }

    public void getproFdlx() {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    String pro_yg = DBService.doConnection("pro_fdlx", String.valueOf(SpUtil.get(context, "MC", "")));

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_yg)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    List<proYgBean> proYgBeans = proYgBean.arrayproYgBeanFromData(pro_yg);

                    proFDList = new ArrayList<>();
                    proFDBHList = new ArrayList<>();


                    for (int i = 0; i < proYgBeans.size(); i++) {
                        proFDList.add(proYgBeans.get(i).getMC());
                        proFDBHList.add(proYgBeans.get(i).getBH());
                    }

                    showSponer(proFDList, etFd, "1");
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                        }
                    });
                }
            }
        });
    }

    private void showSponer(final List<String> profl1list, final EditText et, final String type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stringSpinerPopWindow = new SpinerPopWindow<>(context, profl1list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (TextUtils.isEmpty(type)) {
                            if (!et.getText().toString().trim().equals(profl1list.get(i))) {
                                et.setText(profl1list.get(i));
                            }
                        } else {
                            String s = proFDBHList.get(i);
                            etFd.setText(profl1list.get(i));
                            getpro_fdlx_fd(String.valueOf(SpUtil.get(context, "MC", "")), s);
                        }
                        stringSpinerPopWindow.dismiss();
                    }
                });
                stringSpinerPopWindow.showAsDropDown(et);
            }
        });
    }

    private void getpro_fdlx_fd(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_fdlx_fd = DBService.doConnection("pro_fdlx_fd", key[0], key[1]);
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

                    final List<proFdlxFdBean> proFdlxFdBeans = proFdlxFdBean.arrayproFdlxFdBeanFromData(pro_fdlx_fd);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            orderFenDianListAdapter.getDatas().clear();

                            for (int i = 0; i < proFdlxFdBeans.size(); i++) {
                                proDdFdFdBean proDdFdBean = new proDdFdFdBean();
                                proDdFdBean.setErr(proFdlxFdBeans.get(i).getErr());
                                proDdFdBean.setMC(proFdlxFdBeans.get(i).getMC());
                                proDdFdBean.setBH(proFdlxFdBeans.get(i).getBH());
                                orderFenDianListAdapter.getDatas().add(proDdFdBean);
                            }

                            tvFdCont.setText("分店: " + orderFenDianListAdapter.getDatas().size());
                            orderFenDianListAdapter.notifyDataSetChanged();
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

    private void initTimePicker(String title, final String TYPE) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2016, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2022, 12, 31);
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if ("start".equals(TYPE)) {
                    tvSearchStartTime.setText(DateUtils.getDay(date.getTime()));
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setTitleBgColor(getResources().getColor(R.color.bgtitle))
                .setCancelColor(getResources().getColor(R.color.text))
                .setSubmitColor(getResources().getColor(R.color.text))
                .setTitleText(title)
                .setTitleColor(getResources().getColor(R.color.text))
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200 && requestCode == 203) {
            etSearchHonghuoshang.setText(data.getStringExtra("gonghuoshang"));
        }

        if (resultCode == 204 && requestCode == 204) {
            etFd.setText(data.getStringExtra("MC"));
            getpro_fdlx_fd(String.valueOf(SpUtil.get(context, "MC", "")), data.getStringExtra("BH"));
        }

        if (resultCode == 205 && requestCode == 205) {

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < orderFenDianListAdapter.getDatas().size(); i++) {
                if (TextUtils.isEmpty(orderFenDianListAdapter.getDatas().get(i).getErr())) {
                    if (i == 0) {
                        stringBuffer.append(orderFenDianListAdapter.getDatas().get(i).getMC());
                    } else {
                        stringBuffer.append("+" + orderFenDianListAdapter.getDatas().get(i).getMC());
                    }
                }
            }

            getproDdPmInser(String.valueOf(SpUtil.get(context, "MC", ""))
                    , (String) SpUtil.get(context, "androidIMEI", "")
                    , UrlUtils.BBH
                    , strBH
                    , data.getStringExtra("MC")
                    , stringBuffer.toString()
                    , data.getStringExtra("BH")
                    , data.getStringExtra("DJ")
                    , data.getStringExtra("SL")
            );
        }

    }

    private void getproDdPmInser(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_dd_pm_inser = DBService.doConnection("pro_dd_pm_insert", key[0], key[1], key[2], key[3], key[4], key[5]);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if (TextUtils.isEmpty(pro_dd_pm_inser)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proDdInsertBean> proDdInsertBeans = proDdInsertBean.arrayproDdInsertBeanFromData(pro_dd_pm_inser);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(proDdInsertBeans.get(0).getErr())) {
                                EasyToast.showShort(context, "保存成功");
                                proDdPmBean proDdPmBean = new proDdPmBean();
                                proDdPmBean.setCol1(key[6]);
                                proDdPmBean.setCol2(key[4]);
                                proDdPmBean.setCol3(key[7]);
                                proDdPmBean.setCol4(key[8]);
                                proDdPmBean.setCol5("");
                                adapter.getDatas().add(proDdPmBean);
                                adapter.notifyDataSetChanged();
                                Utils.showSoundWAV(context, R.raw.susses);

                                tvCont.setText("商品: " + adapter.getDatas().size());

                                if (!adapter.getDatas().isEmpty()) {
                                    etSearchHonghuoshang.setFocusable(false);
                                    imgSelelteGonghuoshang.setVisibility(View.GONE);
                                } else {
                                    etSearchHonghuoshang.setFocusable(true);
                                    imgSelelteGonghuoshang.setVisibility(View.VISIBLE);
                                }

                                proDdPm(strBH);

                            } else {
                                CommomDialog.showMessage(context, proDdInsertBeans.get(0).getErr());
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


    private void getProDdFdInsert(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_dd_insert = DBService.doConnection("pro_dd_fd_insert", key[0], key[1], key[2], key[3], key[4]);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_dd_insert)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proDdInsertBean> proDdInsertBeans = proDdInsertBean.arrayproDdInsertBeanFromData(pro_dd_insert);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(proDdInsertBeans.get(0).getErr())) {
                                EasyToast.showShort(context, "保存成功");
                                Utils.showSoundWAV(context, R.raw.susses);

                            } else {
                                CommomDialog.showMessage(context, proDdInsertBeans.get(0).getErr());
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


    private void getproDdInsert(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_dd_insert = DBService.doConnection("pro_dd_update", key[0], key[1], key[2], key[3], key[4], key[5], key[6], key[7], key[8], key[9], key[10]);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    if (TextUtils.isEmpty(pro_dd_insert)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommomDialog.showMessage(context, "链接异常，请检查链接信息");
                                return;
                            }
                        });
                    }

                    final List<proDdInsertBean> proDdInsertBeans = proDdInsertBean.arrayproDdInsertBeanFromData(pro_dd_insert);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(proDdInsertBeans.get(0).getErr())) {
                                EasyToast.showShort(context, "保存成功");
                                Utils.showSoundWAV(context, R.raw.susses);

                            } else {
                                CommomDialog.showMessage(context, proDdInsertBeans.get(0).getErr());
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
                            final List<proDdPmBean> proDdPmBeans = proDdPmBean.arrayproDdPmBeanFromData(pro_dd_pm);
                            tvCont.setText("商品: " + proDdPmBeans.size());
                            // 倒序排列
                            adapter = new OrderGoodsListAdapter(OrderEditActivity.this, proDdPmBeans, tvCont, etSearchHonghuoshang, imgSelelteGonghuoshang);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    rvOrderGoods.setAdapter(adapter);

                                    nsv.scrollTo(0, 280 * adapter.getItemCount() + (int) (getResources().getDimension(R.dimen.y290)));

                                    if (!adapter.getDatas().isEmpty()) {
                                        etSearchHonghuoshang.setFocusable(false);
                                        imgSelelteGonghuoshang.setVisibility(View.GONE);
                                    } else {
                                        etSearchHonghuoshang.setFocusable(true);
                                        imgSelelteGonghuoshang.setVisibility(View.VISIBLE);
                                    }

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

                            tvFdCont.setText("分店: " + proDdFdFdBeans.size());

                            orderFenDianListAdapter = new OrderFenDianListAdapter(OrderEditActivity.this, proDdFdFdBeans, tvFdCont);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    rvOrderFendian.setAdapter(orderFenDianListAdapter);
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

    @Override
    protected void onPause() {
        super.onPause();
    }
}
