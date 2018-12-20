package com.jinguanjiacaigouban.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.jinguanjiacaigouban.bean.proCsAddBean;
import com.jinguanjiacaigouban.bean.proDdInsertBean;
import com.jinguanjiacaigouban.bean.proYgBean;
import com.jinguanjiacaigouban.db.DBService;
import com.jinguanjiacaigouban.utils.DateUtils;
import com.jinguanjiacaigouban.utils.PriorityRunnable;
import com.jinguanjiacaigouban.utils.SpUtil;
import com.jinguanjiacaigouban.utils.UrlUtils;
import com.jinguanjiacaigouban.utils.Utils;
import com.jinguanjiacaigouban.view.CommomDialog;
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
public class OrderAddActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.ll_qiandan)
    LinearLayout llQiandan;
    private TimePickerView pvTime;
    private ArrayList<String> proYgList = new ArrayList<>();
    private Dialog dialog;
    SpinerPopWindow<String> stringSpinerPopWindow;
    private String strBH;
    private String strFKFS;
    private String intFKZQ;
    private String intSHTS;

    @Override
    protected int setthislayout() {
        return R.layout.activity_order_add_layout;
    }

    @Override
    protected void initview() {
        dialog = Utils.showLoadingDialog(context);
    }

    @Override
    protected void initListener() {
        flBack.setOnClickListener(this);
        llStartTime.setOnClickListener(this);
        imgSelelteCGY.setOnClickListener(this);
        imgSelelteGonghuoshang.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        llQiandan.setOnClickListener(this);

        strFKFS = "月结";

        cbXianjie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbYuejie.setChecked(false);
                    strFKFS = "现价";
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

    }

    @Override
    protected void initData() {
        etSearchCGY.setText(String.valueOf(SpUtil.get(context, "MC", "")));
        dialog.show();
        getproCsAdd();
        tvSearchStartTime.setText(DateUtils.getDay(System.currentTimeMillis()));

        String yue = (String) SpUtil.get(context, "yue", "");
        String tian = (String) SpUtil.get(context, "tian", "");

        if (!TextUtils.isEmpty(yue)) {
            etYue.setText(yue);
        }

        if (!TextUtils.isEmpty(tian)) {
            etTian.setText(tian);
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
            case R.id.ll_qiandan:
                startActivity(new Intent(context, OrderEditActivity.class)
                        .putExtra("strBH", strBH)
                        .putExtra("type", "前单"));
                break;
            case R.id.fl_back:
                finish();
                break;
            case R.id.img_selelte_CGY:
                if (proYgList.isEmpty()) {
                    dialog.show();
                    getDataFCGY();
                } else {
                    showSponer(proYgList, etSearchCGY);
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

                SpUtil.putAndApply(context, "yue", etYue.getText().toString());
                SpUtil.putAndApply(context, "tian", etTian.getText().toString());

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

                    showSponer(proYgList, etSearchCGY);
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

    private void showSponer(final List<String> profl1list, final EditText et) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stringSpinerPopWindow = new SpinerPopWindow<>(context, profl1list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!et.getText().toString().trim().equals(profl1list.get(i))) {
                            et.setText(profl1list.get(i));
                        }
                        stringSpinerPopWindow.dismiss();
                    }
                });
                stringSpinerPopWindow.showAsDropDown(et);
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
    }


    private void getproCsAdd() {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {

                    String pro_cs_add = DBService.doConnection("pro_dd_add");
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
                                Utils.showSoundWAV(context, R.raw.susses);

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


    private void getproDdInsert(final String... key) {
        App.pausableThreadPoolExecutor.execute(new PriorityRunnable(1) {
            @Override
            public void doSth() {
                try {
                    String pro_dd_insert = DBService.doConnection("pro_dd_insert", key[0], key[1], key[2], key[3], key[4], key[5], key[6], key[7], key[8], key[9], key[10]);
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
                                finish();
                                startActivity(new Intent(context, OrderEditActivity.class).putExtra("strBH", proDdInsertBeans.get(0).getBH()));
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

}
